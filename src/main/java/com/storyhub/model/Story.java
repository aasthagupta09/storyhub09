package com.storyhub.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/** A published story. Table: stories (many stories -> one author). */
@Entity @Table(name = "stories")
@Getter @Setter @NoArgsConstructor
public class Story {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 30)
    private String category;

    @Lob @Column(nullable = false)
    private String content;               // plain text + light Markdown (# heading, **bold**, *italic*)

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Deleting a story also deletes its child rows (avoids foreign-key errors).
    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE) private List<Comment> comments;
    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE) private List<StoryLike> likes;
    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE) private List<Favorite> favorites;
    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE) private List<Rating> ratings;

    /** Short preview for cards (Markdown symbols removed). */
    public String getExcerpt() {
        String plain = content.replaceAll("[#*]", "").replaceAll("\\s+", " ").trim();
        return plain.length() > 150 ? plain.substring(0, 150) + "…" : plain;
    }

    /** Average adult reads ~200 words per minute. */
    public int getReadingMinutes() {
        return Math.max(1, content.trim().split("\\s+").length / 200);
    }
}
