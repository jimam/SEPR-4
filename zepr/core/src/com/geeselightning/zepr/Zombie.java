package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Zombie extends Character {

    public Zombie(Sprite sprite, Vector2 zombieSpawn) {
        super(sprite, zombieSpawn);
        this.speed = Constant.ZOMBIESPEED;
        this.health = Constant.ZOMBIEMAXHP;
    }

    @Override
    public void update(float delta) {
        //move according to velocity
        super.update(delta);

        // update velocity to move towards player
        Vector2 playercoords= new Vector2(Player.getInstance().getX(),Player.getInstance().getY());
        velocity = getDirNormVector(playercoords).scl(speed);

    }
}
