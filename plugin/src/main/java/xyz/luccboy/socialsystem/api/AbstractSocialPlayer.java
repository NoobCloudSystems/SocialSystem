package xyz.luccboy.socialsystem.api;

import lombok.Getter;
import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.proxy.Player;
import xyz.luccboy.socialsystem.api.data.Party;
import xyz.luccboy.socialsystem.api.data.Settings;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;

import java.util.*;

@Getter
public class AbstractSocialPlayer implements SocialPlayer {

    private final Player player;
    private final Settings settings;
    private final List<UUID> friends;
    private final List<UUID> friendRequests;
    private Optional<Player> lastChatPartner = Optional.empty();
    private Optional<Party> party = Optional.empty();
    private final Map<Player, Party> partyInvites = new HashMap<>();

    public AbstractSocialPlayer(final Player player, final Settings settings, final List<UUID> friends, final List<UUID> friendRequests) {
        this.player = player;
        this.settings = settings;
        this.friends = friends;
        this.friendRequests = friendRequests;
    }

    @Override
    public List<Player> getOnlineFriends() {
        final List<Player> onlineFriends = new ArrayList<>();
        this.friends.forEach(uuid -> SocialPlugin.getInstance().getServer().getPlayer(uuid).ifPresent(onlineFriends::add));
        return onlineFriends;
    }

    @Override
    public void addFriend(final UUID uuid) {
        this.friends.add(uuid);
        SocialPlugin.getInstance().getDatabaseManager().executeUpdate("INSERT INTO friends (uuid, friend) VALUES (?, ?)", this.player.getUniqueId().toString(), uuid.toString()).join();
    }

    @Override
    public void removeFriend(final UUID uuid) {
        if (this.friends.contains(uuid)) {
            this.friends.remove(uuid);
            SocialPlugin.getInstance().getDatabaseManager().executeUpdate("DELETE FROM friends WHERE friend = ?", uuid.toString()).join();
        }
    }

    @Override
    public void addFriendRequest(final UUID requester) {
        this.friendRequests.add(requester);
        SocialPlugin.getInstance().getDatabaseManager().executeUpdate("INSERT INTO friend_requests (requester, uuid) VALUES (?, ?)", requester.toString(), this.player.getUniqueId().toString()).join();
    }

    @Override
    public void sendFriendRequest(final UUID target) {
        SocialPlugin.getInstance().getDatabaseManager().executeUpdate("INSERT INTO friend_requests (requester, uuid) VALUES (?, ?)", this.player.getUniqueId().toString(), target.toString()).join();
    }

    @Override
    public void acceptRequest(final UUID requester) {
        if (this.friendRequests.contains(requester)) {
            SocialPlugin.getInstance().getDatabaseManager().executeUpdate("DELETE FROM friend_requests WHERE requester=? AND uuid=?", requester.toString(), this.player.getUniqueId().toString()).join();
            SocialPlugin.getInstance().getDatabaseManager().executeUpdate("INSERT INTO friends (uuid, friend) VALUES (?, ?)", this.player.getUniqueId().toString(), requester.toString()).join();
            SocialPlugin.getInstance().getDatabaseManager().executeUpdate("INSERT INTO friends (uuid, friend) VALUES (?, ?)", requester.toString(), this.player.getUniqueId().toString()).join();
            this.friendRequests.remove(requester);
            this.friends.add(requester);
        }
    }

    @Override
    public void denyRequest(final UUID requester) {
        if (this.friendRequests.contains(requester)) {
            SocialPlugin.getInstance().getDatabaseManager().executeUpdate("DELETE FROM friend_requests WHERE requester=? AND uuid=?", requester.toString(), this.player.getUniqueId().toString()).join();
            this.friendRequests.remove(requester);
        }
    }

    @Override
    public void setLastChatPartner(Player player) {
        this.lastChatPartner = Optional.of(player);
    }

    @Override
    public void setParty(Party party) {
        this.party = Optional.of(party);
    }

}
