package xyz.luccboy.socialsystem.api.data;

import com.velocitypowered.api.proxy.Player;

import java.util.List;

/**
 * Represents a party
 */
public interface Party {
    /**
     * Gets the leader of the party
     * @return The {@link Player Player-object} of the party leader
     */
    Player getLeader();

    /**
     * Gets the members of the party
     * @return A list containing all {@link Player members} of the party
     */
    List<Player> getMembers();

    /**
     * Gets all invited players of the party
     * @return A list containing all invited {@link Player players}
     */
    List<Player> getInvitedPlayers();

    /**
     * Sends a message to the whole party without a sender
     * @param message The message
     */
    void sendSystemMessage(String message);

    /**
     * Lets a {@param sender} send a {@param message} to the whole party
     * @param sender The sender of the message
     * @param message The message
     */
    void sendMessage(Player sender, String message);

    /**
     * Adds a player to the party
     * @param player The new member
     */
    void addMember(Player player);

    /**
     * Removes a player from the party
     * @param player The member to leave
     */
    void removeMember(Player player);

    /**
     * Sets the leader of a party
     * @param player The new party leader
     */
    void setLeader(Player player);
}
