package com.storyhub.repository;

import com.storyhub.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStoryOrderByCreatedAtDesc(Story story);
}
