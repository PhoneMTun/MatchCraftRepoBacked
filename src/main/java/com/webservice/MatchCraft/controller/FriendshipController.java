package com.webservice.MatchCraft.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webservice.MatchCraft.dto.PendingFriendRequestDto;
import com.webservice.MatchCraft.dto.UserResponseDto;
import com.webservice.MatchCraft.model.Friendship;
import com.webservice.MatchCraft.model.User;
import com.webservice.MatchCraft.service.FriendshipService;

@RestController
@RequestMapping("/api")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/friends/{userId}/{friendId}")
    public ResponseEntity<?> createFriendship(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            Friendship friendship = friendshipService.sendFriendRequest(userId, friendId);
            return ResponseEntity.ok().body("Friend request sent successfully from user " + userId + " to user " + friendId + ".");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/friends/accept/{requestId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId) {
        try {
            Friendship friendship = friendshipService.acceptFriendRequest(requestId);
            return ResponseEntity.ok().body("Friend request accepted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/friends/reject/{requestId}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Long requestId) {
        try {
            friendshipService.rejectFriendRequest(requestId);
            return ResponseEntity.ok().body("Friend request rejected successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<UserResponseDto>> getUserFriends(@PathVariable Long userId) {
        try {
            List<User> friends = friendshipService.findAllFriendsByUserId(userId);
            List<UserResponseDto> friendDtos = friends.stream()
                    .map(friend -> new UserResponseDto(
                            friend.getId(),
                            friend.getName(),
                            friend.getUserName(),
                            friend.getEmail(),
                            friend.getSteamId(),
                            friend.getCreatedAt(),
                            friend.getUpdatedAt())
                    )
                    .collect(Collectors.toList());
            return ResponseEntity.ok(friendDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
//    @GetMapping("/friends/pending/{userId}")
//    public ResponseEntity<List<UserResponseDto>> getPendingFriendRequests(@PathVariable Long userId) {
//        try {
//            // Fetching the list of users who have sent a pending friend request to the user.
//            List<UserResponseDto> pendingRequests = friendshipService.findPendingFriendRequests(userId)
//                    .stream()
//                    .map(user -> new UserResponseDto(
//                            user.getId(),
//                            user.getName(),
//                            user.getUserName(),
//                            user.getEmail(),
//                            user.getSteamId(),
//                            user.getCreatedAt(),
//                            user.getUpdatedAt()))
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(pendingRequests);
//        } catch (RuntimeException e) {
//            // Handling exceptions and returning a bad request response in case of failure.
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
    @GetMapping("/friends/pending/{userId}")
    public ResponseEntity<List<PendingFriendRequestDto>> getPendingFriendRequests(@PathVariable Long userId) {
        try {
            List<PendingFriendRequestDto> pendingRequests = friendshipService.findPendingFriendRequests(userId);
            if (pendingRequests.isEmpty()) {
                // If you want to return a not found status when there are no pending requests
                // return ResponseEntity.notFound().build();
                
                // Or, simply return an empty list with an OK status
                return ResponseEntity.ok(pendingRequests);
            }
            return ResponseEntity.ok(pendingRequests);
        } catch (RuntimeException e) {
            // Log the error or handle it as appropriate
            return ResponseEntity.badRequest().body(null);
        }
    }
    
}
