package xyz.luccboy.socialsystem.listener;

import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.event.Subscribe;
import xyz.luccboy.noobcloud.api.NoobCloudAPI;
import xyz.luccboy.noobcloud.api.events.velocity.ServerMessageVelocityEvent;

import java.util.List;
import java.util.UUID;

public class ServerMessageListener {

    /** These are messages sent by a Minestom server, for example by a GUI */
    @Subscribe
    public void onServerMessage(final ServerMessageVelocityEvent event) {
        final List<String> message = event.getMessage();

        if (message.contains("socialsystem_friend_jump") && message.size() == 3) {
            SocialPlugin.getInstance().getServer().getPlayer(UUID.fromString(message.get(1))).ifPresent(player -> NoobCloudAPI.getInstance().getUsernameByUUID(UUID.fromString(message.get(2))).ifPresent(target -> SocialPlugin.getInstance().getServer().getCommandManager().executeAsync(player, "friend jump " + target)));
        } else if (message.contains("socialsystem_friend_party")) {
            SocialPlugin.getInstance().getServer().getPlayer(UUID.fromString(message.get(1))).ifPresent(player -> NoobCloudAPI.getInstance().getUsernameByUUID(UUID.fromString(message.get(2))).ifPresent(target -> SocialPlugin.getInstance().getServer().getCommandManager().executeAsync(player, "party invite " + target)));
        } else if (message.contains("socialsystem_friend_remove")) {
            SocialPlugin.getInstance().getServer().getPlayer(UUID.fromString(message.get(1))).ifPresent(player -> NoobCloudAPI.getInstance().getUsernameByUUID(UUID.fromString(message.get(2))).ifPresent(target -> SocialPlugin.getInstance().getServer().getCommandManager().executeAsync(player, "friend remove " + target)));
        } else if (message.contains("socialsystem_request_accept")) {
            SocialPlugin.getInstance().getServer().getPlayer(UUID.fromString(message.get(1))).ifPresent(player -> NoobCloudAPI.getInstance().getUsernameByUUID(UUID.fromString(message.get(2))).ifPresent(target -> SocialPlugin.getInstance().getServer().getCommandManager().executeAsync(player, "friend accept " + target)));
        } else if (message.contains("socialsystem_request_decline")) {
            SocialPlugin.getInstance().getServer().getPlayer(UUID.fromString(message.get(1))).ifPresent(player -> NoobCloudAPI.getInstance().getUsernameByUUID(UUID.fromString(message.get(2))).ifPresent(target -> SocialPlugin.getInstance().getServer().getCommandManager().executeAsync(player, "friend deny " + target)));
        }
    }

}
