package angga7togk.luckycrates.menu;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.crates.Keys;
import angga7togk.luckycrates.language.Languages;
import angga7togk.luckycrates.task.RouletteTask;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import me.iwareq.fakeinventories.FakeInventory;

import java.util.List;
import java.util.Map;

public class ChestMenu {

    private final Map<String, String> lang;
    public ChestMenu(){
        this.lang = new Languages().getLanguage();
    }

    public void mainMenu(Player player, String crateName){
        LuckyCrates.crates.reload();
        FakeInventory inv = new FakeInventory(InventoryType.DOUBLE_CHEST,TextFormat.BOLD + crateName);
        if(!LuckyCrates.crates.exists(crateName)){
            player.sendMessage(LuckyCrates.prefix + lang.get("crate-notfound"));
            return;
        }
        ConfigSection crateSect = LuckyCrates.crates.getSection(crateName);
        List<Map<String, Object>> dropsList = crateSect.getList("drops");
        int i = 0;
        for (Map<String, Object> drop : dropsList){
            int id = (int) drop.get("id");
            int meta = (int) drop.get("meta");
            int amount = (int) drop.get("amount");
            int chance = (int) drop.get("chance");
            String customName = drop.containsKey("name") ? (String) drop.get("name") : null;
            String lore = drop.containsKey("lore") ? (String) drop.get("lore") : null;
            Item item = new Item(id, meta, amount);
            if(customName != null){
                item.setCustomName(customName);
            }
            if(lore != null){
                item.setLore(lore, "" ,"§eChance, §r" + chance);
            }else{
                item.setLore("", "§eChance, §r" + chance);
            }
            if(drop.containsKey("enchantments")){
                List<Map<String, Object>> enchantList = (List<Map<String, Object>>) drop.get("enchantments");
                for (Map<String, Object> enchant : enchantList){
                    String enchantName = (String) enchant.get("name");
                    int enchantLevel = (int) enchant.get("level");
                    if(Enchantment.getEnchantment(enchantName) == null){
                        player.sendMessage("§cError Enchantment not found : " + enchantName);
                        return;
                    }
                    item.addEnchantment(Enchantment.getEnchantment(enchantName).setLevel(enchantLevel));
                }
            }
            inv.addItem(item);
            i++;
            if(i > 35)break;
        }
        inv.setItem(49, new Item(130, 0, 1)
                .setCustomName("§l§eOpen Crates")
                .setLore("", "§eNeed Key, §r" + crateSect.getInt("amount") ,"§eMy Key, §r" + getKeysCount(player)));

        inv.setDefaultItemHandler((item, event) -> {
            event.setCancelled();

            Player target = event.getTransaction().getSource();
            int myKey = getKeysCount(target);
            int needKey = crateSect.getInt("amount");
            if(myKey >= needKey){
                reduceKey(target, needKey);
                Server.getInstance().getScheduler().scheduleRepeatingTask(new RouletteTask(target, crateName), 5);
            }

        });

        player.addWindow(inv);
    }

    public int getKeysCount(Player player) {
        Keys keys = new Keys();
        int count = 0;
        for (Item invItem : player.getInventory().getContents().values()) {
            if (keys.isKeys(invItem)) {
                count += invItem.getCount();
            }
        }
        return count;
    }

    public void reduceKey(Player player, int amount) {
        Inventory inv = player.getInventory();
        Keys keys = new Keys();
        for (int slot : inv.getContents().keySet()) {
            Item invItem = inv.getItem(slot);
            if (keys.isKeys(invItem)) {
                int itemCount = invItem.getCount();
                if (itemCount <= amount) {
                    inv.clear(slot);
                    amount -= itemCount;
                } else {
                    invItem.setCount(itemCount - amount);
                    inv.setItem(slot, invItem);
                    break;
                }
            }
        }
    }
}
