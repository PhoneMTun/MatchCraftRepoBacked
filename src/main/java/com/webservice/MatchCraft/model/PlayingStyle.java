package com.webservice.MatchCraft.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "playing_styles")
public class PlayingStyle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role")
    private String role;

    @Column(name = "strategy_preference")
    private String strategyPreference;

    @Column(name = "preferred_heroes")
    private String preferredHeroes;
    private String communicationStyle;
    private String preferredGameModes;
    private String skillLevel;
    private String playtimeAvailability;
    private String gamingGoals;
    private String languagePreferences;
    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;
    
    

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;



    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStrategyPreference() {
        return strategyPreference;
    }

    public void setStrategyPreference(String strategyPreference) {
        this.strategyPreference = strategyPreference;
    }

    public String getPreferredHeroes() {
        return preferredHeroes;
    }

    public void setPreferredHeroes(String preferredHeroes) {
        this.preferredHeroes = preferredHeroes;
    }
    
    
    
    public String getCommunicationStyle() {
		return communicationStyle;
	}

	public void setCommunicationStyle(String communicationStyle) {
		this.communicationStyle = communicationStyle;
	}

	public String getPreferredGameModes() {
		return preferredGameModes;
	}

	public void setPreferredGameModes(String preferredGameModes) {
		this.preferredGameModes = preferredGameModes;
	}

	public String getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}

	public String getPlaytimeAvailability() {
		return playtimeAvailability;
	}

	public void setPlaytimeAvailability(String playtimeAvailability) {
		this.playtimeAvailability = playtimeAvailability;
	}

	public String getGamingGoals() {
		return gamingGoals;
	}

	public void setGamingGoals(String gamingGoals) {
		this.gamingGoals = gamingGoals;
	}

	public String getLanguagePreferences() {
		return languagePreferences;
	}

	public void setLanguagePreferences(String languagePreferences) {
		this.languagePreferences = languagePreferences;
	}

	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
    
    
    
}
