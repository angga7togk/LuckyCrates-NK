/*
 * RRRRRR                         jjj
 * RR   RR   eee    eee               pp pp
 * RRRRRR  ee   e ee   e _____    jjj ppp  pp
 * RR  RR  eeeee  eeeee           jjj pppppp
 * RR   RR  eeeee  eeeee          jjj pp
 *                              jjjj  pp
 *
 * Copyright (c) 2020. Ree-jp.  All Rights Reserved.
 */

package angga7togk.luckycrates.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class LuckyCratesEntity extends Entity {

    @Override
    public int getNetworkId() {
        return 63;
    }

    public LuckyCratesEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
}