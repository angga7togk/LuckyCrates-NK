package angga7togk.luckycrates.command;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.crates.Crates;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

import java.util.Map;

public class SetCrates extends CratesCommand {

    public SetCrates(){
        super("setcrate", "set position LuckyCrates", "/setcrate <crate>", new String[]{"setcrates", "crateset"});
        this.setPermission("luckycrates.command.setcrate");
    }

    @Override
    public void execute(CommandSender sender, Map<String, String> lang, String[] args) {
        if(this.testPermission(sender)){
            if(sender instanceof Player player){
                if(args.length < 1){
                    player.sendMessage(LuckyCrates.prefix + getUsage());
                    return;
                }
                String crate = args[0];
                Crates crates = new Crates();
                if(!crates.crateExists(crate)){
                    player.sendMessage(LuckyCrates.prefix + lang.get("crate-notfound"));
                    return;
                }

                LuckyCrates.setMode.put(player, crate);
                player.sendMessage(LuckyCrates.prefix + lang.get("setmode"));
            }
        }
    }
}
