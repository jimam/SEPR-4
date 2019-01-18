package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpHeal extends PowerUp {

    PowerUpHeal() {
        super(1, new Texture("core/assets/heal.png"));
    }

    @Override
    public void activate() {

        //Health cannot be more than max health
        if(super.player.health+Constant.HEALUP <= (int)(super.player.HPMult * Constant.PLAYERMAXHP)) {

            super.player.health += Constant.HEALUP;

        } else {

            super.player.health = (int)(super.player.HPMult * Constant.PLAYERMAXHP);
        }
    }
}
