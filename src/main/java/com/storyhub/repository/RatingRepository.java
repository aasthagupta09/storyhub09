package com.storyhub.repository;

import com.storyhub.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByStoryAndUser(Story story, User user);
    long countByStory(Story story);

    @Query("select avg(r.stars) from Rating r where r.story = :story")
    Double average(@Param("story") Story story);   // null when nobody has rated yet
}
