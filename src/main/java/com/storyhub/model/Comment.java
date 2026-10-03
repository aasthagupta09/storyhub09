package com.storyhub.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/** A reader's comment on a story. Table: comments */
@Entity @Table(name = "comments")
@Getter @Setter @NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 1000)
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Comment(Story story, User user, String text) {
        this.story = story; this.user = user; this.text = text;
    }
}
