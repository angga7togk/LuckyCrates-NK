package angga7togk.luckycrates.event;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;
import lombok.Getter;

public class PlayerOpenCrateEvent extends Event {

    @Getter
    public static HandlerList handlers = new HandlerList();
    @Getter
    public Player player;
    @Getter
    public Item item;
    @Getter
    public String crateName;
    public PlayerOpenCrateEvent(Player player, Item item, String crateName){
        this.player = player;
        this.item = item;
        this.crateName = crateName;
    }
}
