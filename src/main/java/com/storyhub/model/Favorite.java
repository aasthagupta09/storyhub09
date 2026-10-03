package com.storyhub.model;

import jakarta.persistence.*;
import lombok.*;

/** A story saved to a user's bookmarks. Table: favorites */
@Entity @Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = {"story_id", "user_id"}))
@Getter @Setter @NoArgsConstructor
public class Favorite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private User user;

    public Favorite(Story story, User user) { this.story = story; this.user = user; }
}
