package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class PowerUpSpeed extends PowerUp {

    boolean isActive;
    public float timeRemaining = Constant.SPEEDUPTIME;

    PowerUpSpeed(Level currentLevel) {
        super(2, new Texture("core/assets/speed.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        super.player.speed += Constant.SPEEDUP;
        isActive = true;
        this.getTexture().dispose();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        super.player.speed -= Constant.SPEEDUP;
        isActive = false;
    }

    @Override
    public void update(float delta) {
        if (isActive) {
            timeRemaining -= delta;
        }
        if (timeRemaining < 0) {
            deactivate();
        }
    }
}
