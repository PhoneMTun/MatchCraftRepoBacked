package com.webservice.MatchCraft.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webservice.MatchCraft.model.PlayingStyle;
import com.webservice.MatchCraft.model.User;
import com.webservice.MatchCraft.repo.PlayingStyleRepository;
import com.webservice.MatchCraft.repo.UserRepo;

@Service
public class PlayingStyleService {

    @Autowired
    private PlayingStyleRepository playingStyleRepository;

    @Autowired
    private UserRepo userRepository;

    public List<PlayingStyle> findAll() {
        return playingStyleRepository.findAll();
    }

    public Optional<PlayingStyle> findById(Integer id) {
        return playingStyleRepository.findById(id);
    }

    public Optional<PlayingStyle> findByUserId(Long userId) {
        return playingStyleRepository.findByUserId(userId);
    }

    @Transactional
    public PlayingStyle createOrUpdatePlayingStyleForUser(Long userId, PlayingStyle playingStyleData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        PlayingStyle playingStyle = findByUserId(userId).orElse(new PlayingStyle());

        playingStyle.setRole(playingStyleData.getRole());
        playingStyle.setStrategyPreference(playingStyleData.getStrategyPreference());
        playingStyle.setPreferredHeroes(playingStyleData.getPreferredHeroes());
        playingStyle.setCommunicationStyle(playingStyleData.getCommunicationStyle());
        playingStyle.setPreferredGameModes(playingStyleData.getPreferredGameModes());
        playingStyle.setSkillLevel(playingStyleData.getSkillLevel());
        playingStyle.setPlaytimeAvailability(playingStyleData.getPlaytimeAvailability());
        playingStyle.setGamingGoals(playingStyleData.getGamingGoals());
        playingStyle.setLanguagePreferences(playingStyleData.getLanguagePreferences());
        playingStyle.setUser(user);

        return playingStyleRepository.save(playingStyle);
    }

    // Other methods...
}

