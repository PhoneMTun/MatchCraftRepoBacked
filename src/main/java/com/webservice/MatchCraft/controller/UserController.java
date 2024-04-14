package com.webservice.MatchCraft.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webservice.MatchCraft.dto.LoginDto;
import com.webservice.MatchCraft.dto.SearchCriteriaDto;
import com.webservice.MatchCraft.dto.SignUpDto;
import com.webservice.MatchCraft.dto.UserResponseDto;
import com.webservice.MatchCraft.model.PlayingStyle;
import com.webservice.MatchCraft.model.Role;
import com.webservice.MatchCraft.model.Skills;
import com.webservice.MatchCraft.model.User;
import com.webservice.MatchCraft.repo.RoleRepo;
import com.webservice.MatchCraft.repo.UserRepo;
import com.webservice.MatchCraft.service.FriendshipService;
import com.webservice.MatchCraft.service.JwtService;
import com.webservice.MatchCraft.service.PlayingStyleService;
import com.webservice.MatchCraft.service.listener.WebSocketEventListener;

import jakarta.servlet.http.HttpServletResponse;
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private WebSocketEventListener webSocketEventListener;
    @Autowired
    private PlayingStyleService playingStyleService;


    
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> authenticateUser(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUserName(loginDto.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + loginDto.getUsername()));

            final String jwtToken = jwtService.generateToken(user.getUserName());

            // Correctly set the JWT token in a cookie
            ResponseCookie jwtCookie = ResponseCookie.from("token", jwtToken)
                    .httpOnly(true)
                    .secure(true) // Should be set to true in production for HTTPS
                    .path("/")
                    .maxAge(jwtService.getExpirationTime()) 
                    .build();
            response.addHeader("Set-Cookie", jwtCookie.toString());

            // Prepare the user response
            UserResponseDto userResponse = new UserResponseDto(
                    user.getId(),
                    user.getName(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getSteamId(),
                    user.getCreatedAt(),
                    user.getUpdatedAt());

            // Return the UserResponseDto
            return ResponseEntity.ok(userResponse);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        System.out.print("searching user");
    	User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));


        // Constructing UserResponseDto with additional Skills information if necessary
        UserResponseDto userResponse = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getUserName(),
                user.getEmail(),
                user.getSteamId(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        return ResponseEntity.ok(userResponse);
    }
    

//    @PostMapping("/playingStyle/user/{userId}")
//    public ResponseEntity<?> createOrUpdatePlayingStyle(@PathVariable Long userId, @RequestBody PlayingStyle playingStyleRequest) {
//        try {
//            // Retrieve the user from the database
//            User user = userRepository.findById(userId).orElseThrow(() -> 
//                new UsernameNotFoundException("User Not Found with id: " + userId));
//            
//            // Check if the user already has a playing style
//            PlayingStyle playingStyle = user.getPlayingStyle();
//            if (playingStyle != null) {
//                // Update the existing playing style
//                playingStyle.setRole(playingStyleRequest.getRole());
//                playingStyle.setStrategyPreference(playingStyleRequest.getStrategyPreference());
//                playingStyle.setPreferredHeroes(playingStyleRequest.getPreferredHeroes());
//            } else {
//                // Set the new playing style to the user
//                playingStyle = playingStyleRequest;
//                playingStyle.setUser(user);
//            }
//
//            // Save the playing style
//            PlayingStyle savedPlayingStyle = playingStyleService.save(playingStyle);
//
//            // Optionally, update the user's playing style reference if necessary
//            if (user.getPlayingStyle() == null) {
//                user.setPlayingStyle(savedPlayingStyle);
//                userRepository.save(user);
//            }
//            System.out.print("testing");
//            // Return the saved playing style information
//            return ResponseEntity.ok(Collections.singletonMap("message", "Playing Style Set successfully!"));
//
//        } catch (UsernameNotFoundException ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", ex.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while processing your request."));
//        }
//    }


    @PostMapping("/search")
    public ResponseEntity<?> searchUserBySteamIdOrUsername(@RequestBody SearchCriteriaDto searchCriteria) {
        Optional<User> userOptional = Optional.empty();

        if (searchCriteria.getSteamId() != null) {
            userOptional = userRepository.findBySteamId(searchCriteria.getSteamId());
        } else if (searchCriteria.getUsername() != null) {
            userOptional = userRepository.findByUserName(searchCriteria.getUsername());
        }

        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        Skills skills = user.getSkills();
        String steamPicUrl = skills != null ? skills.getSteam_pic() : null;

        // Instead of modifying UserResponseDto, create a Map to hold the response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", user.getId());
        responseData.put("name", user.getName());
        responseData.put("userName", user.getUserName());
        responseData.put("email", user.getEmail());
        responseData.put("steamId", user.getSteamId());
        responseData.put("createdAt", user.getCreatedAt());
        responseData.put("updatedAt", user.getUpdatedAt());
        responseData.put("steamPic", steamPicUrl); // Add steamPic to the response map

        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/user/isOnline/{userId}")
    public ResponseEntity<?> isUserOnline(@PathVariable Integer userId) {
        boolean isOnline = webSocketEventListener.isUserOnline(userId);
        return ResponseEntity.ok(Map.of("isOnline", isOnline));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        // checking for username exists in a database
    	if (userRepository.existsByUserName(signUpDto.getUsername())) {
    	    return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Username is already exist!"));
    	}

    	if (userRepository.existsByEmail(signUpDto.getEmail())) {
    	    return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email is already exist!"));
    	}

    	if (userRepository.existsBySteamId(signUpDto.getSteamId())) {
    	    return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with this steam id exist!"));
    	}
        
        // creating user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUserName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setSteamId(signUpDto.getSteamId());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // Set the user role to ROLE_USER instead of ROLE_ADMIN
        Role roles = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
            new RuntimeException("Error: Role is not found.")
        );
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }
    
    
    @PostMapping("/generateToken") 
    public String authenticateAndGetToken(@RequestBody LoginDto loginDto) { 
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())); 
        if (authentication.isAuthenticated()) { 
            return jwtService.generateToken(loginDto.getUsername()); 
        } else { 
            throw new UsernameNotFoundException("invalid user request !"); 
        } 
    } 

}