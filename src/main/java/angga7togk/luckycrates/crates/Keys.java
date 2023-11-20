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
        CompoundTag nameTag = item.hasCompoundTag() ? item.getNamedTag() : null;
        
        return id == LuckyCrates.cfg.getInt("keys.id", 131)
                && meta == LuckyCrates.cfg.getInt("keys.meta", 0)
                && nameTag != null
                && nameTag.contains("isKeys");
    }

    public boolean crateExists(String crateName) {
        return LuckyCrates.crates.exists(crateName);
    }

    public boolean giveKey(Player player, String crateName, int amount){
        if (crateExists(crateName)){

            String customName = getName().replace("{crate}", crateName);
            String lore = getLore().replace("{crate}", crateName);
            Item item = new Item(getId(), getMeta(), amount)
                    .setCustomName(customName)
                    .setLore(lore)
                    .setNamedTag(new CompoundTag()
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