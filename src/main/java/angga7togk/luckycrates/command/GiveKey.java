package angga7togk.luckycrates.command;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.crates.Keys;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;

import java.util.Map;

public class GiveKey extends CratesCommand{
    public GiveKey() {
        super("key", "give key to player", "/key <crate> <amount> <player>", new String[]{"givekey"});
        this.setPermission("luckycrates.command.key");
    }

    @Override
    public void execute(CommandSender sender, Map<String, String> lang, String[] args) {
        if(this.testPermission(sender)){
            if(args.length < 3){
                sender.sendMessage(LuckyCrates.prefix + getUsage());
                return;
            }

            String crate = args[0];
            String target = args[2];
            Keys keys = new Keys();
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            }catch (NumberFormatException e){
                sender.sendMessage(LuckyCrates.prefix + lang.get("amount-number"));
                return;
            }
            if(!keys.crateExists(crate)){
                sender.sendMessage(LuckyCrates.prefix + lang.get("crate-notfound"));
                return;
            }
            Player targetPlayer = Server.getInstance().getPlayer(target);
            if(targetPlayer == null){
                sender.sendMessage(LuckyCrates.prefix + lang.get("player-notfound"));
                return;
            }
            if(keys.giveKey(targetPlayer, crate, amount)){
                sender.sendMessage(LuckyCrates.prefix + lang.get("give-key")
                        .replace("{crate}", crate)
                        .replace("{amount}", String.valueOf(amount))
                        .replace("{player}", targetPlayer.getName()));
                targetPlayer.sendMessage(LuckyCrates.prefix + lang.get("accept-key")
                        .replace("{crate}", crate)
                        .replace("{amount}", String.valueOf(amount)));
            }
        }
    }
}
