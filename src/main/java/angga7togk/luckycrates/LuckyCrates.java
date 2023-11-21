package angga7togk.luckycrates;

import angga7togk.luckycrates.command.GiveKey;
import angga7togk.luckycrates.command.KeyAll;
import angga7togk.luckycrates.command.SetCrates;
import angga7togk.luckycrates.listener.Listeners;
import angga7togk.luckycrates.task.FloatingTask;
import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LuckyCrates extends PluginBase {

    @Getter
    private static LuckyCrates instance;
    public static Config crates, pos;
    public static Map<Player, String> setMode = new HashMap<>();
    public static String prefix;
    @Override
    public void onEnable() {
        instance = this;
        for (String filename : Arrays.asList("crates.yml", "position.yml")) {
            saveResource(filename);
        }
        saveDefaultConfig();
        crates = new Config(this.getDataFolder() + "/crates.yml", Config.YAML);
        pos = new Config(this.getDataFolder() + "/position.yml", Config.YAML);
        prefix = getConfig().getString("prefix");

        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
        this.getServer().getCommandMap().register("LuckyCrates", new GiveKey());
        this.getServer().getCommandMap().register("LuckyCrates", new KeyAll());
        this.getServer().getCommandMap().register("LuckyCrates", new SetCrates());
        this.getServer().getScheduler().scheduleRepeatingTask(this, new FloatingTask(), 20 * 5, true);
    }

}