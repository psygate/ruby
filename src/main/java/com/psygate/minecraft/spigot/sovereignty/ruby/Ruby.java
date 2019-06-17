package com.psygate.minecraft.spigot.sovereignty.ruby;

import com.psygate.minecraft.spigot.sovereignty.nucleus.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.nucleus.managment.NucleusPlugin;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.DatabaseInterface;
import com.psygate.minecraft.spigot.sovereignty.ruby.configuration.Configuration;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.listeners.SnitchListener;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordRenderer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

/**
 * Created by psygate (https://github.com/psygate) on 21.03.2016.
 */
public class Ruby extends JavaPlugin implements NucleusPlugin {
    private final static Logger LOG = Logger.getLogger(Ruby.class.getName());
    public final static int PAGE_SIZE = 10;
    private static Ruby instance;
    private Configuration conf;
    private DatabaseInterface dbi;
    private Logger logger;

    static {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.ALL);
        List<Handler> handlers = Arrays.asList(LOG.getHandlers());

        if (handlers.stream().noneMatch(h -> h instanceof FileHandler)) {
            try {
                File logdir = new File("logs/nucleus_logs/ruby/");
                if (!logdir.exists()) {
                    logdir.mkdirs();
                }
                FileHandler fh = new FileHandler(
                        "logs/nucleus_logs/ruby/ruby.%u.%g.log",
                        8 * 1024 * 1024,
                        12,
                        true
                );
                fh.setLevel(Level.ALL);
                fh.setEncoding("UTF-8");
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                LOG.addHandler(fh);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getLogger(String name) {
        Logger log = Logger.getLogger(name);
        log.setParent(LOG);
        log.setUseParentHandlers(true);
        log.setLevel(Level.ALL);
        return log;
    }

    @Override
    public void onEnable() {
        try {
            instance = this;
            saveDefaultConfig();
            conf = new Configuration(getConfig());
            RecordRenderer.getInstance();
            Nucleus.getInstance().register(this);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(p -> p.sendMessage(ChatColor.YELLOW + "Ruby cache flush initiated."));
                SnitchManager.getInstance().flush();
                Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(p -> p.sendMessage(ChatColor.GREEN + "Ruby cache flush done."));
            }, 20 * 60 * 30, 20 * 60 * 30);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(p -> p.sendMessage(ChatColor.YELLOW + "Ruby cache flush initiated."));
        SnitchManager.getInstance().flush();
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(p -> p.sendMessage(ChatColor.GREEN + "Ruby cache flush done."));
    }

    public static Ruby getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin not initialized.");
        }

        return instance;
    }

    public static DatabaseInterface DBI() {
        return getInstance().dbi;
    }

    public static Configuration getConf() {
        return getInstance().conf;
    }

    @Override
    public int getWantedDBVersion() {
        return 1;
    }

    @Override
    public void fail() {
        System.err.println("Ruby unable to load.");
        Bukkit.shutdown();
    }

    @Override
    public List<Listener> getListeners() {
        try {
            return Arrays.asList(
                    new SnitchListener()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Logger getPluginLogger() {
        return logger;
    }

    @Override
    public void setDatabaseInterface(DatabaseInterface databaseInterface) {
        dbi = databaseInterface;
    }

    public void reloadRuby() {
        SnitchManager.getInstance().flush();
        reloadConfig();
        conf = new Configuration(getConfig());
        RecordRenderer.getInstance();
        SnitchManager.getInstance().reload(Bukkit.getWorlds());
    }
}
