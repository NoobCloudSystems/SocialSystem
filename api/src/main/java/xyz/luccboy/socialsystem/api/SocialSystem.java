package xyz.luccboy.socialsystem.api;

import xyz.luccboy.socialsystem.api.managers.PlayerManager;

/**
 * The API of SocialSystem
 */
public abstract class SocialSystem {

    private static SocialSystem instance;

    /**
     * The player manager storing all players
     * @return The {@link PlayerManager}
     */
    public abstract PlayerManager getPlayerManager();

    /**
     * The constructor
     */
    public SocialSystem() {
        instance = this;
    }

    /**
     * The access point for the API
     * @return {@link SocialSystem}
     */
    public static SocialSystem getInstance() {
        return instance;
    }

}
