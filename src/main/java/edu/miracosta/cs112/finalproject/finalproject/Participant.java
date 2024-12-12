package edu.miracosta.cs112.finalproject.finalproject;

import java.util.ArrayList;
import java.util.List;

//Abstract class!
public abstract class Participant {
    protected String name;
    protected List<Card> hand;

    //Player or dealer
    public Participant(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    //Adding a card to the player/dealer hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    //Calculate the hands value
    public abstract int calculateHandValue();
}