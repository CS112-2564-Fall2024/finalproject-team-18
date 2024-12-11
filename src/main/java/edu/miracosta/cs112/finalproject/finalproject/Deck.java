package edu.miracosta.cs112.finalproject.finalproject;

//Shuffles and deals cards

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
    }

}
