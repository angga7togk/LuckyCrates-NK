package angga7togk.luckycrates.crates;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.menu.ChestMenu;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;

public class Crates extends Keys{

    public Crates(){
        super();
    }

    public boolean isCrates(Block block){
        Location location = block.getLocation();
        return LuckyCrates.pos.exists(getLocationString(location));
    }

    public void previewCrates(Player player, Block block){
        if(isCrates(block)){
            if(getCrateName(block) != null){
                ChestMenu menu = new ChestMenu();
                menu.mainMenu(player, getCrateName(block));
            }
        }
    }

    public void breakCrates(Block block){
        if(isCrates(block)){
            Location location = block.getLocation();
            LuckyCrates.pos.remove(getLocationString(location));
            LuckyCrates.pos.save();
            LuckyCrates.pos.reload();
        }
    }

    public String getCrateName(Block block){
        if(isCrates(block)){
            Location location = block.getLocation();
            return LuckyCrates.pos.getString(getLocationString(location));
        }
        return null;
    }

    public boolean setCrate(Block block, String crateName){
        if(crateExists(crateName)){
            Location location = block.getLocation();
            LuckyCrates.pos.set(getLocationString(location), crateName);
            LuckyCrates.pos.save();
            LuckyCrates.pos.reload();
            return true;
        }
        return false;
    }

    public String getLocationString(Location location) {
        return location.getFloorX() + ":" + location.getFloorY() + ":" + location.getFloorZ() + ":" + location.getLevelName();
    }

}
