package xyz.luccboy.socialsystem.api;

import lombok.Getter;
import xyz.luccboy.socialsystem.SocialPlugin;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import xyz.luccboy.socialsystem.api.data.Party;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AbstractParty implements Party {

    private Player leader;
    private final List<Player> members = new ArrayList<>();
    private final List<Player> invitedPlayers = new ArrayList<>();

    public AbstractParty(final Player leader) {
        this.leader = leader;
    }

    @Override
    public void sendSystemMessage(final String message) {
        this.leader.sendMessage(Component.text());
        this.members.forEach(member -> member.sendMessage(Component.text(message)));
    }

    @Override
    public void sendMessage(final Player sender, final String message) {
        this.leader.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "§b" + sender.getUsername() + " §8| §7" + message));
        this.members.forEach(member -> member.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + "§b" + sender.getUsername() + " §8| §7" + message)));
    }

    @Override
    public void addMember(final Player player) {
        this.members.add(player);
        this.leader.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getJoinedParty(player.getUsername())));
        this.members.forEach(member -> member.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getJoinedParty(player.getUsername()))));
    }

    @Override
    public void removeMember(final Player player) {
        this.members.remove(player);
        this.leader.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getLeftParty(player.getUsername())));
        this.members.forEach(member -> member.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getLeftParty(player.getUsername()))));
    }

    @Override
    public void setLeader(final Player player) {
        this.leader = player;
        this.leader.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNewPartyLeader(player.getUsername())));
        this.members.forEach(member -> member.sendMessage(Component.text(SocialPlugin.getInstance().getPrefix() + SocialPlugin.getInstance().getLocalization().getNewPartyLeader(player.getUsername()))));
    }

}
