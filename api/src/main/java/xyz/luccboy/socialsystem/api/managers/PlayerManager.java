package xyz.luccboy.socialsystem.api.managers;

import com.velocitypowered.api.proxy.Player;
import xyz.luccboy.socialsystem.api.data.SocialPlayer;

import java.util.Map;

/**
 * The PlayerManager stores all {@link SocialPlayer SocialPlayers}
 */
public interface PlayerManager {
    /**
     * The map storing all {@link Player players} and the corresponding {@link SocialPlayer}
     * @return A map with all {@link Player players} and their {@link SocialPlayer}
     */
    Map<Player, SocialPlayer> getPlayers();

    /**
     * Gets a {@link SocialPlayer} by a {@link Player}
     * @param player The {@link Player}
     * @return The {@link SocialPlayer}
     */
    SocialPlayer getSocialPlayer(Player player);
}
