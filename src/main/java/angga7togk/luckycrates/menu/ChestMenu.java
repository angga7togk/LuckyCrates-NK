package angga7togk.luckycrates.menu;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.language.Languages;
import cn.nukkit.Player;
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
            String customName = drop.containsKey("name") ? (String) drop.get("name") : null;
            String lore = drop.containsKey("lore") ? (String) drop.get("lore") : null;
            Item item = new Item(id, meta, amount);
            if(customName != null){
                item.setCustomName(customName);
            }
            if(lore != null){
                item.setLore(lore, "Chance, 50(static)");
            }else{
                item.setLore("Chance, 50(static)");
            }
            if(drop.containsKey("enchantments")){
                List<Map<String, Object>> enchantList = (List<Map<String, Object>>) drop.get("enchantments");
                for (Map<String, Object> enchant : enchantList){
                    String enchantName = (String) enchant.get("name");
                    int enchantLevel = (int) enchant.get("level");
                    item.addEnchantment(Enchantment.getEnchantment(enchantName).setLevel(enchantLevel));
                }
            }
            inv.addItem(item);
            i++;
            if(i > 35)break;
        }
        inv.setItem(49, new Item(130, 0, 1).setCustomName("§l§eOpen Crates"));

        inv.setDefaultItemHandler((item, event) -> {
            event.setCancelled();
        });

        player.addWindow(inv);
    }
}
