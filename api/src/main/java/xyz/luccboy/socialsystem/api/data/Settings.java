package xyz.luccboy.socialsystem.api.data;

/**
 * Represents the settings of a player
 * @param friendRequests The boolean indicating whether the player wants to receive friend requests
 * @param partyRequests The boolean indicating whether the player wants to receive party requests
 * @param jump The boolean indicating whether the friends of a player should be allowed to jump to the player's currently connected server
 */
public record Settings(boolean friendRequests, boolean partyRequests, boolean jump) {}