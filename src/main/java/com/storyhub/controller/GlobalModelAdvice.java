package com.storyhub.controller;

import com.storyhub.config.CurrentUser;
import com.storyhub.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Adds data every page needs (navbar user + category list) to every template automatically. */
@ControllerAdvice @RequiredArgsConstructor
public class GlobalModelAdvice {
    private final CurrentUser currentUser;

    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) { return currentUser.get(session); }

    @ModelAttribute("categories")
    public List<String> categories() {
        return List.of("Sci-Fi", "Mystery", "Romance", "Fantasy", "Horror", "Adventure");
    }
}
