package xyz.luccboy.socialsystem.database;

import io.github.heliumdioxid.database.api.data.ConnectionData;
import io.github.heliumdioxid.database.mysql.MySQLConnectionHandler;
import io.github.heliumdioxid.database.mysql.MySQLDatabaseConnection;
import io.github.heliumdioxid.database.mysql.config.MySQLConnectionConfig;
import io.github.heliumdioxid.database.mysql.utils.Function;
import xyz.luccboy.socialsystem.SocialPlugin;
import xyz.luccboy.socialsystem.config.Config;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final MySQLDatabaseConnection databaseConnection;
    private final MySQLConnectionHandler connectionHandler;

    public DatabaseManager() {
        final Config config = SocialPlugin.getInstance().getConfigManager().getConfig();
        final ConnectionData connectionData = new ConnectionData(config.getSqlHost(), config.getSqlUsername(), config.getSqlPassword(), config.getSqlDatabase(), config.getSqlPort());
        final MySQLConnectionConfig connectionConfig = new MySQLConnectionConfig(connectionData);
        connectionConfig.applyDefaultHikariConfig();

        this.databaseConnection = new MySQLDatabaseConnection(connectionConfig);
        this.connectionHandler = this.databaseConnection.connect().join().get();
    }

    public DatabaseManager createTables() {
        this.executeUpdate("CREATE TABLE IF NOT EXISTS friends (uuid VARCHAR(36), friend VARCHAR(36))").join();
        this.executeUpdate("CREATE TABLE IF NOT EXISTS friend_requests (requester VARCHAR(36), uuid VARCHAR(36))").join();
        this.executeUpdate("CREATE TABLE IF NOT EXISTS settings (uuid VARCHAR(36), friend_requests TINYINT, party_requests TINYINT, jump TINYINT, PRIMARY KEY (uuid))").join();
        return this;
    }

    public void disconnect() {
        this.databaseConnection.disconnect();
    }

    public CompletableFuture<MySQLConnectionHandler.UpdateResult> executeUpdate(final String query,  final String... parameters) {
        return CompletableFuture.supplyAsync(() -> this.connectionHandler.executeUpdate(query, parameters));
    }

    public <T> CompletableFuture<T> executeQuery(final Function<ResultSet, T> consumer, final T defaultValue, final String query, final String... parameters) {
        return CompletableFuture.supplyAsync(() -> this.connectionHandler.executeQuery(consumer, defaultValue, query, parameters));
    }

}
