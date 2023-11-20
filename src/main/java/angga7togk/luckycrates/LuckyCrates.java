package angga7togk.luckycrates;

import angga7togk.luckycrates.command.GiveKey;
import angga7togk.luckycrates.command.KeyAll;
import angga7togk.luckycrates.command.SetCrates;
import angga7togk.luckycrates.listener.Listeners;
import angga7togk.luckycrates.task.FloatingTask;
import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.HashMap;
import java.util.Map;

public class LuckyCrates extends PluginBase {

    public static Config cfg, crates, pos;
    public static Map<Player, String> setMode = new HashMap<>();
    public static String prefix;
    @Override
    public void onEnable() {
        this.saveResource("config.yml");
        this.saveResource("crates.yml");
        this.saveResource("position.yml");
        cfg = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        crates = new Config(this.getDataFolder() + "/crates.yml", Config.YAML);
        pos = new Config(this.getDataFolder() + "/position.yml", Config.YAML);
        prefix = cfg.getString("prefix");

        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
        this.getServer().getCommandMap().register("cratesCommand", new GiveKey());
        this.getServer().getCommandMap().register("cratesCommand", new KeyAll());
        this.getServer().getCommandMap().register("cratesCommand", new SetCrates());
        this.getServer().getScheduler().scheduleRepeatingTask(this, new FloatingTask(), 20 * 5, true);
    }


}