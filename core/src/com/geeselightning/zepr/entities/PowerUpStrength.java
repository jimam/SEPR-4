package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.levels.Level;
import com.geeselightning.zepr.util.Constant;
/**
 * 
 * @author ljd546
 *	Implements a powerup which increases the user's damage
 *	NEW FOR ASSESSMENT 3
 */
public class PowerUpStrength extends PowerUp {

    public float timeRemaining = Constant.ATKUPTIME;

    public PowerUpStrength(Level currentLevel) {
        super(4, new Texture("strength.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        super.player.attackDamage += 10;
        this.getTexture().dispose();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        super.player.attackDamage -= 10;
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
