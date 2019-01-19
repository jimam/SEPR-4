package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpImmunity extends PowerUp {

    PowerUpImmunity(Level currentLevel) {
        super(3, new Texture("core/assets/speed.png"), currentLevel);
    }

}
