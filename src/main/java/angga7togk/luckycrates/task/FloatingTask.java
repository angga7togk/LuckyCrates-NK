package angga7togk.luckycrates.task;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.network.protocol.AddPlayerPacket;

import java.util.UUID;


public class FloatingTask implements Runnable{

    @Override
    public void run() {
        int id = 0;
        for (String loc : LuckyCrates.pos.getKeys(false)){
            String[] location = loc.split(":");
            String crate = LuckyCrates.pos.getString(loc);
            String name = LuckyCrates.crates.getString(crate + ".floating-text");
            double x = Double.parseDouble(location[0]);
            double y = Double.parseDouble(location[1]);
            double z = Double.parseDouble(location[2]);
            String levelName = location[3];
            createAndSendPacket(name, 2000 + id++, x, y + 2, z, levelName);
        }
    }


    private void createAndSendPacket(String name, long id, double x, double y, double z, String levelName)
    {
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.entityRuntimeId = id;
        pk.entityUniqueId = id;
        pk.uuid = UUID.randomUUID();
        pk.x = (float) x;
        pk.y = (float) y;
        pk.z = (float) z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        long flags = (
                (1L << Entity.DATA_FLAG_IMMOBILE)
        );
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS, flags)
                .putLong(Entity.DATA_LEAD_HOLDER_EID,-1)
                .putFloat(Entity.DATA_SCALE, 0.0001f);
        pk.item = Item.get(Item.AIR);
        pk.username = name;
        for (Player p : Server.getInstance().getOnlinePlayers().values()){
            Level level = Server.getInstance().getLevelByName(levelName);
            if(level != null && p.getLevel() == level) p.dataPacket(pk);
        }

    }
}
