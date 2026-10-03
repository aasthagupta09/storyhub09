package com.storyhub.controller;

import com.storyhub.config.CurrentUser;
import com.storyhub.model.User;
import com.storyhub.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** Register, login, logout, and view/edit profile. */
@Controller @RequiredArgsConstructor
public class AuthController {
    private final UserRepository users;
    private final StoryRepository stories;
    private final BCryptPasswordEncoder encoder;
    private final CurrentUser currentUser;

    // ---------- Register ----------
    @GetMapping("/register")
    public String registerForm() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String email,
                           @RequestParam String password, HttpSession session, Model model) {
        username = username.trim(); email = email.trim();
        String error = null;
        if (username.length() < 3)               error = "Username must be at least 3 characters.";
        else if (!email.contains("@"))           error = "Enter a valid email address.";
        else if (password.length() < 6)          error = "Password must be at least 6 characters.";
        else if (users.existsByUsername(username)) error = "That username is taken. Try another.";
        else if (users.existsByEmail(email))     error = "An account with this email already exists.";

        if (error != null) { model.addAttribute("error", error); return "register"; }

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));
        users.save(u);
        session.setAttribute("userId", u.getId());   // log the new user in straight away
        return "redirect:/";
    }

    // ---------- Login / Logout ----------
    @GetMapping("/login")
    public String loginForm() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String login, @RequestParam String password,
                        HttpSession session, Model model) {
        User u = users.findByUsernameOrEmail(login.trim(), login.trim()).orElse(null);
        if (u == null || !encoder.matches(password, u.getPasswordHash())) {
            model.addAttribute("error", "Username/email or password is incorrect.");
            return "login";
        }
        session.setAttribute("userId", u.getId());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/"; }

    // ---------- Profile ----------
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User u = currentUser.get(session);
        if (u == null) return "redirect:/login";
        model.addAttribute("myStories", stories.findByAuthorOrderByCreatedAtDesc(u));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String email, @RequestParam(required = false) String bio,
                                @RequestParam(required = false) String newPassword,
                                HttpSession session, Model model) {
        User u = currentUser.get(session);
        if (u == null) return "redirect:/login";

        boolean emailTaken = users.findByEmail(email.trim()).filter(o -> !o.getId().equals(u.getId())).isPresent();
        if (emailTaken || !email.contains("@")) {
            model.addAttribute("error", emailTaken ? "That email belongs to another account." : "Enter a valid email address.");
            model.addAttribute("myStories", stories.findByAuthorOrderByCreatedAtDesc(u));
            return "profile";
        }
        u.setEmail(email.trim());
        u.setBio(bio);
        if (newPassword != null && newPassword.length() >= 6) u.setPasswordHash(encoder.encode(newPassword));
        users.save(u);
        return "redirect:/profile?saved";
    }
}
