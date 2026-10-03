package com.storyhub.repository;

import com.storyhub.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<StoryLike, Long> {
    long countByStory(Story story);
    Optional<StoryLike> findByStoryAndUser(Story story, User user);
}
