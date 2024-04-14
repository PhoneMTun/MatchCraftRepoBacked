package com.webservice.MatchCraft.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webservice.MatchCraft.model.Group;
import com.webservice.MatchCraft.model.User;
import com.webservice.MatchCraft.repo.GroupRepository;
import com.webservice.MatchCraft.repo.UserRepo;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepo userRepository;

    public Group createOrUpdateGroup(Group group) {
        // This method seems to just save the group, consider renaming if it doesn't handle updates specifically
        return groupRepository.save(group);
    }
    public Group createGroup(Group group, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        group.getMembers().add(user);

        return groupRepository.save(group);
    }


    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }
    
    public void addUserToGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Group group = findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));

        // Check if the user is already a member of the group
        if (group.getMembers().contains(user)) {
            throw new RuntimeException("User is already a member of this group");
        }

        group.getMembers().add(user);
        groupRepository.save(group);
    }
    public void removeUserFromGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Group group = findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getMembers().contains(user)) {
            throw new RuntimeException("User is not a member of this group");
        }

        group.getMembers().remove(user);
        groupRepository.save(group);
    }

    
    public Group createGroup(Group group, String username) {
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        group.getMembers().add(user);
        // Save group with its members
        Group savedGroup = groupRepository.save(group);
        // Optionally, if you want to explicitly link the user back to the group
        user.getGroups().add(savedGroup);
        userRepository.save(user);

        return savedGroup;
    }
}
