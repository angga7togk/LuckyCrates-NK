package angga7togk.luckycrates.utils;

import angga7togk.luckycrates.LuckyCrates;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class FloatingUtils {

    public static void removeEntity(String id){
        if (LuckyCrates.particles.containsKey(id)){
            LuckyCrates.particles.get(id).close();
            LuckyCrates.particles.remove(id);
        }
    }

    public static void createEntity(String name, String idEntity, Position pos)
    {
        Entity entity = LuckyCrates.particles.getOrDefault(idEntity, Entity.createEntity("LuckyCrates", pos));
        if (entity == null){
            Server.getInstance().getLogger().info("Entity Null");
            return;
        }
        entity.setScale(0f);
        entity.setNameTag(name);
        entity.setNameTagVisible();
        entity.setNameTagAlwaysVisible();
        entity.spawnToAll();
        if(!LuckyCrates.particles.containsKey(idEntity)) LuckyCrates.particles.put(idEntity, entity);
    }
}
