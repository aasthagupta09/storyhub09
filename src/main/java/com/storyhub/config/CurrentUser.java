package com.storyhub.config;

import com.storyhub.model.User;
import com.storyhub.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Tiny helper: "who is logged in?" Login state = a userId stored in the HTTP session. */
@Component @RequiredArgsConstructor
public class CurrentUser {
    private final UserRepository users;

    /** Returns the logged-in user, or null if nobody is logged in. */
    public User get(HttpSession session) {
        Object id = session.getAttribute("userId");
        return id == null ? null : users.findById((Long) id).orElse(null);
    }
}
