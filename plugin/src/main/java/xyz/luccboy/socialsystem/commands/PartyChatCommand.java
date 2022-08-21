package xyz.luccboy.socialsystem.commands;

import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;

public class PartyChatCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        if (!(invocation.source() instanceof final Player sender)) return;
        final SocialPlayer socialSender = SocialPlugin.getInstance().getPlayerManager().getSocialPlayer(sender);
        final String[] arguments = invocation.arguments();

        if (arguments.length > 0) {
            socialSender.getParty().ifPresentOrElse(party -> {
                final StringBuilder message = new StringBuilder();
                for (final String arg : arguments) {
                    if (arg.equals(arguments[0]))
                        message.append(arg);
                    else
                        message.append(" ").append(arg);
                }

                party.sendMessage(sender, message.toString());
            }, () -> sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoParty())));
        } else {
            sender.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNoMessage()));
        }
    }

}
