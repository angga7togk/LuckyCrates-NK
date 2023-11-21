package angga7togk.luckycrates;

import angga7togk.luckycrates.command.GiveKey;
import angga7togk.luckycrates.command.KeyAll;
import angga7togk.luckycrates.command.SetCrates;
import angga7togk.luckycrates.listener.Listeners;
import angga7togk.luckycrates.task.FloatingTask;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuckyCrates extends PluginBase {

    @Getter
    private static LuckyCrates instance;
    public static Config crates, pos;
    public static Map<Player, String> setMode = new HashMap<>();
    public static String prefix;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        for (String filename : Arrays.asList("crates.yml", "position.yml")) {
            saveResource(filename);
        }
        saveDefaultConfig();
        crates = new Config(this.getDataFolder() + "/crates.yml", Config.YAML);
        pos = new Config(this.getDataFolder() + "/position.yml", Config.YAML);
        prefix = getConfig().getString("prefix");

        this.checkDepend();
        this.checkCratesConfig();

        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
        this.getServer().getCommandMap().register("LuckyCrates", new GiveKey());
        this.getServer().getCommandMap().register("LuckyCrates", new KeyAll());
        this.getServer().getCommandMap().register("LuckyCrates", new SetCrates());
        this.getServer().getScheduler().scheduleRepeatingTask(this, new FloatingTask(), 20 * 5, true);
    }

    private void checkDepend(){
        Plugin depend = this.getServer().getPluginManager().getPlugin("FakeInventories");
        if (depend != null) {
            String version = depend.getDescription().getVersion();
            if(!version.equalsIgnoreCase("1.1.5")){
                this.getServer().getLogger().warning(prefix + TextFormat.RED + "please download the depend first, with version 1.1.5 https://github.com/IWareQ/FakeInventories/releases/tag/v1.1.5");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        } else {
            this.getServer().getLogger().warning(prefix + TextFormat.RED + "please download the depend first https://github.com/IWareQ/FakeInventories/releases/tag/v1.1.5");
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void checkCratesConfig(){
        try {
            for (String crateName : crates.getKeys(false)){
                ConfigSection crateSect = LuckyCrates.crates.getSection(crateName);
                if(!crateSect.exists("drops", true)) throw new RuntimeException("crates.yml key " + crateName + ":drops: not found!");
                if(!crateSect.exists("amount", true)) throw new RuntimeException("crates.yml key " + crateName + ":amount: not found!");
                if(!crateSect.exists("floating-text", true)) throw new RuntimeException("crates.yml key " + crateName + ":floating-text: not found!");
                List<Map<String, Object>> dropsList = crateSect.getList("drops");
                for (Map<String, Object> drop : dropsList){
                    if(!drop.containsKey("id")) throw new RuntimeException("crates.yml key " + crateName + ":drops:id: not found!");
                    if(!drop.containsKey("meta")) throw new RuntimeException("crates.yml key " + crateName + ":drops:meta: not found!");
                    if(!drop.containsKey("amount")) throw new RuntimeException("crates.yml key " + crateName + ":drops:amount: not found!");
                    if(!drop.containsKey("chance")) throw new RuntimeException("crates.yml key " + crateName + ":drops:chance: not found!");
                    int id = (int) drop.get("id");
                    int meta = (int) drop.get("meta");
                    int amount = (int) drop.get("amount");
                    Item item = new Item(id, meta, amount);
                    if(item.isNull()){
                        throw new RuntimeException("Item ID:" + id + " META:" +meta + " not found!");
                    }
                    if(drop.containsKey("enchantments")){
                        List<Map<String, Object>> enchantList = (List<Map<String, Object>>) drop.get("enchantments");
                        for (Map<String, Object> enchant : enchantList){
                            String enchantName = (String) enchant.get("name");
                            if(Enchantment.getEnchantment(enchantName) == null){
                                throw new RuntimeException("Error Enchantment not found : " + enchantName);
                            }
                        }
                    }
                }
            }
        }catch (RuntimeException e){
            this.getServer().getLogger().error("LuckyCrates Error: " + e.getMessage());
            this.getServer().shutdown();
        }
    }

}