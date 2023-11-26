package angga7togk.luckycrates.task;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import me.iwareq.fakeinventories.FakeInventory;

import java.util.*;

public class RouletteTask extends Task {
    private final FakeInventory inv;
    private int currentTick = 0;
    private int round;
    @Getter
    private final Player player;
    private final List<Map<String, Object>> safeDrops = new ArrayList<>();

    public RouletteTask(Player player, String crateName) {
        this.player = player;
        this.round = LuckyCrates.getInstance().getConfig().getInt("crates.roulette.round", 25);
        ConfigSection crateSection = LuckyCrates.crates.getSection(crateName);
        this.inv = new FakeInventory(InventoryType.DOUBLE_CHEST, TextFormat.BOLD + crateName);
        this.inv.setItem(4, Item.get(208, 0, 1));
        this.inv.setItem(22, Item.get(208, 0, 1));

        List<Map<String, Object>> drops = crateSection.getList("drops");
        drops.forEach((objectMap -> {
            int chance = Math.min((int) objectMap.get("chance"), 100);
            for (int i = 0; i <= ((chance > 30) ? chance * 2 : chance); i++) {
                this.safeDrops.add(objectMap);
            }
        }));
    }

    @Override
    public void onRun(int i) {
        currentTick++;
        if (currentTick <= round) {
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
        if (currentTick == round) {
            Item item = this.inv.getItem(13);
            player.getInventory().addItem(item);
            List<String> commands = getCommandsFromItem(item);
            if(commands != null){
                for (String command : commands){
                    Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command.replace("{player}", this.getPlayer().getName()));
                }
            }
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
        this.getPlayer().getLevel().addSound(player.getPosition(), Sound.RANDOM_CLICK);
        this.getPlayer().addWindow(this.inv);
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
        if(lore != null){
            item.setLore(lore);
        }

        if (drop.containsKey("enchantments")) {
            List<Map<String, Object>> enchantList = (List<Map<String, Object>>) drop.get("enchantments");
            for (Map<String, Object> enchant : enchantList) {
                addEnchantmentToItem(item, enchant);
            }
        }
        if(drop.containsKey("commands")){
            List<String> commands = (List<String>) drop.get("commands");
            addCommandToItem(item, commands);
        }
    }
    private void addEnchantmentToItem(Item item, Map<String, Object> enchant) {
        String enchantName = (String) enchant.get("name");
        int enchantLevel = (int) enchant.get("level");
        Integer enchantId = LuckyCrates.getEnchantmentByName(enchantName);

        if (enchantId == null) {
            player.sendMessage("§cError Enchantment not found : " + enchantName);
            return;
        }
        Enchantment enchantment = Enchantment.getEnchantment(enchantId);
        item.addEnchantment(enchantment.setLevel(enchantLevel));
    }

    private void addCommandToItem(Item item, List<String> commands) {
        CompoundTag namedTag = (item.getNamedTag() != null) ? item.getNamedTag() : new CompoundTag();
        ListTag<StringTag> commandListTag = new ListTag<>();
        for (String command : commands) {
            commandListTag.add(new StringTag("", command.replace("{player}", this.getPlayer().getName())));
        }
        namedTag.putList(commandListTag);
        item.setNamedTag(namedTag);
    }

    private List<String> getCommandsFromItem(Item item) {
        CompoundTag namedTag = item.getNamedTag();
        if (namedTag != null && namedTag.contains("commands")) {
            ListTag<StringTag> commandListTag = namedTag.getList("commands", StringTag.class);
            List<String> commands = new ArrayList<>();
            for (StringTag stringTag : commandListTag.getAll()) {
                commands.add(stringTag.data);
            }
            namedTag.remove("commands");
            return commands;
        }
        return null;
    }

    private void stopRoulette() {
        this.getPlayer().removeWindow(this.inv);
        this.cancel();
    }
}
