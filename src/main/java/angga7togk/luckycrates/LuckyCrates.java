package angga7togk.luckycrates;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class LuckyCrates extends PluginBase {

    public static Config cfg, crates;
    public static String prefix;
    @Override
    public void onEnable() {
        this.saveResource("config.yml");
        this.saveResource("crates.yml");
        cfg = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        crates = new Config(this.getDataFolder() + "/crates.yml", Config.YAML);
        prefix = cfg.getString("prefix");
    }


}