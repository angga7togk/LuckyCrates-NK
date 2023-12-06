package angga7togk.luckycrates.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.util.UUID;

public class FloatingUtils {

    public static void removeEntity(long id, String levelName){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = id;

        for (Player p : Server.getInstance().getOnlinePlayers().values()){
            Level level = Server.getInstance().getLevelByName(levelName);
            if(level != null && p.getLevel() == level) p.dataPacket(pk);
        }
    }

    public static void createEntity(String name, long id, float x, float y, float z, String levelName)
    {
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.entityRuntimeId = id;
        pk.entityUniqueId = id;
        pk.uuid = UUID.randomUUID();
        pk.x = x;
        pk.y = y;
        pk.z = z;
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
