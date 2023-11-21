package angga7togk.luckycrates.task;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import me.iwareq.fakeinventories.FakeInventory;

import java.util.*;

public class RouletteTask extends Task {
    private final FakeInventory inv;
    private int currentTick = 0;
    private final Player player;
    private final List<Map<String, Object>> safeDrops = new ArrayList<>();

    public RouletteTask(Player player, String crateName) {
        this.player = player;
        ConfigSection crateSection = LuckyCrates.crates.getSection(crateName);
        this.inv = new FakeInventory(InventoryType.DOUBLE_CHEST, TextFormat.BOLD + crateName);
        this.inv.setItem(4, Item.get(198, 0, 1));
        this.inv.setItem(22, Item.get(198, 0, 1));

        List<Map<String, Object>> drops = crateSection.getList("drops");
        drops.forEach((objectMap -> {
            int chance = Math.min((int) objectMap.get("chance"), 100);
            for (int i = 0; i <= chance; i++) {
                this.safeDrops.add(objectMap);
            }
        }));
    }

    @Override
    public void onRun(int i) {
        currentTick++;
        if (currentTick <= 20) {
            spinRoulette();
        } else {
            stopRoulette();
        }
    }

    private void spinRoulette() {
        Random random = new Random();
        Collections.shuffle(this.safeDrops);

        updateRouletteDisplay(random);

        // Give the player the item when the roulette stops (after a certain number of ticks)
        if (currentTick == 20) {
            Item item = this.inv.getItem(13);
            player.getInventory().addItem(item);
        }
    }

    private void updateRouletteDisplay(Random random) {
        for (int i = 10; i <= 16; i++) {
            setRouletteItem(random, i);
        }
        for (int i = 25; i <= 34; i += 9) {
            setRouletteItem(random, i);
        }
        for (int i = 43; i >= 37; i--) {
            setRouletteItem(random, i);
        }
        for (int i = 28; i >= 19; i -= 9) {
            setRouletteItem(random, i);
        }
        this.inv.setDefaultItemHandler((item, event) -> event.setCancelled());
        this.player.getLevel().addSound(player.getPosition(), Sound.RANDOM_CLICK);
        this.player.addWindow(this.inv);
    }

    private void setRouletteItem(Random random, int slot) {
        int randomIndex = random.nextInt(this.safeDrops.size());
        Map<String, Object> drop = this.safeDrops.get(randomIndex);

        Item item = createItemFromDrop(drop);
        this.inv.setItem(slot, item);
    }

    private Item createItemFromDrop(Map<String, Object> drop) {
        int id = (int) drop.get("id");
        int meta = (int) drop.get("meta");
        int amount = (int) drop.get("amount");

        Item item = new Item(id, meta, amount);

        setItemDetails(drop, item);

        return item;
    }

    private void setItemDetails(Map<String, Object> drop, Item item) {
        String customName = drop.containsKey("name") ? (String) drop.get("name") : null;
        String lore = drop.containsKey("lore") ? (String) drop.get("lore") : null;

        if (customName != null) {
            item.setCustomName(customName);
        }

        int chance = (int) drop.get("chance");
        if(lore != null){
            item.setLore(lore, "" ,"§eChance, §r" + chance);
        }else{
            item.setLore("", "§eChance, §r" + chance);
        }

        if (drop.containsKey("enchantments")) {
            List<Map<String, Object>> enchantList = (List<Map<String, Object>>) drop.get("enchantments");
            for (Map<String, Object> enchant : enchantList) {
                addEnchantmentToItem(item, enchant);
            }
        }
    }
    private void addEnchantmentToItem(Item item, Map<String, Object> enchant) {
        String enchantName = (String) enchant.get("name");
        int enchantLevel = (int) enchant.get("level");

        Enchantment enchantment = Enchantment.getEnchantment(enchantName);

        if (enchantment == null) {
            player.sendMessage("§cError Enchantment not found : " + enchantName);
            return;
        }

        item.addEnchantment(enchantment.setLevel(enchantLevel));
    }

    private void stopRoulette() {
        this.player.removeWindow(this.inv);
        this.cancel();
    }
}
