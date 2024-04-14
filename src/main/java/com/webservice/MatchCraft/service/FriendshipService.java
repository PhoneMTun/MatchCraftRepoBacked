package com.webservice.MatchCraft.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservice.MatchCraft.dto.PendingFriendRequestDto;
import com.webservice.MatchCraft.model.Friendship;
import com.webservice.MatchCraft.model.User;
import com.webservice.MatchCraft.repo.FriendshipRepo;
import com.webservice.MatchCraft.repo.UserRepo;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepo friendshipRepo;

    @Autowired
    private UserRepo userRepo;

    public Friendship sendFriendRequest(Long userId, Long friendId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        User friend = userRepo.findById(friendId).orElseThrow(() -> new RuntimeException("Friend not found with id: " + friendId));

        if (!friendshipRepo.existsByUserAndFriend(user, friend) && !friendshipRepo.existsByUserAndFriend(friend, user)) {
            Friendship friendship = new Friendship();
            friendship.setUser(user);
            friendship.setFriend(friend);
            friendship.setFriendshipStatus("PENDING");
            return friendshipRepo.save(friendship);
        } else {
            throw new RuntimeException("Friendship request already exists or users are already friends.");
        }
    }

    public Friendship acceptFriendRequest(Long requestId) {
        Friendship friendship = friendshipRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friendship request not found with id: " + requestId));
        friendship.setFriendshipStatus("ACCEPTED");
        return friendshipRepo.save(friendship);
    }
    
    public void rejectFriendRequest(Long requestId) {
        Friendship friendship = friendshipRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friendship request not found with id: " + requestId));
        friendshipRepo.delete(friendship);
    }


    // Implement rejectFriendRequest, blockUser, etc., based on your application needs.

    public List<User> findAllFriendsByUserId(Long userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Find all friendships where the user is the 'user'
        List<Friendship> friendshipsAsUser = friendshipRepo.findByUser(user);

        // Find all friendships where the user is the 'friend'
        List<Friendship> friendshipsAsFriend = friendshipRepo.findByFriend(user);

        // Prepare a list to hold all unique friends
        Set<User> friends = new HashSet<>();

        // Add all friends from the first list (where our user is the 'user')
        for (Friendship friendship : friendshipsAsUser) {
            friends.add(friendship.getFriend());
        }

        // Add all friends from the second list (where our user is the 'friend')
        for (Friendship friendship : friendshipsAsFriend) {
            friends.add(friendship.getUser());
        }

        // Convert the set back to a list to return
        return new ArrayList<>(friends);
    }

    
    public List<PendingFriendRequestDto> findPendingFriendRequests(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Friendship> pendingRequests = friendshipRepo.findByFriendAndFriendshipStatus(user, "PENDING");

        return pendingRequests.stream()
                .map(friendship -> new PendingFriendRequestDto(
                    friendship.getUser().getId(),
                    friendship.getUser().getUserName(), // Assume there's a getUserName, adjust as per your User entity
                    friendship.getId()))
                .collect(Collectors.toList());
    }
    
    
}
