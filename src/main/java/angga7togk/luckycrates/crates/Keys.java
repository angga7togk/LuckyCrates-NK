package angga7togk.luckycrates.crates;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;

public class Keys{
    private final int id;
    private final int meta;
    private final String name;
    private final String lore;
    public Keys(){
        this.id = LuckyCrates.getInstance().getConfig().getInt("keys.id", 131);
        this.meta = LuckyCrates.getInstance().getConfig().getInt("keys.meta", 0);
        this.name = LuckyCrates.getInstance().getConfig().getString("keys.name", "{crate} Key");
        this.lore = LuckyCrates.getInstance().getConfig().getString("keys.lore", "Claim rewards from a {crate} Crate");
    }

    public boolean isKeys(Item item) {
        int id = item.getId();
        int meta = item.getDamage();
        CompoundTag nameTag = item.hasCompoundTag() ? item.getNamedTag() : null;
        
        return id == LuckyCrates.getInstance().getConfig().getInt("keys.id", 131)
                && meta == LuckyCrates.getInstance().getConfig().getInt("keys.meta", 0)
                && nameTag != null
                && nameTag.contains("isKeys");
    }

    public String getCrateName(Item item){
        if(isKeys(item)){
            return item.getNamedTag().getString("crateName");
        }
        return null;
    }

    public boolean crateExists(String crateName) {
        return LuckyCrates.crates.exists(crateName);
    }

    public boolean giveKey(Player player, String crateName, int amount){
        boolean isVirtual = LuckyCrates.getInstance().getConfig().getString("type-key", "item").equalsIgnoreCase("virtual");
        if (crateExists(crateName)){
            if (isVirtual){
                int myKeys = LuckyCrates.keys.getInt(player.getName().toLowerCase() + "." + crateName, 0);
                LuckyCrates.keys.set(player.getName().toLowerCase() + "." + crateName , myKeys + amount);
                LuckyCrates.keys.save();
                return true;
            }else{
                String customName = this.name.replace("{crate}", crateName);
                String lore = this.lore.replace("{crate}", crateName);
                Item item = new Item(this.id, this.meta, amount)
                        .setNamedTag(new CompoundTag()
                                .putBoolean("isKeys", true)
                                .putString("crateName", crateName));
                item.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_BINDING_CURSE));
                if (isKeys(item)) {
                    player.getInventory().addItem(item.setCustomName(customName).setLore(lore));
                    return true;
                }

            }
        }
        return false;
    }
}