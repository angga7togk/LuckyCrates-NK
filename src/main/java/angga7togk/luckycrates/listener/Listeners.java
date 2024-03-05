package angga7togk.luckycrates.listener;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.crates.Crates;
import angga7togk.luckycrates.language.Languages;
import angga7togk.luckycrates.utils.FloatingUtils;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Objects;

public class Listeners implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        LuckyCrates.setMode.remove(player);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Crates crates = new Crates();
        Map<String, String> lang = new Languages().getLanguage();
        if(LuckyCrates.setMode.containsKey(player)){
            event.setCancelled();
            String crate = LuckyCrates.setMode.get(player);
            if(crates.setCrate(block, crate)){
                player.sendMessage(LuckyCrates.prefix + lang.get("setted-crate")
                        .replace("{crate}", crate));
            }
            LuckyCrates.setMode.remove(player);
        }else{
            if(player.hasPermission("break.crates")){
                if(crates.isCrates(block)){
                    player.sendMessage(LuckyCrates.prefix + lang.get("break-crate")
                            .replace("{crate}", crates.getCrateName(block)));
                    crates.breakCrates(block);
                }
            }
        }
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Crates crates = new Crates();
//        Map<String, String> lang = new Languages().getLanguage();
        if (Objects.requireNonNull(event.getAction()) == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            assert block != null;
            if(crates.isCrates(block)){
                event.setCancelled();
                crates.previewCrates(player, block);
            }
        }
    }
}
