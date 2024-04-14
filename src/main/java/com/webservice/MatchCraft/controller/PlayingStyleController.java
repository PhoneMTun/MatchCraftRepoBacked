package com.webservice.MatchCraft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webservice.MatchCraft.model.PlayingStyle;
import com.webservice.MatchCraft.service.PlayingStyleService;

@RestController
@RequestMapping("/api/playingStyles")
public class PlayingStyleController {

    @Autowired
    private PlayingStyleService playingStyleService;

    @GetMapping
    public ResponseEntity<List<PlayingStyle>> getAllPlayingStyles() {
        List<PlayingStyle> playingStyles = playingStyleService.findAll();
        return ResponseEntity.ok(playingStyles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayingStyle> getPlayingStyleById(@PathVariable Integer id) {
        return playingStyleService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/user/search/{userId}")
    public ResponseEntity<PlayingStyle> getPlayingStyleByUserId(@PathVariable Long userId) {
        return playingStyleService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createOrUpdatePlayingStyleForUser(@PathVariable Long userId, @RequestBody PlayingStyle playingStyleRequest) {
        try {
            PlayingStyle updatedPlayingStyle = playingStyleService.createOrUpdatePlayingStyleForUser(userId, playingStyleRequest);
            return ResponseEntity.ok().body("Playing style updated successfully");
        } catch (RuntimeException e) { // Using RuntimeException to catch not found users, you can create more specific exceptions if needed
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
