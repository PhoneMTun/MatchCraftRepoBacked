package com.webservice.MatchCraft.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.webservice.MatchCraft.model.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	User findByUserNameOrEmail(String userName, String email);
	Optional<User> findById(Long id);
	Optional<User> findByUserName(String username);
	Optional<User> findBySteamId(Long steamId);

	boolean existsByUserName(String username);
	boolean existsByEmail(String email);
	boolean existsBySteamId(Long steamId);



}