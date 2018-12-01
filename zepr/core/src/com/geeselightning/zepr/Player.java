package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {

    private static final Player instance = new Player(new Sprite(new Texture("core/assets/player01.png")), new Vector2(0, 0));
    private Stage currentStage;

    private Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn);
    }

    public static Player getInstance(){
        return instance;
    }

    public void respawn(Vector2 playerSpawn, Stage stage){
        setX(playerSpawn.x);
        setY(playerSpawn.y);
        this.speed = Constant.PLAYERSPEED;
        this.health = Constant.PLAYERMAXHP;
        this.currentStage = stage;
        this.setTexture(new Texture("core/assets/player01.png"));
    }
}
