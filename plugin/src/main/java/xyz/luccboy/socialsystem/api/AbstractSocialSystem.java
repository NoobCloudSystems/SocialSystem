package xyz.luccboy.socialsystem.api;

import lombok.Getter;

public class AbstractSocialSystem extends SocialSystem {

    @Getter
    private final AbstractPlayerManager playerManager;

    public AbstractSocialSystem(final AbstractPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

}
