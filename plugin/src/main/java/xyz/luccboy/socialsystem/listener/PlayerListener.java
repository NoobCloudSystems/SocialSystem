package xyz.luccboy.socialsystem.listener;

import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;

import java.util.Random;

public class PlayerListener {

    @Subscribe
    public void onPlayerJoin(final PostLoginEvent event) {
        final Player player = event.getPlayer();
        final SocialPlayer socialPlayer = SocialPlugin.getInstance().getPlayerManager().registerPlayer(player);
        socialPlayer.getOnlineFriends().forEach(friend -> friend.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getIsOnline(player.getUsername()))));
    }

    @Subscribe
    public void onPlayerQuit(final DisconnectEvent event) {
        final Player player = event.getPlayer();
        final SocialPlayer socialPlayer = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(player);

        socialPlayer.getParty().ifPresent(party -> {
            if (party.getLeader() == player) {
                final Player newLeader = party.getMembers().get(new Random().nextInt(party.getMembers().size()));
                party.setLeader(newLeader);
                party.getMembers().forEach(member -> member.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "§b" + player.getUsername() + " §7hat die Party verlassen§8.")));
            }
        });

        socialPlayer.getOnlineFriends().forEach(friend -> friend.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getIsOffline(player.getUsername()))));

        SocialPlugin.getInstance().getPlayerManager().removePlayer(player);
    }

    @Subscribe
    public void onServerSwitch(final ServerPostConnectEvent event) {
        final Player player = event.getPlayer();

        if (event.getPreviousServer() != null && player.getCurrentServer().isPresent()) {
            final SocialPlayer socialPlayer = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(player);
            socialPlayer.getParty().ifPresent(party -> {
                if (party.getLeader() == player) {
                    party.sendSystemMessage(SocialPlugin.getInstance().getPrefix() + "Die Party betritt den Server §b" + player.getCurrentServer().get().getServerInfo().getName() + "§8.");
                    party.getMembers().forEach(member -> member.createConnectionRequest(player.getCurrentServer().get().getServer()).fireAndForget());
                }
            });
        }
    }

}
