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
        String loc = location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getLevelName();
        return LuckyCrates.pos.exists(loc);
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
            String loc = location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getLevelName();
            LuckyCrates.pos.remove(loc);
            LuckyCrates.pos.save();
            LuckyCrates.pos.reload();
        }
    }

    public String getCrateName(Block block){
        if(isCrates(block)){
            Location location = block.getLocation();
            String loc = location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getLevelName();
            return LuckyCrates.pos.getString(loc);
        }
        return null;
    }

    public boolean setCrate(Block block, String crateName){
        if(crateExists(crateName)){
            Location location = block.getLocation();
            String loc = location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getLevelName();
            LuckyCrates.pos.set(loc, crateName);
            LuckyCrates.pos.save();
            LuckyCrates.pos.reload();
        }
        return false;
    }

}
