package angga7togk.luckycrates.task;

import angga7togk.luckycrates.LuckyCrates;
import angga7togk.luckycrates.utils.FloatingUtils;

public class FloatingTextTask implements Runnable{
    @Override
    public void run() {
        for (String crateName : LuckyCrates.pos.getSection("crates").getKeys(false)){
            double x = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.x");
            double y = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.y");
            double z = LuckyCrates.pos.getDouble("crates." + crateName + ".pos.z");
            String level = LuckyCrates.pos.getString("crates." + crateName + ".pos.level");
            long id = LuckyCrates.pos.getLong("crates." + crateName + ".id");

            String floating = LuckyCrates.crates.getString(crateName + ".floating-text");
            FloatingUtils.createEntity(floating, id, (float) x, (float) y, (float) z, level);
        }
    }
}
