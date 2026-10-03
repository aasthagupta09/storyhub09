package com.storyhub.model;

import jakarta.persistence.*;
import lombok.*;

/** One user liked one story. Table: likes (unique pair prevents double likes). */
@Entity @Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"story_id", "user_id"}))
@Getter @Setter @NoArgsConstructor
public class StoryLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private User user;

    public StoryLike(Story story, User user) { this.story = story; this.user = user; }
}
