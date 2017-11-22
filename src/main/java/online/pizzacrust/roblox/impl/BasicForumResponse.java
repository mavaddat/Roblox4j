package online.pizzacrust.roblox.impl;

import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.forum.ForumResponse;

/**
 * note: immutable, does not refresh.
 */
public class BasicForumResponse implements ForumResponse {

    private final String username;
    private final String message;

    public BasicForumResponse(String robloxian, String message) {
        this.username = robloxian;
        this.message = message;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
