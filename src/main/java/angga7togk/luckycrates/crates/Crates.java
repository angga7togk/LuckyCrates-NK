package angga7togk.luckycrates.crates;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.menu.ChestMenu;
import angga7togk.luckycrates.utils.FloatingUtils;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;

import java.util.*;

public class Crates extends Keys {
    double offsetX = 0.5;
    double offsetY = 0.7;
    double offsetZ = 0.5;
    public Crates() {
        super();
    }

    public boolean isCrates(Block block) {
        Location location = block.getLocation();
        double x = location.getX() + offsetX;
        double y = location.getY() + offsetY;
        double z = location.getZ() + offsetZ;
        String level = location.getLevel().getName();

        for (String crateName : LuckyCrates.pos.getSection("crates").getKeys(false)){
            double posX = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.x");
            double posY = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.y");
            double posZ = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.z");
            String posLevel = LuckyCrates.pos.getString("crates." + crateName + ".pos.level");

            if (level.equalsIgnoreCase(posLevel) && Double.valueOf(x).equals(posX) && Double.valueOf(y).equals(posY
            ) && Double.valueOf(z).equals(posZ)) {
                return true;
            }
        }
        return false;
    }

    public void previewCrates(Player player, Block block) {
        if (isCrates(block)) {
            if (getCrateName(block) != null) {
                ChestMenu menu = new ChestMenu();
                menu.mainMenu(player, getCrateName(block));
            }
        }
    }

    public void breakCrates(Block block) {
        if (isCrates(block)) {
            String crateName = getCrateName(block);
            if (crateName != null){
                String levelName = LuckyCrates.pos.getString("crates." + crateName + ".pos.level");
                long id = LuckyCrates.pos.getLong("crates." + crateName + ".id");

                FloatingUtils.removeEntity(id, levelName);
                LuckyCrates.pos.remove(crateName);
                LuckyCrates.pos.save();
                LuckyCrates.pos.reload();
            }
        }
    }

    public String getCrateName(Block block) {
        if (isCrates(block)){
            Location location = block.getLocation();
            double x = location.getX() + offsetX;
            double y = location.getY() + offsetY;
            double z = location.getZ() + offsetZ;
            String level = location.getLevel().getName();

            for (String crateName : LuckyCrates.pos.getSection("crates").getKeys(false)){
                double posX = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.x");
                double posY = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.y");
                double posZ = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.z");
                String posLevel = LuckyCrates.pos.getString("crates." + crateName + ".pos.level");

                if (level.equalsIgnoreCase(posLevel) && Double.valueOf(x).equals(posX) && Double.valueOf(y).equals(posY
                ) && Double.valueOf(z).equals(posZ)) {
                    return crateName;
                }
            }
        }
        return null;
    }

    public boolean setCrate(Block block, String crateName) {
        if (crateExists(crateName)) {
            if (!isCrates(block)) {
                Location location = block.getLocation();
                double x = location.getX() + offsetX;
                double y = location.getY() + offsetY;
                double z = location.getZ() + offsetZ;
                String level = location.getLevel().getName();
                long idEntity = (2000 + LuckyCrates.offsetIdEntity++);


                LuckyCrates.pos.set("crates." + crateName + ".id", idEntity);
                LuckyCrates.pos.set("crates." + crateName + ".crate", crateName);

                LuckyCrates.pos.set("crates." + crateName + ".pos.x", x);
                LuckyCrates.pos.set("crates." + crateName + ".pos.y", y);
                LuckyCrates.pos.set("crates." + crateName + ".pos.z", z);
                LuckyCrates.pos.set("crates." + crateName + ".pos.level", level);

                LuckyCrates.pos.save();
                LuckyCrates.pos.reload();

                String floating = LuckyCrates.crates.getString(crateName + ".floating-text");
                FloatingUtils.createEntity(floating, idEntity, (float) x, (float) y, (float) z, level);
                return true;
            }
        }
        return false;
    }


}
