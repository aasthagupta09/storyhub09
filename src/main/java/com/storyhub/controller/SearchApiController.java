package com.storyhub.controller;

import com.storyhub.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** JSON endpoint behind the live-search dropdown. JavaScript calls GET /api/search?q=... as you type. */
@RestController @RequiredArgsConstructor
public class SearchApiController {
    private final StoryRepository stories;

    public record Hit(Long id, String title, String author, String category) {}

    @GetMapping("/api/search")
    public List<Hit> search(@RequestParam(defaultValue = "") String q) {
        if (q.isBlank()) return List.of();
        return stories.search(q.trim()).stream().limit(6)
                .map(s -> new Hit(s.getId(), s.getTitle(), s.getAuthor().getUsername(), s.getCategory()))
                .toList();
    }
}
