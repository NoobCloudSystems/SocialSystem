package xyz.luccboy.socialsystem.api;

import lombok.Getter;
import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.proxy.Player;
import xyz.luccboy.socialsystem.api.data.Settings;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;
import xyz.luccboy.socialsystem.api.managers.PlayerManager;

import java.sql.SQLException;
import java.util.*;

public class AbstractPlayerManager implements PlayerManager {

    @Getter
    private final Map<Player, SocialPlayer> players = new HashMap<>();

    public SocialPlayer registerPlayer(final Player player) {
        final SocialPlayer socialPlayer = new AbstractSocialPlayer(player, getSettings(player.getUniqueId()), getFriends(player.getUniqueId()), getRequests(player.getUniqueId()));
        this.players.put(player, socialPlayer);
        return socialPlayer;
    }

    public void removePlayer(final Player player) {
        this.players.remove(player);
    }

    @Override
    public SocialPlayer getSocialPlayer(final Player player) {
        return this.players.get(player);
    }

    private Settings getSettings(final UUID uuid) {
        return SocialPlugin.getInstance().getDatabaseManager().executeQuery(resultSet -> {
            try {
                while (resultSet.next()) {
                    return new Settings(resultSet.getBoolean("friend_requests"), resultSet.getBoolean("party_requests"), resultSet.getBoolean("jump"));
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
            return new Settings(true, true, true);
        }, new Settings(true, true, true), "SELECT friend_requests, party_requests, jump FROM settings WHERE uuid=?", uuid.toString()).join();
    }

    private List<UUID> getFriends(final UUID uuid) {
        return SocialPlugin.getInstance().getDatabaseManager().executeQuery(resultSet -> {
            final List<UUID> friends = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    final UUID friendUUID = UUID.fromString(resultSet.getString("friend"));
                    friends.add(friendUUID);
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
            return friends;
        }, Collections.<UUID>emptyList(), "SELECT friend FROM friends WHERE uuid=?", uuid.toString()).join();
    }

    private List<UUID> getRequests(final UUID uuid) {
        return SocialPlugin.getInstance().getDatabaseManager().executeQuery(resultSet -> {
            final List<UUID> requests = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    final UUID requesterUUID = UUID.fromString(resultSet.getString("requester"));
                    requests.add(requesterUUID);
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
            return requests;
        }, Collections.<UUID>emptyList(), "SELECT requester FROM friend_requests WHERE uuid=?", uuid.toString()).join();
    }

}
