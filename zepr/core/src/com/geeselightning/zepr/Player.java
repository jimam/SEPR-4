package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {

    private static final Player instance = new Player(new Sprite(new Texture("core/assets/player01.png")), new Vector2(0, 0));
    int attackDamage = Constant.PLAYERDMG;
    int hitRange = Constant.PLAYERRANGE;
    final float hitCooldown =  Constant.PLAYERHITCOOLDOWN;
    boolean attack = false;
    float HPMult;
    float dmgMult;
    float speedMult;
    String playertype;


    private Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn, null);
    }

    public static Player getInstance(){
        return instance;
    }

    public void setType(String playertype){
        this.playertype = playertype;
    }

    public void attack(Zombie zombie, float delta) {
        if (canHitGlobal(zombie, hitRange) && hitRefresh > hitCooldown) {
            zombie.takeDamage(attackDamage);
            hitRefresh = 0;
        } else {
            hitRefresh += delta;
        }
    }

    public void respawn(Vector2 playerSpawn, Level level){
        setX(playerSpawn.x);
        setY(playerSpawn.y);
        if (playertype == "nerdy"){
            dmgMult = Constant.NERDYDMGMULT;
            HPMult = Constant.NERDYHPMULT;
            speedMult = Constant.NERDYSPEEDMULT;
        }
        else if (playertype == "sporty"){
            dmgMult = Constant.SPORTYDMGMULT;
            HPMult = Constant.SPORTYHPMULT;
            speedMult = Constant.SPORTYSPEEDMULT;
        }
        else if(playertype == null){
            dmgMult =1;
            HPMult = 1;
            speedMult = 1;
        }
        this.attackDamage = (int)(Constant.PLAYERDMG * dmgMult);
        this.speed = (int)(Constant.PLAYERSPEED * speedMult);
        this.health = (int)(HPMult * Constant.PLAYERMAXHP);
        this.currentLevel = level;
        this.setTexture(new Texture("core/assets/player01.png"));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Update the direction the player is facing.
        direction = getDirectionTo(currentLevel.getMouseWorldCoordinates());

        if (health <= 0) {
            currentLevel.gameOver();
        }
    }
}
