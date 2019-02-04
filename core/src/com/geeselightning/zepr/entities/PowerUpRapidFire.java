package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.levels.Level;
import com.geeselightning.zepr.util.Constant;

/**
 * 
 * @author ljd546
 *	Implements a powerup which reduces the cool down between hits.
 *	NEW FOR ASSESSMENT 3
 */
public class PowerUpRapidFire extends PowerUp {
	
	
    public float timeRemaining = Constant.SPEEDUPTIME;

    public PowerUpRapidFire(Level currentLevel) {
        super(5, new Texture("rapidfire.png"), currentLevel);
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
