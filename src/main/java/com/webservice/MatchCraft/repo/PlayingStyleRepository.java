package com.webservice.MatchCraft.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.webservice.MatchCraft.model.PlayingStyle;

@Repository
public interface PlayingStyleRepository extends JpaRepository<PlayingStyle, Integer> {
	Optional<PlayingStyle> findByUserId(Long userId);
	@Query("SELECT ps FROM PlayingStyle ps JOIN FETCH ps.user WHERE ps.user.id != :userId")
    List<PlayingStyle> findAllExcludeUser(Integer userId);
}
