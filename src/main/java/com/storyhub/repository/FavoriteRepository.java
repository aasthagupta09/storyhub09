package com.storyhub.repository;

import com.storyhub.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserOrderByIdDesc(User user);
    Optional<Favorite> findByStoryAndUser(Story story, User user);
}
