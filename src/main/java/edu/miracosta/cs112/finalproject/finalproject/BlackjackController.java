package edu.miracosta.cs112.finalproject.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlackjackController {

    @FXML
    private Button NewGame;
    @FXML
    private Button Balance;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button LeaveTable;
    @FXML
    private Button DoubleDown;
    @FXML
    private Button Bet;
    @FXML
    private Button Hit;
    @FXML
    private Button Stand;
    @FXML
    private Button startGame;
    @FXML
    private ImageView PlayerCard1, PlayerCard2, PlayerCard3, PlayerCard4;
    @FXML
    private ImageView DealerCard1, DealerCard2, DealerCard3, DealerCard4;

    private Deck deck;
    private Player player;
    private List<Card> dealerHand;

    //Display the player's balance
    @FXML
    public void showBalance() {
        String balanceMessage = "Your current balance is: $" + player.getBalance();
        System.out.println(balanceMessage);

        //Update a label in the UI
        if (balanceLabel != null) {
            balanceLabel.setText(balanceMessage);
        } else {
            //Popup
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Player Balance");
            alert.setHeaderText("Current Balance");
            alert.setContentText(balanceMessage);
            alert.showAndWait();
        }
    }

    //Create and shuffle deck
    //Create player and give a starting balance
    @FXML
    public void initialize() {
        deck = new Deck();
        deck.shuffle();
        player = new Player("Player 1", 1000);
        dealerHand = new ArrayList<>();
    }

    @FXML
    public void dealCards() {
        if (deck.isEmpty()) {
            System.out.println("Deck is empty! Reshuffling.");
            deck = new Deck();
            deck.shuffle();
        }

        // Deal two cards to player and dealer
        Card playerCard1 = deck.dealCard();
        Card playerCard2 = deck.dealCard();
        Card dealerCard1 = deck.dealCard();
        Card dealerCard2 = deck.dealCard();

        // Add cards to hands
        player.addCardToHand(playerCard1);
        player.addCardToHand(playerCard2);
        dealerHand.add(dealerCard1);
        dealerHand.add(dealerCard2);

        // Set images for player's cards
        PlayerCard1.setImage(getCardImage(playerCard1));
        PlayerCard2.setImage(getCardImage(playerCard2));

        // Set images for dealer's cards (one face down)
        DealerCard1.setImage(getCardImage(dealerCard1));
        DealerCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Pictures/SingleFaceDownCard.jpg"))));
        // Clear unused card slots
        clearRemainingCards();
    }

    private Image getCardImage(Card card) {
        String cardName = card.getValue().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".jpg";
        String imagePath = "/Cards/" + cardName;
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream == null) {
            throw new RuntimeException("Image not found: " + imagePath);
        }
        return new Image(imageStream);
    }

    //Clear card slots
    @FXML
    private void clearRemainingCards() {
        PlayerCard3.setImage(null);
        PlayerCard4.setImage(null);
        DealerCard3.setImage(null);
        DealerCard4.setImage(null);
    }

    //Hit logic
    @FXML
    public void hit() {
        Card newCard = deck.dealCard();
        player.addCardToHand(newCard);

        if (PlayerCard3.getImage() == null) {
            PlayerCard3.setImage(getCardImage(newCard));
        } else if (PlayerCard4.getImage() == null) {
            PlayerCard4.setImage(getCardImage(newCard));
        } else {
            System.out.println("Maximum cards reached for the player.");
        }
    }

    //Stand logic
    @FXML
    public void stand() {
        DealerCard2.setImage(getCardImage(dealerHand.get(1))); // Reveal dealer's second card

        while (getDealerHandValue() < 17) {
            Card newCard = deck.dealCard();
            dealerHand.add(newCard);

            if (DealerCard3.getImage() == null) {
                DealerCard3.setImage(getCardImage(newCard));
            } else if (DealerCard4.getImage() == null) {
                DealerCard4.setImage(getCardImage(newCard));
            }
        }

        determineWinner();
    }

    //Get the value of the dealers hand
    private int getDealerHandValue() {
        int handValue = 0;
        int aceCount = 0;

        for (Card card : dealerHand) {
            int cardValue = getCardValue(card);
            handValue += cardValue;
            if (cardValue == 11) {
                aceCount++;
            }
        }

        while (handValue > 21 && aceCount > 0) {
            handValue -= 10;
            aceCount--;
        }

        return handValue;
    }

    //Get the value of a card
    private int getCardValue(Card card) {
        return switch (card.getValue()) {
            case "Ace" -> 11;
            case "Two" -> 2;
            case "Three" -> 3;
            case "Four" -> 4;
            case "Five" -> 5;
            case "Six" -> 6;
            case "Seven" -> 7;
            case "Eight" -> 8;
            case "Nine" -> 9;
            case "Ten", "Jack", "Queen", "King" -> 10;
            default -> throw new IllegalArgumentException("Invalid card value: " + card.getValue());
        };
    }

    //Determine the winner
    private void determineWinner() {
        int playerValue = player.calculateHandValue();
        int dealerValue = getDealerHandValue();

        if (playerValue > 21) {
            System.out.println("Player busts! Dealer wins.");
        } else if (dealerValue > 21 || playerValue > dealerValue) {
            System.out.println("Player wins!");
        } else if (playerValue < dealerValue) {
            System.out.println("Dealer wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    //Start the next round
    @FXML
    public void startNextRound() {
        clearRemainingCards();

        PlayerCard1.setImage(null);
        PlayerCard2.setImage(null);
        DealerCard1.setImage(null);
        DealerCard2.setImage(null);

        deck = new Deck();
        deck.shuffle();
        player.clearHand();
        dealerHand.clear();

        System.out.println("New round started!");
    }
}
