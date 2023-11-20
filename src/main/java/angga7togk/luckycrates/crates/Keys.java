package angga7togk.luckycrates.crates;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import lombok.Getter;

import java.util.Objects;

public abstract class Keys implements Crates{

    private final int id;
    private final int meta;
    private final CompoundTag nameTag;

    @Getter
    private Item key;

    public Keys(Item item){
        this.id = item.getId();
        this.meta = item.getDamage();
        this.nameTag = item.hasCompoundTag() ? item.getNamedTag() : null;
        this.key = item;
    }

    @Override
    public boolean isKeys() {
        return id == LuckyCrates.cfg.getInt("keys.id", 131)
                &&
                meta == LuckyCrates.cfg.getInt("keys.id", 0)
                &&
                nameTag != null
                &&
                nameTag.contains("isKeys");
    }

    @Override
    public boolean crateExists(String crateName) {
        return LuckyCrates.crates.exists(crateName);
    }

    public boolean giveKey(Player player, String crateName, int amount){
        if(isKeys()){
            if (crateExists(crateName)){
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putBoolean("isKeys", true);
                compoundTag.putString("crateName", crateName);

                getKey().setCount(amount);
                getKey().setNamedTag(compoundTag);
                player.getInventory().addItem(getKey());
                return true;
            }
        }
        return false;
    }
}