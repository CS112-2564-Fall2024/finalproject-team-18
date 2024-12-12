package edu.miracosta.cs112.finalproject.finalproject;

//imports
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

//Class
public class BlackjackController {

    //FXML's
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

    //Deck, Player, Array
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

    //Deal the cards
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

    //Program finding image
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

    //hit logic for player
    @FXML
    public void hit() {
        // Deal a new card
        Card newCard = deck.dealCard();
        player.addCardToHand(newCard);

        // Use 3rd and 4th card slot
        //Statement for if max slots used
        if (PlayerCard3.getImage() == null) {
            PlayerCard3.setImage(getCardImage(newCard));
        } else if (PlayerCard4.getImage() == null) {
            PlayerCard4.setImage(getCardImage(newCard));
        } else {
            System.out.println("Maximum cards reached for the player.");
        }

        // Make sure player doesn't bust
        //If the player busts, end the game
        int playerHandValue = player.calculateHandValue();
        if (playerHandValue > 21) {
            System.out.println("Player busts! Dealer wins.");
            showBustMessage();
            endGame();
        }
    }

    // Message for when player busts
    private void showBustMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Player Busted!");
        alert.setContentText("Your hand value exceeded 21. You lose.");
        alert.showAndWait();
    }

    //Ending the game, reset
    private void endGame() {
        // Clear player and dealer hands
        player.clearHand();
        dealerHand.clear();

        // Clear card images from the board
        PlayerCard1.setImage(null);
        PlayerCard2.setImage(null);
        PlayerCard3.setImage(null);
        PlayerCard4.setImage(null);
        DealerCard1.setImage(null);
        DealerCard2.setImage(null);
        DealerCard3.setImage(null);
        DealerCard4.setImage(null);

        // Create a new deck, shuffle it
        // Make sure buttons are activated
        deck = new Deck();
        deck.shuffle();
        setButtonState(true);

        System.out.println("Game has been reset. Start a new round!");
    }

    //Stand logic & Dealer's turn logic
    @FXML
    public void stand() {
        disableActionButtons(true);
        // Reveal the dealer's second card
        DealerCard2.setImage(getCardImage(dealerHand.get(1)));

        // Dealer's turn logic
        while (getDealerHandValue() < 17 ||
                (getDealerHandValue() <= player.calculateHandValue() && getDealerHandValue() <= 21)) {
            Card newCard = deck.dealCard();
            dealerHand.add(newCard);
            if (DealerCard3.getImage() == null) {
                DealerCard3.setImage(getCardImage(newCard));
            } else if (DealerCard4.getImage() == null) {
                DealerCard4.setImage(getCardImage(newCard));
            } else {
                System.out.println("Maximum cards reached for the dealer.");
            }
        }
        determineWinner();
    }

    //Calculate the dealers hand
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

    //Disable buttons once player stands
    private void disableActionButtons(boolean disable) {
        Hit.setDisable(disable);
        Stand.setDisable(disable);
        DoubleDown.setDisable(disable);
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

    //Game over message
    private void showGameOverMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Result");
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Determine winner logic and give message
    private void determineWinner() {
        int playerValue = player.calculateHandValue();
        int dealerValue = getDealerHandValue();

        if (playerValue > 21) {
            System.out.println("Player busts! Dealer wins.");
            showGameOverMessage("You lose! Player busted.");
        } else if (dealerValue > 21) {
            System.out.println("Dealer busts! Player wins.");
            showGameOverMessage("You win! Dealer busted.");
        } else if (playerValue > dealerValue) {
            System.out.println("Player wins!");
            showGameOverMessage("You win! Your hand is better.");
        } else if (dealerValue > playerValue) {
            System.out.println("Dealer wins!");
            showGameOverMessage("You lose! Dealer's hand is better.");
        } else {
            System.out.println("It's a tie!");
            showGameOverMessage("It's a tie!");
        }
        // Reset game
        endGame();
    }

    //Make sure buttons are enabled
    private void setButtonState(boolean enable) {
        Hit.setDisable(!enable);
        Stand.setDisable(!enable);
        DoubleDown.setDisable(!enable);
        Bet.setDisable(!enable);
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

        setButtonState(true);

        System.out.println("New round started!");
    }
}