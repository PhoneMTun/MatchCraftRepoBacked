package com.webservice.MatchCraft.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webservice.MatchCraft.model.Group;
import com.webservice.MatchCraft.service.GroupService;
import com.webservice.MatchCraft.service.UserService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    
    @Autowired
    private UserService userService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createGroup(@PathVariable Long userId, @RequestBody Group group) {
        try {
            Group savedGroup = groupService.createGroup(group, userId);
            return ResponseEntity.ok(savedGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }
    
    @PostMapping("/{groupId}/join/{userId}")
    public ResponseEntity<?> joinGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        try {
            groupService.addUserToGroup(userId, groupId);
            // Return a success message indicating the user has been added to the group
            return ResponseEntity.ok("User with ID " + userId + " added to the group with ID " + groupId + " successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
    
    @PostMapping("/{groupId}/leave/{userId}")
    public ResponseEntity<?> leaveGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        try {
            groupService.removeUserFromGroup(userId, groupId);
            return ResponseEntity.ok("User with ID " + userId + " removed from the group with ID " + groupId + " successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }


}
