package angga7togk.luckycrates;

import angga7togk.luckycrates.command.GiveKey;
import angga7togk.luckycrates.command.KeyAll;
import angga7togk.luckycrates.command.SetCrates;
import angga7togk.luckycrates.entity.LuckyCratesEntity;
import angga7togk.luckycrates.listener.Listeners;
import angga7togk.luckycrates.utils.FloatingUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.*;

public class LuckyCrates extends PluginBase {

    @Getter
    private static LuckyCrates instance;
    public static Config crates, pos, keys;
    public static Map<Player, String> setMode = new HashMap<>();
    public static String prefix;
    public static final Map<String, Entity> particles = new HashMap<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        for (String filename : Arrays.asList("crates.yml", "position.yml", "keys.yml")) {
            saveResource(filename);
        }
        saveDefaultConfig();
        crates = new Config(this.getDataFolder() + "/crates.yml", Config.YAML);
        pos = new Config(this.getDataFolder() + "/position.yml", Config.YAML);
        keys = new Config(this.getDataFolder() + "/keys.yml", Config.YAML);

        if (!pos.exists("crates", true)){
            pos.set("crates", new HashMap<>());
            pos.save();
        }
        prefix = getConfig().getString("prefix");


        this.checkDepend();
        this.checkCratesConfig();

        this.getServer().getPluginManager().registerEvents(new Listeners(), this);

        this.getServer().getCommandMap().registerAll(getName(), Arrays.asList(
                new GiveKey(),
                new KeyAll(),
                new SetCrates()
        ));

        // Register Entity LuckyCrates
        if(Entity.registerEntity("LuckyCrates", LuckyCratesEntity.class)){
            this.getLogger().info(prefix + "Entity LuckyCrates Registered.");
            spawnEntityAllFloatingText();
        }else{
            this.getLogger().warning(prefix + "Entity LuckyCrates not Register.");
        }
    }

    @Override
    public void onDisable() {
        for (Level level : this.getServer().getLevels().values()) {
            for (Entity entity : level.getEntities()) {
                if (entity instanceof LuckyCratesEntity) entity.close();
            }
        }
    }

    private void checkDepend(){
        Plugin depend = this.getServer().getPluginManager().getPlugin("FakeInventories");
        if (depend != null) {
            String version = depend.getDescription().getVersion();
            if(!version.equalsIgnoreCase("1.1.8")){
                this.getLogger().warning(prefix + TextFormat.RED + "please download the depend first, with version 1.1.8 https://github.com/IWareQ/FakeInventories/releases/tag/v1.1.8");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        } else {
            this.getLogger().warning(prefix + TextFormat.RED + "please download the depend first https://github.com/IWareQ/FakeInventories/releases/tag/v1.1.8");
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
                    if(item.isNull()) throw new RuntimeException("Item ID:" + id + " META:" +meta + " not found!");
                    if(drop.containsKey("enchantments")){
                        List<Map<String, Object>> enchantList = (List<Map<String, Object>>) drop.get("enchantments");
                        for (Map<String, Object> enchant : enchantList){
                            String enchantName = (String) enchant.get("name");
                            if(getEnchantmentByName(enchantName) == null) throw new RuntimeException("Error Enchantment not found : " + enchantName);
                        }
                    }
                }
            }
        }catch (RuntimeException e){
            this.getLogger().error("LuckyCrates Error: " + e.getMessage());
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    public static @Nullable Integer getEnchantmentByName(String enchant){
        Map<String, Integer> enchantmentsMap = new HashMap<>();
        enchantmentsMap.put("protection", 0);
        enchantmentsMap.put("fire_protection", 1);
        enchantmentsMap.put("feather_falling", 2);
        enchantmentsMap.put("blast_protection", 3);
        enchantmentsMap.put("projectile_protection", 4);
        enchantmentsMap.put("thorns", 5);
        enchantmentsMap.put("respiration", 6);
        enchantmentsMap.put("aqua_affinity", 7);
        enchantmentsMap.put("depth_strider", 8);
        enchantmentsMap.put("sharpness", 9);
        enchantmentsMap.put("smite", 10);
        enchantmentsMap.put("bane_of_arthropods", 11);
        enchantmentsMap.put("knockback", 12);
        enchantmentsMap.put("fire_aspect", 13);
        enchantmentsMap.put("looting", 14);
        enchantmentsMap.put("efficiency", 15);
        enchantmentsMap.put("silk_touch", 16);
        enchantmentsMap.put("unbreaking", 17);
        enchantmentsMap.put("fortune", 18);
        enchantmentsMap.put("power", 19);
        enchantmentsMap.put("punch", 20);
        enchantmentsMap.put("flame", 21);
        enchantmentsMap.put("infinity", 22);
        enchantmentsMap.put("luck_of_the_sea", 23);
        enchantmentsMap.put("lure", 24);
        enchantmentsMap.put("frost_walker", 25);
        enchantmentsMap.put("mending", 26);
        enchantmentsMap.put("binding", 27);
        enchantmentsMap.put("vanishing", 28);
        enchantmentsMap.put("impaling", 29);
        enchantmentsMap.put("riptide", 30);
        enchantmentsMap.put("loyalty", 31);
        enchantmentsMap.put("channeling", 32);
        enchantmentsMap.put("multishot", 33);
        enchantmentsMap.put("piercing", 34);
        enchantmentsMap.put("quick_charge", 35);
        enchantmentsMap.put("soul_speed", 36);
        enchantmentsMap.put("swift_sneak", 37);
        if(enchantmentsMap.containsKey(enchant)) return enchantmentsMap.get(enchant);
        return null;
    }

    private void spawnEntityAllFloatingText(){
        for (String idEntity : pos.getSection("crates").getKeys(false)) {
            String crateName = pos.getString("crates." + idEntity + ".crate");
            double x = pos.getDouble("crates." + idEntity + ".pos.x");
            double y = pos.getDouble("crates." + idEntity + ".pos.y");
            double z = pos.getDouble("crates." + idEntity + ".pos.z");
            String levelName = pos.getString("crates." + idEntity + ".pos.level");

            Level level = Server.getInstance().getLevelByName(levelName);
            if(level != null){
                String displayName = crates.getString(crateName + ".floating-text");
                FloatingUtils.createEntity(displayName, idEntity, new Position(x, y, z, level));
                this.getLogger().info(prefix + "FloatingText " + crateName + TextFormat.RESET + " Loaded.");
            }
        }
    }
}