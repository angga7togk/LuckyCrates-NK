package angga7togk.luckycrates.crates;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import lombok.Getter;

@Getter
public class Keys{
    private final int id;
    private final int meta;
    private final String name;
    private final String lore;
    public Keys(){
        this.id = LuckyCrates.cfg.getInt("keys.id", 131);
        this.meta = LuckyCrates.cfg.getInt("keys.meta", 0);
        this.name = LuckyCrates.cfg.getString("keys.name", "{crate} Key");
        this.lore = LuckyCrates.cfg.getString("keys.lore", "Claim rewards from a {crate} Crate");
    }

    public boolean isKeys(Item item) {
        int id = item.getId();
        int meta = item.getDamage();
        String name = item.hasCustomName() ? item.getCustomName() : null;
        String lore = (item.getLore().length > 0) ? item.getLore()[0] : null;
        CompoundTag nameTag = item.hasCompoundTag() ? item.getNamedTag() : null;
        
        return id == LuckyCrates.cfg.getInt("keys.id", 131)
                && meta == LuckyCrates.cfg.getInt("keys.meta", 0)
                && name != null && lore != null && nameTag != null
                && item.hasEnchantment(Enchantment.ID_BINDING_CURSE)
                && nameTag.contains("isKeys");
    }

    public boolean crateExists(String crateName) {
        return LuckyCrates.crates.exists(crateName);
    }

    public boolean giveKey(Player player, String crateName, int amount){
        if (crateExists(crateName)){
            Item item = new Item(getId(), getMeta(), amount);
            item.setCustomName(getName().replace("{crate}", crateName));
            item.setLore(getLore().replace("{crate}", crateName));
            item.setCount(amount);
            item.setNamedTag(new CompoundTag()
                    .putBoolean("isKeys", true)
                    .putString("crateName", crateName));
            item.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_BINDING_CURSE));
            if(isKeys(item)){
                player.getInventory().addItem(item);
                return true;
            }
        }
        return false;
    }
}