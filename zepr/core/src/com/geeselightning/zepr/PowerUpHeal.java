package com.geeselightning.zepr;

public class PowerUpHeal extends PowerUp {

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
