package angga7togk.luckycrates.crates;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.menu.ChestMenu;
import angga7togk.luckycrates.utils.FloatingUtils;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;

import java.rmi.dgc.Lease;
import java.util.*;

public class Crates extends Keys {
    double offsetX = 0.5;
    double offsetY = 0.7;
    double offsetZ = 0.5;
    public Crates() {
        super();
    }

    public  boolean isCrates(Block block) {
        Location location = block.getLocation();
        double x = location.getX() + offsetX;
        double y = location.getY() + offsetY;
        double z = location.getZ() + offsetZ;
        String level = location.getLevel().getName();

        for (String idEntity : LuckyCrates.pos.getSection("crates").getKeys(false)){
            double posX = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.x");
            double posY = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.y");
            double posZ = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.z");
            String posLevel = LuckyCrates.pos.getString("crates." + idEntity + ".pos.level");

            if (level.equalsIgnoreCase(posLevel) && Double.valueOf(x).equals(posX) && Double.valueOf(y).equals(posY
            ) && Double.valueOf(z).equals(posZ)) {
                return true;
            }
        }
        return false;
    }

    public void previewCrates(Player player, Block block) {
        if (isCrates(block)) {
            String crateName = getCrateName(block);
            if (crateName != null) {
                ChestMenu menu = new ChestMenu();
                menu.mainMenu(player, crateName);
            }
        }
    }

    public void breakCrates(Block block) {
        if (isCrates(block)) {
            String idEntity = getCrateIdEntity(block);
            if (idEntity != null){
                FloatingUtils.removeEntity(idEntity);
                LuckyCrates.pos.getSection("crates").remove(idEntity);
                LuckyCrates.pos.save();
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

            for (String idEntity : LuckyCrates.pos.getSection("crates").getKeys(false)){
                double posX = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.x");
                double posY = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.y");
                double posZ = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.z");
                String posLevel = LuckyCrates.pos.getString("crates." + idEntity + ".pos.level");

                if (level.equalsIgnoreCase(posLevel) && Double.valueOf(x).equals(posX) && Double.valueOf(y).equals(posY
                ) && Double.valueOf(z).equals(posZ)) {
                    return LuckyCrates.pos.getString("crates." + idEntity + ".crate");
                }
            }
        }
        return null;
    }

    public String getCrateIdEntity(Block block) {
        if (isCrates(block)){
            Location location = block.getLocation();
            double x = location.getX() + offsetX;
            double y = location.getY() + offsetY;
            double z = location.getZ() + offsetZ;
            String level = location.getLevel().getName();

            for (String idEntity : LuckyCrates.pos.getSection("crates").getKeys(false)){
                double posX = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.x");
                double posY = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.y");
                double posZ = LuckyCrates.pos.getDouble("crates." + idEntity + ".pos.z");
                String posLevel = LuckyCrates.pos.getString("crates." + idEntity + ".pos.level");

                if (level.equalsIgnoreCase(posLevel) && Double.valueOf(x).equals(posX) && Double.valueOf(y).equals(posY
                ) && Double.valueOf(z).equals(posZ)) {
                    return idEntity;
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
                Level level = location.getLevel();
                String idEntity = UUID.randomUUID().toString();


                LuckyCrates.pos.set("crates." + idEntity + ".id", idEntity);
                LuckyCrates.pos.set("crates." + idEntity + ".crate", crateName);

                LuckyCrates.pos.set("crates." + idEntity + ".pos.x", x);
                LuckyCrates.pos.set("crates." + idEntity + ".pos.y", y);
                LuckyCrates.pos.set("crates." + idEntity + ".pos.z", z);
                LuckyCrates.pos.set("crates." + idEntity + ".pos.level", level.getName());

                LuckyCrates.pos.save();

                String displayName = LuckyCrates.crates.getString(crateName + ".floating-text");
                FloatingUtils.createEntity(displayName, idEntity, new Position(x, y, z, level));
                return true;
            }
        }
        return false;
    }


}
