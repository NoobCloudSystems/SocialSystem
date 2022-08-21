package xyz.luccboy.socialsystem.config;

import lombok.Getter;
import xyz.luccboy.socialsystem.SocialPlugin;

import java.io.*;
import java.nio.file.Files;

public class ConfigManager {

    @Getter
    private Config config;

    @Getter
    private Localization localization;

    public ConfigManager() {
        final File configFile = new File("plugins/SocialSystem/config.json");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            writeConfig();
        }

        exportResource("en.json", "plugins/SocialSystem/messages");
        exportResource("de.json", "plugins/SocialSystem/messages");

        try (final Reader configReader = new FileReader("plugins/SocialSystem/config.json")) {
            this.config = SocialPlugin.getInstance().getGson().fromJson(configReader, Config.class);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        try (final Reader localeReader = new FileReader("plugins/SocialSystem/messages/" + this.config.getLanguage() + ".json")) {
            this.localization = SocialPlugin.getInstance().getGson().fromJson(localeReader, Localization.class);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void writeConfig() {
        try (final FileWriter fileWriter = new FileWriter("plugins/SocialSystem/config.json")) {
            SocialPlugin.getInstance().getGson().toJson(new Config("de", "§bSocialSystem §8| §7", "127.0.0.1", 3306, "socialsystem", "server", ""), fileWriter);
        } catch (final IOException ignored) {}
    }

    private void exportResource(final String resourceName, final String path) {
        final File file = new File(path + "/" + resourceName);
        if (!file.exists()) {
            final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
            final File parentFile = file.getParentFile();
            if (!parentFile.exists()) parentFile.mkdirs();
            try {
                if (inputStream != null) {
                    Files.copy(inputStream, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}
