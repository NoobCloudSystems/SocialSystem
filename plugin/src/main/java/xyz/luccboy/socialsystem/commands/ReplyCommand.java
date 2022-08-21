package xyz.luccboy.socialsystem.commands;

import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReplyCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        if (!(invocation.source() instanceof final Player sender)) return;
        final SocialPlayer socialSender = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(sender);
        final String[] arguments = invocation.arguments();

        if (arguments.length > 0) {
            final StringBuilder message = new StringBuilder();
            for (final String arg : arguments) {
                if (arg.equals(arguments[0]))
                    message.append(arg);
                else
                    message.append(" ").append(arg);
            }

            socialSender.getLastChatPartner().ifPresentOrElse(player -> {
                sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getMessageToFriend(player.getUsername(), message.toString())));
                player.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getMessageFromFriend(sender.getUsername(), message.toString())));
            }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getStartConversation())));
        } else {
            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoMessage()));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        if (invocation.source() instanceof final Player sender) {
            return CompletableFuture.supplyAsync(() -> SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(sender).getOnlineFriends().stream().map(Player::getUsername).toList());
        }
        return CompletableFuture.supplyAsync(Collections::emptyList);
    }

}
