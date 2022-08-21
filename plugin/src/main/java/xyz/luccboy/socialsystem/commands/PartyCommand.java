package xyz.luccboy.socialsystem.commands;

import xyz.luccboy.socialsystem.SocialPlugin;
import xyz.luccboy.socialsystem.api.AbstractParty;
import xyz.luccboy.socialsystem.api.data.Party;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PartyCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        if (!(invocation.source() instanceof final Player sender)) return;
        final SocialPlayer socialSender = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(sender);
        final String[] arguments = invocation.arguments();

        if (arguments.length == 1) {
            if (arguments[0].equalsIgnoreCase("info")) {
                socialSender.getParty().ifPresentOrElse(party -> {
                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getPartyListLeader(party.getLeader().getUsername())));
                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getPartyListMembers(String.valueOf(party.getMembers().size()))));

                    party.getMembers().forEach(member -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getPartyListMember(member.getUsername()))));

                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
                }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoParty())));
            } else if (arguments[0].equalsIgnoreCase("leave")) {
                socialSender.getParty().ifPresentOrElse(party -> party.removeMember(sender), () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoParty())));
            } else {
                sendHelp(sender);
            }
        } else if (arguments.length == 2) {
            if (arguments[0].equalsIgnoreCase("invite")) {
                if (arguments[1].equalsIgnoreCase(sender.getUsername())) {
                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoSelfInvite()));
                    return;
                }
                SocialPlugin.getInstance().getServer().getPlayer(arguments[1]).ifPresentOrElse(player -> {
                    final SocialPlayer target = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(player);

                    if (!target.getSettings().partyRequests()) {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getDisabledJump()));
                        return;
                    }

                    final Party party = socialSender.getParty().orElseGet(() -> {
                        final Party createdParty = new AbstractParty(sender);
                        socialSender.setParty(createdParty);
                        return createdParty;
                    });

                    target.getPartyInvites().put(sender, party);
                    party.getInvitedPlayers().add(target.getPlayer());

                    sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getSentPartyRequest(player.getUsername())));
                    player.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getReceivedPartyRequest(sender.getUsername())));
                    final TextComponent requestActions = Component.text(SocialPlugin.getInstance().getPrefix())
                            .append(Component.text(SocialPlugin.getInstance().getLocalization().getHoverPartyAccept())
                                    .clickEvent(ClickEvent.runCommand("party accept " + sender.getUsername())))
                            .append(Component.text(" "))
                            .append(Component.text(SocialPlugin.getInstance().getLocalization().getHoverPartyDeny())
                                    .clickEvent(ClickEvent.runCommand("party deny " + sender.getUsername())));
                    player.sendMessage(requestActions);
                }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNotOnline())));
            } else if (arguments[0].equalsIgnoreCase("accept")) {
                SocialPlugin.getInstance().getServer().getPlayer(arguments[1]).ifPresentOrElse(player -> {
                    final SocialPlayer requester = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(player);
                    requester.getParty().ifPresentOrElse(party -> {
                        if (party.getInvitedPlayers().contains(sender)) {
                            party.addMember(sender);
                            socialSender.getPartyInvites().remove(player);

                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getAcceptPartyRequest(player.getUsername())));
                        } else {
                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoPartyInvitation()));
                        }
                    }, () -> {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getPartyNotExisting()));
                        socialSender.getPartyInvites().remove(player);
                    });
                }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNotOnline())));
            } else if (arguments[0].equalsIgnoreCase("deny")) {
                SocialPlugin.getInstance().getServer().getPlayer(arguments[1]).ifPresentOrElse(player -> {
                    final SocialPlayer requester = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(player);
                    requester.getParty().ifPresentOrElse(party -> {
                        if (party.getInvitedPlayers().contains(sender)) {
                            party.getInvitedPlayers().remove(sender);
                            socialSender.getPartyInvites().remove(player);

                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getDenyPartyRequest(player.getUsername())));
                        } else {
                            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoPartyInvitation()));
                        }
                    }, () -> {
                        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getPartyNotExisting()));
                        socialSender.getPartyInvites().remove(player);
                    });
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

        if (arguments.length == 0 || arguments.length == 1) {
            final List<String> suggestions = List.of("info", "invite", "accept", "deny", "leave");
            return CompletableFuture.supplyAsync(() -> suggestions);
        } else if (arguments.length == 2 && (arguments[0].equalsIgnoreCase("invite") || arguments[0].equalsIgnoreCase("accept")
                || arguments[0].equalsIgnoreCase("deny"))) {
            return CompletableFuture.supplyAsync(() -> SocialPlugin.getInstance().getServer().getAllPlayers().stream().map(Player::getUsername).toList());
        }

        return CompletableFuture.supplyAsync(Collections::emptyList);
    }

    private void sendHelp(final Player sender) {
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/party info"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/party invite <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/party accept <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/party deny <player>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/party leave"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/party allowRequests <true/false>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "/p <message>"));
        sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix()));
    }

}
