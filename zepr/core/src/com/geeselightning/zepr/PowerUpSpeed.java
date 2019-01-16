package com.geeselightning.zepr;

public class PowerUpSpeed extends PowerUp {

    private long time;

    @Override
    public void activate() {

        time = System.currentTimeMillis(); // time in milliseconds

        super.player.speed += Constant.SPEEDUP;

        // reduce speed back to normal after some time passes
        while(System.currentTimeMillis() < time + Constant.SPEEDUPTIME){}
        super.player.speed -= Constant.SPEEDUP;
    }
}
