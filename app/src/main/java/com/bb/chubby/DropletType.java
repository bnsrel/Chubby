package com.bb.chubby;

import android.util.Log;

public enum DropletType {
    FOOD(10), SWEETS(20), VEGETABLES(5), GAMEOVER(0);

    private final int points;

    DropletType(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

    public static DropletType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }

}
