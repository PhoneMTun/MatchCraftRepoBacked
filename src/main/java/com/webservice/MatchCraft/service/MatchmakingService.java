package com.webservice.MatchCraft.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservice.MatchCraft.dto.UserResponseDto;
import com.webservice.MatchCraft.model.PlayingStyle;
import com.webservice.MatchCraft.model.User;
import com.webservice.MatchCraft.repo.PlayingStyleRepository;

@Service
public class MatchmakingService {

    @Autowired
    private PlayingStyleRepository playingStyleRepo;

    public List<UserResponseDto> findMatches(Long userId) {
        // Fetch the PlayingStyle of the current user
        PlayingStyle currentUserStyle = playingStyleRepo.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Playing style for the current user not found"));

        // Fetch all PlayingStyles excluding the current user's style and calculate matches
        return playingStyleRepo.findAllExcludeUser(userId.intValue()).stream()
            .map(candidateStyle -> new MatchScore(candidateStyle.getUser(), calculateSimilarity(currentUserStyle, candidateStyle)))
            .sorted(Comparator.comparing(MatchScore::getScore).reversed())
            .map(matchScore -> convertToDto(matchScore.getUser()))
            .collect(Collectors.toList());
    }

    // Helper method to convert User to UserResponseDto
    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(
            user.getId().intValue(),  // Convert Long to Integer if necessary
            user.getName(),
            user.getUserName(),
            user.getEmail(),
            user.getSteamId(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    // Calculate similarity based on PlayingStyle attributes
    private double calculateSimilarity(PlayingStyle style1, PlayingStyle style2) {
        double score = 0.0;
        if (style1.getRole().equalsIgnoreCase(style2.getRole())) score += 1.0;
        if (style1.getStrategyPreference().equalsIgnoreCase(style2.getStrategyPreference())) score += 1.0;
        if (style1.getCommunicationStyle().equalsIgnoreCase(style2.getCommunicationStyle())) score += 1.0;
        if (style1.getPreferredGameModes().equalsIgnoreCase(style2.getPreferredGameModes())) score += 1.0;
        if (style1.getSkillLevel().equalsIgnoreCase(style2.getSkillLevel())) score += 1.0;
        if (style1.getPlaytimeAvailability().equalsIgnoreCase(style2.getPlaytimeAvailability())) score += 1.0;
        if (style1.getGamingGoals().equalsIgnoreCase(style2.getGamingGoals())) score += 1.0;
        if (style1.getLanguagePreferences().equalsIgnoreCase(style2.getLanguagePreferences())) score += 1.0;
        return score;
    }

    // Inner class to hold match scores
    static class MatchScore {
        private User user;
        private double score;

        public MatchScore(User user, double score) {
            this.user = user;
            this.score = score;
        }

        public User getUser() {
            return user;
        }

        public double getScore() {
            return score;
        }
    }
}
