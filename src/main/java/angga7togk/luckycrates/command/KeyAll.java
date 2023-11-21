package angga7togk.luckycrates.command;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.crates.Keys;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;

import java.util.Map;

public class KeyAll extends CratesCommand{
    public KeyAll() {
        super("keyall", "give key to player online", "/keyall <crate> <amount>", new String[]{});
        this.setPermission("luckycrates.command.keyall");
    }

    @Override
    public void execute(CommandSender sender, Map<String, String> lang, String[] args) {
        if(this.testPermission(sender)){
            if(args.length < 2){
                sender.sendMessage(LuckyCrates.prefix + getUsage());
                return;
            }

            String crate = args[0];
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
            Server.getInstance().getOnlinePlayers().values().forEach(targetPlayer -> {
                if(keys.giveKey(targetPlayer, crate, amount)){
                    targetPlayer.sendMessage(LuckyCrates.prefix + lang.get("accept-key")
                            .replace("{crate}", crate)
                            .replace("{amount}", String.valueOf(amount)));
                }
            });
            sender.sendMessage(LuckyCrates.prefix +  lang.get("key-all")
                    .replace("{crate}", crate)
                    .replace("{amount}", String.valueOf(amount)));

        }
    }
}
