package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {

    private static final Player instance = new Player(new Sprite(new Texture("core/assets/player01.png")), new Vector2(0, 0));
    int attackDamage = 20;
    int hitRange = 30;
    final float hitCooldown = (float) 0.2;


    private Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn, null);
    }

    public static Player getInstance(){
        return instance;
    }

    public void respawn(Vector2 playerSpawn, Level level){
        setX(playerSpawn.x);
        setY(playerSpawn.y);
        this.speed = Constant.PLAYERSPEED;
        this.health = Constant.PLAYERMAXHP;
        this.currentLevel = level;
        this.setTexture(new Texture("core/assets/player01.png"));
    }

    public boolean canHit(Character character){
        return canHitGlobal(character, hitRange);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // update the direction the player is facing
        direction = getDirection(currentLevel.getMouseWorldCoordinates());

        if (health <= 0) {
            currentLevel.gameOver();
        }
    }
}
