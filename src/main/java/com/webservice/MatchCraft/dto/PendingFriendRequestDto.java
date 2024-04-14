package com.webservice.MatchCraft.dto;

public class PendingFriendRequestDto {
    private Integer userId;
    private String userName; // And any other user fields you need
    private Long friendshipId; // ID of the friendship (friend request)

    // Constructor
    public PendingFriendRequestDto(Integer userId, String userName, Long friendshipId) {
        this.userId = userId;
        this.userName = userName;
        this.friendshipId = friendshipId;
    }

    // Getters and Setters
    

    public String getUserName() {
        return userName;
    }

    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(Long friendshipId) {
        this.friendshipId = friendshipId;
    }
}
