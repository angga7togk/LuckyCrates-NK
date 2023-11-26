package angga7togk.luckycrates.task;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;

import java.util.*;

public class InstantTask extends Task {
    @Getter
    private final Player player;
    private final List<Map<String, Object>> safeDrops = new ArrayList<>();
    public InstantTask(Player player, String crateName){
        this.player = player;
        ConfigSection crateSect = LuckyCrates.crates.getSection(crateName);
        List<Map<String, Object>> drops = crateSect.getList("drops");
        drops.forEach((objectMap -> {
            int chance = Math.min((int) objectMap.get("chance"), 100);
            for (int ii = 0; ii <= ((chance > 30) ? chance * 2 : chance); ii++) {
                this.safeDrops.add(objectMap);
            }
        }));
    }

    @Override
    public void onRun(int i) {
        Collections.shuffle(this.safeDrops);
        Random random = new Random();
        int randomIndex = random.nextInt(this.safeDrops.size());
        Map<String, Object> drop = this.safeDrops.get(randomIndex);

        Item item = createItem(drop);
        if(item != null){
            player.getInventory().addItem(item);
            if(drop.containsKey("commands")){
                for (String command : (List<String>) drop.get("commands")){
                    Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command.replace("{player}", this.getPlayer().getName()));
                }
            }
        }
    }

    private Item createItem(Map<String, Object> drop) {
        int id = (int) drop.get("id");
        int meta = (int) drop.get("meta");
        int amount = (int) drop.get("amount");
        String customName = drop.containsKey("name") ? (String) drop.get("name") : null;
        String lore = drop.containsKey("lore") ? (String) drop.get("lore") : null;
        Item item = new Item(id, meta, amount);
        if(customName != null){
            item.setCustomName(customName);
        }
        if(lore != null){
            item.setLore(lore);
        }
        if(drop.containsKey("enchantments")){
            List<Map<String, Object>> enchantList = (List<Map<String, Object>>) drop.get("enchantments");
            for (Map<String, Object> enchant : enchantList){
                String enchantName = (String) enchant.get("name");
                int enchantLevel = (int) enchant.get("level");
                Integer enchantId = LuckyCrates.getEnchantmentByName(enchantName);
                if(enchantId == null){
                    this.getPlayer().sendMessage("§cError Enchantment not found : " + enchantName);
                    return null;
                }
                item.addEnchantment(Enchantment.getEnchantment(enchantId).setLevel(enchantLevel));
            }
        }
        return item;
    }
}
