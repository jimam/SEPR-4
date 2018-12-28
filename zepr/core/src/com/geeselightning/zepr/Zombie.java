package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Zombie extends Character {

    private Player player = Player.getInstance();

    public Zombie(Sprite sprite, Vector2 zombieSpawn, Stage currentStage) {
        super(sprite, zombieSpawn, currentStage);
        this.speed = Constant.ZOMBIESPEED;
        this.health = Constant.ZOMBIEMAXHP;
    }

    @Override
    public void update(float delta) {
        //move according to velocity
        super.update(delta);

        // update velocity to move towards player
        // Vector2.scl scales the vector
        velocity = getDirNormVector(player.getCenter()).scl(speed);

        // update direction to face the player
        direction = getDirection(player.getCenter());

        if (getHealth() <= 0) {
            currentStage.zombiesRemaining--;
            currentStage.aliveZombies.remove(this);
            this.getTexture().dispose();
        }
    }
}
