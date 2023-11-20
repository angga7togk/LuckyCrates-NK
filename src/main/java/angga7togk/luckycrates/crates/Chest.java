package angga7togk.luckycrates.crates;

import angga7togk.luckycrates.LuckyCrates;

public abstract class Chest implements Crates{



    @Override
    public boolean isCrates() {
        return false;
    }

    @Override
    public boolean crateExists(String crateName) {
        return LuckyCrates.crates.exists(crateName);
    }
}
