package com.webservice.MatchCraft.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webservice.MatchCraft.model.User;
import com.webservice.MatchCraft.repo.UserRepo;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {
	@Autowired    
	UserRepo userRepo;
	
    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {
        User user = userRepo.findByUserNameOrEmail(username, username);
        if(user==null){
            throw new UsernameNotFoundException("User not exists by Username");
        }
           
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }
    
    public Integer getUserIdByUsername(String username) {
        return userRepo.findByUserName(username)
                             .map(User::getId)
                             .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    
    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    
    public void setUserOnlineStatus(Integer userId, Boolean isOnline) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        user.setIsOnline(isOnline);
        userRepo.save(user);
    }


    
    
}