package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.levels.Level;

public class PowerUpRapidFire extends PowerUp {

    public float timeRemaining = Constant.SPEEDUPTIME;

    public PowerUpRapidFire(Level currentLevel) {
        super(5, new Texture("speed.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        super.player.hitCooldown = 0.08f;
        this.getTexture().dispose();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        super.player.hitCooldown = Constant.PLAYERHITCOOLDOWN;
    }

    @Override
    public void update(float delta) {
        if (active) {
            timeRemaining -= delta;
        }
        if (timeRemaining < 0) {
            deactivate();
        }
    }
}
