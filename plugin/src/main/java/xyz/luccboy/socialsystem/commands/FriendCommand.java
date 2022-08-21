package xyz.luccboy.socialsystem.commands;

import xyz.luccboy.noobcloud.api.server.Server;
import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import xyz.luccboy.noobcloud.api.NoobCloudAPI;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FriendCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        if (!(invocation.source() instanceof final Player sender)) return;
        final SocialPlayer socialSender = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(sender);
        final String[] arguments = invocation.arguments();

        if (arguments.length == 1) {
            if (arguments[0].equalsIgnoreCase("list")) {
                sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
                final List<UUID> friends = socialSender.getFriends();
                if (friends.isEmpty()) {
                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoFriends()));
                } else {
                    friends.forEach(friend -> {
                        NoobCloudAPI.getInstance().getCloudPlayer(friend).ifPresentOrElse(cloudPlayer -> {
                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getFriendListOnline(cloudPlayer.getUsername(), cloudPlayer.getServer().map(Server::getName).orElse(""))));
                        }, () -> {
                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getFriendListOffline(NoobCloudAPI.getInstance().getUsernameByUUID(friend).orElse(friend.toString()))));
                        });
                    });
                }
                sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
            } else if (arguments[0].equalsIgnoreCase("listRequests")) {
                sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
                final List<UUID> requests = socialSender.getFriendRequests();
                if (requests.isEmpty()) {
                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoFriendRequests()));
                } else {
                    requests.forEach(requester -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getFriendListRequest(NoobCloudAPI.getInstance().getUsernameByUUID(requester).orElseGet(requester::toString)))));
                }
                sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
            } else {
                sendHelp(sender);
            }
        } else if (arguments.length == 2) {
            if (arguments[0].equalsIgnoreCase("add")) {
                if (arguments[1].equalsIgnoreCase(sender.getUsername())) {
                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoSelfRequest()));
                    return;
                }
                SocialPlugin.getInstance().getServer().getPlayer(arguments[1]).ifPresentOrElse(player -> {
                    final SocialPlayer target = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(player);
                    if (socialSender.getFriends().contains(target.getPlayer().getUniqueId())) {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getAlreadyFriends()));
                        return;
                    }
                    target.addFriendRequest(sender.getUniqueId());

                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getSentFriendRequest(player.getUsername())));
                    player.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getReceivedFriendRequest(sender.getUsername())));
                    final TextComponent requestActions = Component.text(SocialPlugin.getInstance().getPrefix())
                            .append(Component.text(SocialPlugin.getInstance().getLocalization().getHoverFriendAccept())
                                    .clickEvent(ClickEvent.runCommand("friend accept " + sender.getUsername())))
                            .append(Component.text(" "))
                            .append(Component.text(SocialPlugin.getInstance().getLocalization().getHoverFriendDeny())
                                    .clickEvent(ClickEvent.runCommand("friend deny " + sender.getUsername())));
                    player.sendMessage(requestActions);
                }, () -> {
                    final String targetName = arguments[1];
                    NoobCloudAPI.getInstance().getUUIDByUsername(targetName).ifPresentOrElse(player -> {

                        socialSender.sendFriendRequest(player);
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getSentFriendRequest(targetName)));
                    }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNeverOnlineBefore())));
                });
            } else if (arguments[0].equalsIgnoreCase("remove")) {
                final String requesterName = arguments[1];
                NoobCloudAPI.getInstance().getUUIDByUsername(requesterName).ifPresentOrElse(requester -> {
                    if (!socialSender.getFriends().contains(requester)) {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNotFriendWith()));
                        return;
                    }
                    socialSender.removeFriend(requester);

                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoFriendAnymore(requesterName)));
                }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNeverOnlineBefore())));
            } else if (arguments[0].equalsIgnoreCase("accept")) {
                final String requesterName = arguments[1];
                NoobCloudAPI.getInstance().getUUIDByUsername(requesterName).ifPresentOrElse(requester -> {
                    if (socialSender.getFriendRequests().contains(requester)) {
                        socialSender.acceptRequest(requester);
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getAcceptFriendRequest(requesterName)));
                    } else {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoFriendRequestFrom()));
                    }
                }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNeverOnlineBefore())));
            } else if (arguments[0].equalsIgnoreCase("deny")) {
                final String requesterName = arguments[1];
                NoobCloudAPI.getInstance().getUUIDByUsername(requesterName).ifPresentOrElse(requester -> {
                    if (socialSender.getFriendRequests().contains(requester)) {
                        socialSender.denyRequest(requester);
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getDenyFriendRequest(requesterName)));
                    } else {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoFriendRequestFrom()));
                    }
                }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNeverOnlineBefore())));
            } else if (arguments[0].equalsIgnoreCase("jump")) {
                SocialPlugin.getInstance().getServer().getPlayer(arguments[1]).ifPresentOrElse(player -> {
                    if (socialSender.getFriends().contains(player.getUniqueId())) {
                        if (!SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(player).getSettings().jump()) {
                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getDisabledJump()));
                            return;
                        }

                        if (sender.getCurrentServer().isPresent() && player.getCurrentServer().isPresent()) {
                            if (sender.getCurrentServer().get().getServerInfo() == player.getCurrentServer().get().getServerInfo()) {
                                sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getAlreadyConnected()));
                            } else {
                                sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getWillBeConnected(player.getCurrentServer().get().getServerInfo().getName())));
                                final ConnectionRequestBuilder.Result result = sender.createConnectionRequest(player.getCurrentServer().get().getServer()).connect().join();
                                if (!result.isSuccessful()) {
                                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getErrorJump()));
                                }
                            }
                        } else {
                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getErrorJump()));
                        }
                    } else {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNotFriendWith()));
                    }
                }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNotOnline())));
            } else {
                sendHelp(sender);
            }
        } else {
            sendHelp(sender);
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        final String[] arguments = invocation.arguments();

        if (invocation.source() instanceof final Player sender) {
            if (arguments.length == 0 || arguments.length == 1) {
                final List<String> suggestions = List.of("list", "listRequests", "add", "remove", "accept", "deny", "jump");
                return CompletableFuture.supplyAsync(() -> suggestions);
            } else if (arguments.length == 2) {
                if (arguments[0].equalsIgnoreCase("add")) {
                    return CompletableFuture.supplyAsync(() -> SocialPlugin.getInstance().getServer().getAllPlayers().stream().map(Player::getUsername).toList());
                } else if (arguments[0].equalsIgnoreCase("remove") || arguments[0].equalsIgnoreCase("jump")) {
                    return CompletableFuture.supplyAsync(() -> SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(sender).getOnlineFriends().stream().map(Player::getUsername).toList());
                } else if (arguments[0].equalsIgnoreCase("accept") || arguments[0].equalsIgnoreCase("deny")) {
                    return CompletableFuture.supplyAsync(() -> SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(sender).getFriendRequests().stream().map(uuid -> NoobCloudAPI.getInstance().getUsernameByUUID(uuid).get()).toList());
                }
            }
        }

        return CompletableFuture.supplyAsync(Collections::emptyList);
    }

    private void sendHelp(final Player sender) {
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend list"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend listRequests"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend add <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend remove <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend accept <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend deny <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend jump <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend allowRequests <true/false>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/friend allowJump <true/false>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/msg <player> <message>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/r <message>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
    }

}