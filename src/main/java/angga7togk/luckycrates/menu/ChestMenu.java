package angga7togk.luckycrates.menu;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.language.Languages;
import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import me.iwareq.fakeinventories.FakeInventory;

import java.util.List;
import java.util.Map;

public class ChestMenu {

    private final Map<String, String> lang;
    public ChestMenu(){
        this.lang = new Languages().getLanguage();
    }

    public void mainMenu(Player player, String crateName){
        FakeInventory inv = new FakeInventory(InventoryType.DOUBLE_CHEST,TextFormat.BOLD + crateName);
        if(!LuckyCrates.crates.exists(crateName)){
            player.sendMessage(LuckyCrates.prefix + lang.get("crate-notfound"));
            return;
        }
        ConfigSection crateSect = LuckyCrates.crates.getSection(crateName);
        List<Map<String, Object>> dropsList = crateSect.getList("drops");
        int i = 0;
        for (Map<String, Object> drop : dropsList){
            
            if(i > 34)break;
            i++;
        }
    }
}
