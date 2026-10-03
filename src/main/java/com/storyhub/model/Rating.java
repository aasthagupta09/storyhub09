package com.storyhub.model;

import jakarta.persistence.*;
import lombok.*;

/** A 1-5 star rating. One rating per user per story (re-rating updates it). Table: ratings */
@Entity @Table(name = "ratings", uniqueConstraints = @UniqueConstraint(columnNames = {"story_id", "user_id"}))
@Getter @Setter @NoArgsConstructor
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int stars;

    public Rating(Story story, User user, int stars) { this.story = story; this.user = user; this.stars = stars; }
}
