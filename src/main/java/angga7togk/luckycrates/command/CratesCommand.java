package angga7togk.luckycrates.command;

import angga7togk.luckycrates.language.Languages;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.Map;

public abstract class CratesCommand extends Command {


    public CratesCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        this.setPermission("luckycrates.command");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(this.testPermission(sender)){
            Map<String, String> lang = new Languages().getLanguage();
            execute(sender, lang, args);
        }
        return true;
    }

    public abstract void execute(CommandSender sender, Map<String, String> lang, String[] args);
}
