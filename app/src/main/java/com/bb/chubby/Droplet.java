package com.bb.chubby;

import java.util.Random;

public class Droplet {
    private final String[] food = {"🌭", "🍕", "🍗", "🍔", "🥩"};
    private final String[] sweets = {"🍦", "🍩", "🍰", "🍭", "🥞"};
    private final String[] vegetables = {"🍏", "🥦", "🥬", "🥕", "🍆"};
    private final String[] gameover = {"☠️", "☠️", "☠️", "☠️", "☠️"};


    private int tag;
    private int xPos;
    private DropletType type;
    private String emoji;
    private int points;

    public Droplet(int tag, int xPos) {
        this.tag = tag;
        this.xPos = xPos;
        generateEmoji();
    }

    private void generateEmoji() {
        this.type = DropletType.getRandom();
        int item = new Random().nextInt(5);
        switch(type) {
            case FOOD:{
                this.emoji = food[item];
                break;
            }
            case SWEETS:{
                this.emoji = sweets[item];
                break;
            }
            case VEGETABLES:{
                this.emoji = vegetables[item];
                break;
            }
            case GAMEOVER:{
                this.emoji = gameover[item];
                break;
            }
        }
        this.points = this.type.getPoints();
    }

    public int getTag() {
        return tag;
    }

    public int getxPos() {
        return xPos;
    }

    public DropletType getType() {
        return type;
    }

    public String getEmoji() {
        return emoji;
    }
}
