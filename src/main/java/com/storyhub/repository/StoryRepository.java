package com.storyhub.repository;

import com.storyhub.model.Story;
import com.storyhub.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findAllByOrderByCreatedAtDesc();
    List<Story> findByCategoryOrderByCreatedAtDesc(String category);
    List<Story> findByAuthorOrderByCreatedAtDesc(User author);

    /** Search by story title OR author username (case-insensitive, partial match). */
    @Query("select s from Story s where lower(s.title) like lower(concat('%', :q, '%')) " +
           "or lower(s.author.username) like lower(concat('%', :q, '%')) order by s.createdAt desc")
    List<Story> search(@Param("q") String q);

    /** "Featured" = most liked first. Pass PageRequest.of(0, 3) for the top three. */
    @Query("select s from Story s order by " +
           "(select count(l) from StoryLike l where l.story = s) desc, s.createdAt desc")
    List<Story> findFeatured(Pageable pageable);
}
