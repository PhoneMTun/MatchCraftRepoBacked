package com.webservice.MatchCraft.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @NotEmpty(message="Username is required!")
    @Size(min=3, max =30, message="Username must be between 3 and 30 characters")
    @Column(nullable = false, unique = true)
    private String userName;

    @NotEmpty(message="Email is required!")
    @Email(message="Please enter a valid email!")
    @Column(nullable = false, unique = true)
    private String email;
    

    @Column(name = "steam_id", nullable = false, unique = true)
    private Long steamId;

    @NotEmpty(message="Password is required!")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Friendship> friendships;
    
    private Boolean isOnline;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Skills skills;
    
    
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private Set<Chat> chats = new HashSet<>();

    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getSteamId() {
		return steamId;
	}

	public void setSteamId(Long steamId) {
		this.steamId = steamId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Friendship> getFriendships() {
		return friendships;
	}

	public void setFriendships(Set<Friendship> friendships) {
		this.friendships = friendships;
	}

	public Skills getSkills() {
		return skills;
	}

	public void setSkills(Skills skills) {
		this.skills = skills;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Chat> getChats() {
		return chats;
	}

	public void setChats(Set<Chat> chats) {
		this.chats = chats;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	public Set<Group> getGroups() {
	    return groups;
	}

	public void setGroups(Set<Group> groups) {
	    this.groups = groups;
	}

	public void joinGroup(Group group) {
	    this.groups.add(group);
	    group.getMembers().add(this);
	}

	public void leaveGroup(Group group) {
	    this.groups.remove(group);
	    group.getMembers().remove(this);
	}
	public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

	

    
}
