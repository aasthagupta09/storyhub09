package com.storyhub.config;

import com.storyhub.model.*;
import com.storyhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/** Inserts a demo user and a few stories on first run, so the app is not empty during the demo. */
@Component @RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final StoryRepository stories;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;

        User demo = new User();
        demo.setUsername("demo");
        demo.setEmail("demo@storyhub.com");
        demo.setPasswordHash(encoder.encode("demo123"));
        demo.setBio("Demo account. Writes about stars, secrets and strange weather.");
        users.save(demo);

        add(demo, "The Last Lighthouse on Mars", "Sci-Fi",
            "# Chapter One\n\nThe beacon had been blinking for **eleven years** before anyone noticed it was not ours.\n\n"
          + "Mira climbed the rusted ladder with a flask of tea and a lot of questions. Below her, the red dust of "
          + "Arcadia Planitia rolled out like a sea nobody had ever sailed.\n\n*Someone was signalling back.*");
        add(demo, "Quiet Rooms", "Mystery",
            "Every night at 2:14 the piano in the empty ballroom played one wrong note.\n\n"
          + "Inspector Hale had stopped believing in ghosts years ago. He had **not** stopped believing in people "
          + "who wanted a hotel to look haunted.");
        add(demo, "Letters from the Monsoon", "Romance",
            "The first letter arrived with the rains. The second, a week later, smelled faintly of cardamom.\n\n"
          + "By the fifth, Anaya had stopped pretending she was only replying out of politeness.");
        add(demo, "The Dragon's Apprentice", "Fantasy",
            "# The Smallest Egg\n\nNobody wanted the smallest egg in the clutch, which is exactly why Tobin took it.\n\n"
          + "It hatched during a thunderstorm, sneezed once, and set his boots on fire.");
    }

    private void add(User author, String title, String category, String content) {
        Story s = new Story();
        s.setTitle(title); s.setCategory(category); s.setContent(content); s.setAuthor(author);
        stories.save(s);
    }
}
