package xyz.luccboy.socialsystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import xyz.luccboy.socialsystem.api.AbstractSocialSystem;
import xyz.luccboy.socialsystem.commands.*;
import xyz.luccboy.socialsystem.api.AbstractPlayerManager;
import xyz.luccboy.socialsystem.config.ConfigManager;
import xyz.luccboy.socialsystem.config.Localization;
import xyz.luccboy.socialsystem.database.DatabaseManager;
import xyz.luccboy.socialsystem.listener.PlayerListener;
import xyz.luccboy.socialsystem.listener.ServerMessageListener;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

@Getter
@Plugin(id = "socialsystem", name = "SocialSystem", version = "1.0-SNAPSHOT", description = "NoobCloud compatible friends and party system for Velocity", authors = {"Luccboy"}, dependencies = {@Dependency(id = "noobcloud")})
public class SocialPlugin {

    @Getter
    private static SocialPlugin instance;

    private final ProxyServer server;
    private final Logger logger;

    private DatabaseManager databaseManager;
    private final AbstractPlayerManager playerManager = new AbstractPlayerManager();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ConfigManager configManager;

    @Inject
    public SocialPlugin(final ProxyServer server, final Logger logger) {
        this.server = server;
        this.logger = logger;

        instance = this;
        this.configManager = new ConfigManager();
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        this.databaseManager = new DatabaseManager().createTables();
        new AbstractSocialSystem(this.playerManager);

        final CommandManager commandManager = this.server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("friend").build(), new FriendCommand());
        commandManager.register(commandManager.metaBuilder("msg").build(), new MsgCommand());
        commandManager.register(commandManager.metaBuilder("reply").aliases("r").build(), new ReplyCommand());
        commandManager.register(commandManager.metaBuilder("party").build(), new PartyCommand());
        commandManager.register(commandManager.metaBuilder("partychat").aliases("pc", "p").build(), new PartyChatCommand());

        this.server.getEventManager().register(this, new PlayerListener());
        this.server.getEventManager().register(this, new ServerMessageListener());
    }

    @Subscribe
    public void onProxyShutdown(final ProxyShutdownEvent event) {
        this.databaseManager.disconnect();
    }

    public String getPrefix() {
        return this.configManager.getConfig().getPrefix();
    }

    public Localization getLocalization() {
        return this.configManager.getLocalization();
    }

}
