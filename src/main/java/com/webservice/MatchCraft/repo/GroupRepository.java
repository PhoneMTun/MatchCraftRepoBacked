package com.webservice.MatchCraft.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webservice.MatchCraft.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
    // Custom queries can be added here
}
