package edu.miracosta.cs112.finalproject.finalproject;

//Shuffles and deals cards

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Deck {

    private List<Card> cards;
    private Random random;

    public Deck() {
        cards = new ArrayList<>();
        random = new Random();


        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] values = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};

        for(String suit : suits) {
            for(String value : values) {
                cards.add(new Card(suit, value));
            }
        }
    }

    //Shuffle
    public void shuffle() {
        List<Card> shuffled = new ArrayList<>();
        while(!cards.isEmpty()) {
            int randomIndex = random.nextInt(cards.size());
            shuffled.add(cards.remove(randomIndex));
        }
        cards = shuffled;
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.remove(cards.size() - 1);
    }

    public int getRemainingCards() {
        return cards.size();
    }

    @Override
    public String toString() {
        String result = "Deck contains: \n";
        for (Card card : cards) {
            result += card + "\n";
        }
        return result;
    }

}
