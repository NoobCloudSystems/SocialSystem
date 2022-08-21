package xyz.luccboy.socialsystem.config;

import lombok.Getter;

@Getter
public class Config {

    private final String language;
    private final String prefix;
    private final String sqlHost;
    private final int sqlPort;
    private final String sqlDatabase;
    private final String sqlUsername;
    private final String sqlPassword;

    public Config(final String language, final String prefix, final String sqlHost, final int sqlPort, final String sqlDatabase, final String sqlUsername, final String sqlPassword) {
        this.language = language;
        this.prefix = prefix;
        this.sqlHost = sqlHost;
        this.sqlPort = sqlPort;
        this.sqlDatabase = sqlDatabase;
        this.sqlUsername = sqlUsername;
        this.sqlPassword = sqlPassword;
    }

}
