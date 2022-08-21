package xyz.luccboy.socialsystem.api.data;

import com.velocitypowered.api.proxy.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a player
 */
public interface SocialPlayer {
    /**
     * Gets the Velocity player-object
     * @return The {@link Player}
     */
    Player getPlayer();

    /**
     * Gets the player's settings
     * @return The player's {@link Settings}
     */
    Settings getSettings();

    /**
     * Gets the player's friends
     * @return A list containing all {@link UUID uuids} of the player's friends
     */
    List<UUID> getFriends();

    /**
     * Gets the player's friend requests
     * @return A list containing all {@link UUID uuids} of the requesters
     */
    List<UUID> getFriendRequests();

    /**
     * Gets the player's last chat partner
     * @return An {@link Optional} with the player's last {@link Player chat partner}
     */
    Optional<Player> getLastChatPartner();

    /**
     * Sets the player's last chat partner
     * @param player The new {@link Player chat partner}
     */
    void setLastChatPartner(Player player);

    /**
     * Gets the player's party
     * @return An {@link Optional} with the player's {@link Party}
     */
    Optional<Party> getParty();

    /**
     * Sets the player's party
     * @param party The new {@link Party}
     */
    void setParty(Party party);

    /**
     * Gets all party invites
     * @return A map containing the {@link Player requester} and the {@link Party}
     */
    Map<Player, Party> getPartyInvites();

    /**
     * Gets the player's friends who are online
     * @return A list containing all online {@link Player friends}
     */
    List<Player> getOnlineFriends();

    /**
     * Adds a friend
     * @param uuid The new friend
     */
    void addFriend(UUID uuid);

    /**
     * Removes a friend
     * @param uuid The friend's {@link UUID}
     */
    void removeFriend(UUID uuid);

    /**
     * Adss a friend request
     * @param requester The {@link UUID} of the requester
     */
    void addFriendRequest(UUID requester);

    /**
     * Sends a friend request to a player (without a message, only in the database)
     * @param target The {@link UUID} of the new friend
     */
    void sendFriendRequest(UUID target);

    /**
     * Accepts a friend request
     * @param requester The {@link UUID} of the requester
     */
    void acceptRequest(UUID requester);

    /**
     * Denies a friend request
     * @param requester The {@link UUID} of the requester
     */
    void denyRequest(UUID requester);
}
