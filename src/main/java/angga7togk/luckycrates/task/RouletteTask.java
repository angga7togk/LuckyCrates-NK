package angga7togk.luckycrates.task;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.crates.Crates;
import angga7togk.luckycrates.event.PlayerOpenCrateEvent;
import angga7togk.luckycrates.menu.ChestMenu;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import me.iwareq.fakeinventories.FakeInventory;

import java.util.*;

public class RouletteTask extends AsyncTask {
    private final FakeInventory inv;
    private final int round;
    @Getter
    private final Player player;
    private final List<Map<String, Object>> safeDrops = new ArrayList<>();
    private final String crateName;
    private final long speed;

    public RouletteTask(Player player, String crateName, FakeInventory inv, long speed) {
        this.crateName = crateName;
        this.player = player;
        this.speed = speed;
        this.round = LuckyCrates.getInstance().getConfig().getInt("crates.roulette.round", 25);
        ConfigSection crateSection = LuckyCrates.crates.getSection(crateName);
        this.inv = inv;
        this.inv.clearAll();
        this.inv.setItem(4, Item.get(208, 0, 1));
        this.inv.setItem(22, Item.get(208, 0, 1));

        List<Map<String, Object>> drops = crateSection.getList("drops");
        List<Map<String, Object>> cache = new ArrayList<>();
        drops.forEach((objectMap -> {
            int chance = Math.min((int) objectMap.get("chance"), 100);
            for (int i = 0; i <= chance * 2; i++) {
                cache.add(objectMap);
            }
        }));
        Collections.shuffle(cache);
        int i = 0;
        for (Map<String, Object> objectMap : cache){
            this.safeDrops.add(objectMap);
            if (i == 17){
                break;
            }
            i++;
        }
    }

    @Override
    public void onRun() {
        try {
            for (int i = 0; i <= round;i++){
                if (i < round) {
                    spinRoulette();
                } else {
                    this.getPlayer().getLevel().addSound(player.getPosition(), Sound.RANDOM_TOTEM, 1.0F, 1.0F, player);
                    Item item = this.inv.getItem(13);
                    setDisplayGift(item);
                }
                Thread.sleep(speed);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void spinRoulette() {
        Random random = new Random();
        Collections.shuffle(this.safeDrops);

        updateRouletteDisplay(random);
    }

    private void setDisplayGift(Item item) throws InterruptedException {
        for (int i = 10; i <= 16; i++) {
            this.inv.setItem(i, item);
        }
        for (int i = 25; i <= 34; i += 9) {
            this.inv.setItem(i, item);
        }
        for (int i = 43; i >= 37; i--) {
            this.inv.setItem(i, item);
        }
        for (int i = 28; i >= 19; i -= 9) {
            this.inv.setItem(i, item);
        }
        this.inv.setDefaultItemHandler((items, event) -> event.setCancelled());
        this.getPlayer().addWindow(this.inv);
        Thread.sleep(1000);
        this.setResult(item);
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
        this.getPlayer().getLevel().addSound(player.getPosition(), Sound.RANDOM_CLICK, 1.0F, 1.0F, player);
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

        if (drop.containsKey("broadcast")){
            addBroadcastToItem(item, drop.get("broadcast").toString().replace("{reward}", (customName == null) ? Objects.requireNonNull(item.getName()) : customName));
        }

        if(drop.containsKey("commands")){
            List<String> commands = (List<String>) drop.get("commands");
            addCommandToItem(item, commands);
        }
    }

    private void addBroadcastToItem(Item item, String msg){
        CompoundTag namedTag = (item.hasCompoundTag()) ? item.getNamedTag() : new CompoundTag();
        namedTag.putString("broadcast", msg.replace("{player}", player.getName()));
        item.setNamedTag(namedTag);
    }
    private String getBroadcastFromItem(Item item){
        CompoundTag namedTag = (item.hasCompoundTag()) ? item.getNamedTag() : new CompoundTag();
        if (namedTag.contains("broadcast")){
            String broadcast = namedTag.getString("broadcast");
            namedTag.remove("broadcast");
            return broadcast;
        }
        return null;
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
        if (commands == null || commands.isEmpty()) {
            return;
        }
        CompoundTag namedTag = (item.hasCompoundTag()) ? item.getNamedTag() : new CompoundTag();
        StringBuilder commandString = new StringBuilder();
        int lastIndex = commands.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            commandString.append(commands.get(i).replace("{player}", this.getPlayer().getName())).append(",");
        }
        commandString.append(commands.get(lastIndex).replace("{player}", this.getPlayer().getName()));

        item.setNamedTag(namedTag.putString("commands", commandString.toString()));
    }


    private List<String> getCommandsFromItem(Item item) {
        CompoundTag namedTag = (item.hasCompoundTag()) ? item.getNamedTag() : new CompoundTag();
        if (namedTag.contains("commands")) {
            List<String> commands = Arrays.asList(namedTag.getString("commands").split(","));

            namedTag.remove("commands");
            item.setNamedTag(namedTag);
            return commands;
        }
        return null;
    }

    @Override
    public void onCompletion(Server server) {
        Item item = (Item) this.getResult();
        if (item != null){
            player.getInventory().addItem(item);
            List<String> commands = getCommandsFromItem(item);
            if(commands != null){
                for (String command : commands){
                    Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command.replace("{player}", this.getPlayer().getName()));
                }
            }
            String broadcast = getBroadcastFromItem(item);
            if (broadcast != null) Server.getInstance().broadcastMessage(LuckyCrates.prefix + "§r" + broadcast);
            Server.getInstance().getPluginManager().callEvent(new PlayerOpenCrateEvent(player, item, crateName));
        }
        ChestMenu menu = new ChestMenu();
        menu.mainMenu(player, crateName);
    }
}
