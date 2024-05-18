package net.pulsir.comet;

import lombok.Getter;
import net.pulsir.comet.database.IDatabase;
import net.pulsir.comet.database.impl.FlatFile;
import net.pulsir.comet.database.impl.Mongo;
import net.pulsir.comet.listener.ProfileListener;
import net.pulsir.comet.mongo.MongoManager;
import net.pulsir.comet.punishment.manager.PunishmentManger;
import net.pulsir.comet.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

@Getter
public final class Comet extends JavaPlugin {

    @Getter private static Comet instance;

    private Config configuration;
    private Config punishments;

    private PunishmentManger punishmentManger;
    private MongoManager mongoManager;
    private IDatabase database;

    @Override
    public void onEnable() {
        instance = this;

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

        this.loadConfiguration();
        this.loadListeners(Bukkit.getPluginManager());
    }

    @Override
    public void onDisable() {
        this.database.savePlayers();
        instance = null;
    }

    private void loadConfiguration(){
        this.configuration = new Config(this, new File(getDataFolder(), "configuration.yml"),
                new YamlConfiguration(), "configuration.yml");
        this.punishments = new Config(this, new File(getDataFolder(), "punishments.yml"),
                new YamlConfiguration(), "punishments.yml");

        this.configuration.create();
        this.punishments.create();
    }

    private void loadListeners(PluginManager pluginManager){
        pluginManager.registerEvents(new ProfileListener(), this);
    }
}
