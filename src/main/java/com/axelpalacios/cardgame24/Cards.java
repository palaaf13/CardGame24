package com.axelpalacios.cardgame24;

import javafx.scene.image.Image;
import java.util.Random;

public class Cards {

    private Image[] cardImages = new Image[52];
    private int[] cardValues = new int[4];
    private Random rand = new Random();

    public Cards(){

        for (int i = 0; i < 52; i++) {
            cardImages[i] = new Image(getClass().getResourceAsStream("/com/axelpalacios/cardgame24/cards/c" + (i + 1) + ".png"));
        }
    }
    public Image[] getRandomCards() {

        Image[] selected = new Image[4];

        for(int i = 0; i < 4; i++) {
            int index = rand.nextInt(52);

            selected[i] = cardImages[index];
            cardValues[i] = index % 13 + 1;
        }

        return selected;
    }

    public int[] getCardValues() {
        return cardValues;
    }
}
