package com.storyhub.controller;

import com.storyhub.config.CurrentUser;
import com.storyhub.model.*;
import com.storyhub.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Everything about stories: browse, search, read, write/edit/delete, like, rate, comment, favorite. */
@Controller @RequiredArgsConstructor
public class StoryController {
    private final StoryRepository stories;
    private final CommentRepository comments;
    private final LikeRepository likes;
    private final FavoriteRepository favorites;
    private final RatingRepository ratings;
    private final CurrentUser currentUser;

    // =============== BROWSE ===============
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featured", stories.findFeatured(PageRequest.of(0, 3)));
        model.addAttribute("latest", stories.findAllByOrderByCreatedAtDesc());
        return "home";
    }

    @GetMapping("/category/{name}")
    public String byCategory(@PathVariable String name, Model model) {
        model.addAttribute("heading", name + " stories");
        model.addAttribute("stories", stories.findByCategoryOrderByCreatedAtDesc(name));
        return "list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q, Model model) {
        model.addAttribute("heading", "Results for “" + q + "”");
        model.addAttribute("stories", q.isBlank() ? List.of() : stories.search(q.trim()));
        return "list";
    }

    @GetMapping("/favorites")
    public String myFavorites(HttpSession session, Model model) {
        User u = currentUser.get(session);
        if (u == null) return "redirect:/login";
        List<Story> saved = favorites.findByUserOrderByIdDesc(u).stream().map(Favorite::getStory).toList();
        model.addAttribute("heading", "Your bookmarks");
        model.addAttribute("stories", saved);
        return "list";
    }

    // =============== READ ===============
    @GetMapping("/stories/{id}")
    public String read(@PathVariable Long id, HttpSession session, Model model) {
        Story story = stories.findById(id).orElse(null);
        if (story == null) return "redirect:/";
        User u = currentUser.get(session);

        Double avg = ratings.average(story);
        model.addAttribute("story", story);
        model.addAttribute("comments", comments.findByStoryOrderByCreatedAtDesc(story));
        model.addAttribute("likeCount", likes.countByStory(story));
        model.addAttribute("avgRating", avg == null ? 0.0 : Math.round(avg * 10) / 10.0);
        model.addAttribute("ratingCount", ratings.countByStory(story));
        // Per-user state (false / 0 when logged out)
        model.addAttribute("liked", u != null && likes.findByStoryAndUser(story, u).isPresent());
        model.addAttribute("favorited", u != null && favorites.findByStoryAndUser(story, u).isPresent());
        model.addAttribute("myRating", u == null ? 0 : ratings.findByStoryAndUser(story, u).map(Rating::getStars).orElse(0));
        return "story";
    }

    // =============== WRITE / EDIT / DELETE ===============
    @GetMapping("/stories/new")
    public String newForm(HttpSession session, Model model) {
        if (currentUser.get(session) == null) return "redirect:/login";
        model.addAttribute("story", new Story());
        model.addAttribute("action", "/stories");
        return "editor";
    }

    @PostMapping("/stories")
    public String create(@RequestParam String title, @RequestParam String category,
                         @RequestParam String content, HttpSession session, Model model) {
        User u = currentUser.get(session);
        if (u == null) return "redirect:/login";

        Story s = new Story();
        s.setAuthor(u);
        return save(s, title, category, content, "/stories", model);
    }

    @GetMapping("/stories/{id}/edit")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        Story s = stories.findById(id).orElse(null);
        User u = currentUser.get(session);
        if (s == null || u == null || !s.getAuthor().getId().equals(u.getId())) return "redirect:/";  // authors only
        model.addAttribute("story", s);
        model.addAttribute("action", "/stories/" + id + "/edit");
        return "editor";
    }

    @PostMapping("/stories/{id}/edit")
    public String update(@PathVariable Long id, @RequestParam String title, @RequestParam String category,
                         @RequestParam String content, HttpSession session, Model model) {
        Story s = stories.findById(id).orElse(null);
        User u = currentUser.get(session);
        if (s == null || u == null || !s.getAuthor().getId().equals(u.getId())) return "redirect:/";
        return save(s, title, category, content, "/stories/" + id + "/edit", model);
    }

    @PostMapping("/stories/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        Story s = stories.findById(id).orElse(null);
        User u = currentUser.get(session);
        if (s != null && u != null && s.getAuthor().getId().equals(u.getId())) stories.delete(s);
        return "redirect:/profile";
    }

    /** Shared by create + update: validate, then save. */
    private String save(Story s, String title, String category, String content, String action, Model model) {
        s.setTitle(title.trim()); s.setCategory(category); s.setContent(content.trim());
        if (title.isBlank() || content.isBlank()) {
            model.addAttribute("story", s);
            model.addAttribute("action", action);
            model.addAttribute("error", "Title and story text cannot be empty.");
            return "editor";
        }
        stories.save(s);
        return "redirect:/stories/" + s.getId();
    }

    // =============== ENGAGEMENT (all need login) ===============
    @PostMapping("/stories/{id}/like")
    public String toggleLike(@PathVariable Long id, HttpSession session) {
        User u = currentUser.get(session); Story s = stories.findById(id).orElse(null);
        if (u == null) return "redirect:/login";
        if (s == null) return "redirect:/";
        likes.findByStoryAndUser(s, u).ifPresentOrElse(likes::delete, () -> likes.save(new StoryLike(s, u)));
        return "redirect:/stories/" + id;
    }

    @PostMapping("/stories/{id}/favorite")
    public String toggleFavorite(@PathVariable Long id, HttpSession session) {
        User u = currentUser.get(session); Story s = stories.findById(id).orElse(null);
        if (u == null) return "redirect:/login";
        if (s == null) return "redirect:/";
        favorites.findByStoryAndUser(s, u).ifPresentOrElse(favorites::delete, () -> favorites.save(new Favorite(s, u)));
        return "redirect:/stories/" + id;
    }

    @PostMapping("/stories/{id}/rate")
    public String rate(@PathVariable Long id, @RequestParam int stars, HttpSession session) {
        User u = currentUser.get(session); Story s = stories.findById(id).orElse(null);
        if (u == null) return "redirect:/login";
        if (s == null || stars < 1 || stars > 5) return "redirect:/";
        Rating r = ratings.findByStoryAndUser(s, u).orElse(new Rating(s, u, stars));
        r.setStars(stars);                      // new rating or update the old one
        ratings.save(r);
        return "redirect:/stories/" + id;
    }

    @PostMapping("/stories/{id}/comment")
    public String comment(@PathVariable Long id, @RequestParam String text, HttpSession session) {
        User u = currentUser.get(session); Story s = stories.findById(id).orElse(null);
        if (u == null) return "redirect:/login";
        if (s != null && !text.isBlank()) comments.save(new Comment(s, u, text.trim()));
        return "redirect:/stories/" + id + "#comments";
    }
}
