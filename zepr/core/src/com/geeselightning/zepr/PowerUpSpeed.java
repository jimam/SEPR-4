package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class PowerUpSpeed extends PowerUp {

    PowerUpSpeed() {
        super(2, new Texture("core/assets/speed.png"));
    }

    @Override
    public void activate() {
        super.player.speed += Constant.SPEEDUP;
        super.isActive = true;
    }

    @Override
    public void deactivate(){
        super.player.speed -= Constant.SPEEDUP;
        super.isActive = false;
    }
}
