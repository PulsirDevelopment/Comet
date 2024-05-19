package net.pulsir.comet;

import lombok.Getter;
import net.pulsir.comet.command.HistoryCommand;
import net.pulsir.comet.command.KickCommand;
import net.pulsir.comet.command.WarnCommand;
import net.pulsir.comet.database.IDatabase;
import net.pulsir.comet.database.impl.FlatFile;
import net.pulsir.comet.database.impl.Mongo;
import net.pulsir.comet.mongo.MongoManager;
import net.pulsir.comet.punishment.manager.PunishmentManger;
import net.pulsir.comet.utils.config.Config;
import net.pulsir.comet.utils.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

@Getter
public final class Comet extends JavaPlugin {

    @Getter private static Comet instance;

    private Config configuration;
    private Config language;
    private Config punishments;

    private Message message;

    private PunishmentManger punishmentManger;
    private MongoManager mongoManager;
    private IDatabase database;

    private final NamespacedKey historyButton = new NamespacedKey(this, "button");

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfiguration();

        if (Objects.requireNonNull(getConfiguration().getConfiguration().getString("database")).equalsIgnoreCase("mongo")) {
            this.mongoManager = new MongoManager();
            database = new Mongo();
            Bukkit.getConsoleSender().sendMessage("[Comet] Successfully loaded MONGO as database.");
        } else if (Objects.requireNonNull(getConfiguration().getConfiguration().getString("database")).equalsIgnoreCase("flatfile")) {
            database = new FlatFile();
            Bukkit.getConsoleSender().sendMessage("[Comet] Successfully loaded FLATFILE as database.");
        } else {
            Bukkit.getConsoleSender().sendMessage("[Comet] Unsupported database. Loading default one [FLATFILE].");
            database = new FlatFile();
        }
        this.punishmentManger = new PunishmentManger();

        this.database.loadPlayers();

        this.message = new Message();

        this.loadCommands();
    }

    @Override
    public void onDisable() {
        this.database.savePlayers();
        instance = null;
    }

    private void loadConfiguration() {
        this.configuration = new Config(this, new File(getDataFolder(), "configuration.yml"),
                new YamlConfiguration(), "configuration.yml");
        this.punishments = new Config(this, new File(getDataFolder(), "punishments.yml"),
                new YamlConfiguration(), "punishments.yml");
        this.language = new Config(this, new File(getDataFolder(), "language.yml"),
                new YamlConfiguration(), "language.yml");

        this.configuration.create();
        this.punishments.create();
        this.language.create();
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("history")).setExecutor(new HistoryCommand());
        Objects.requireNonNull(getCommand("kick")).setExecutor(new KickCommand());
        Objects.requireNonNull(getCommand("warn")).setExecutor(new WarnCommand());
    }
}
