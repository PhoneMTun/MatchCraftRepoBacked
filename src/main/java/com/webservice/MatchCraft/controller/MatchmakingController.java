package com.webservice.MatchCraft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webservice.MatchCraft.dto.UserResponseDto;
import com.webservice.MatchCraft.service.MatchmakingService;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    @Autowired
    private MatchmakingService matchmakingService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserResponseDto>> findMatchesForUser(@PathVariable Long userId) {
        List<UserResponseDto> matches = matchmakingService.findMatches(userId);
        if (matches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(matches);
    }

}

