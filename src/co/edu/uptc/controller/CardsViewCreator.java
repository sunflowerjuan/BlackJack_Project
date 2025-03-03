package co.edu.uptc.controller;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.model.BlackjackGame;
import co.edu.uptc.model.Card;
import co.edu.uptc.model.Player;
import co.edu.uptc.utils.ImgManager;
import co.edu.uptc.utils.NumericalConstraints;
import co.edu.uptc.view.CardView;
import co.edu.uptc.view.MainView;

public class CardsViewCreator {

    private BlackjackGame blackjackGame;
    private MainView view;
    private int cardWidth;
    private int cardHeight;

    public CardsViewCreator(BlackjackGame blackjackGame, MainView view) {
        this.blackjackGame = blackjackGame;
        this.view = view;
        this.cardWidth = (int) (view.getW() * NumericalConstraints.CARD_WIDTH.getValue());
        this.cardHeight = (int) (view.getH() * NumericalConstraints.CARD_HEIGTH.getValue());
    }

    public List<CardView> resize(List<CardView> cards, int oldWidth, int oldHeight) {
        this.cardWidth = (int) (view.getW() * NumericalConstraints.CARD_WIDTH.getValue());
        this.cardHeight = (int) (view.getH() * NumericalConstraints.CARD_HEIGTH.getValue());
        for (CardView cardView : cards) {
            int x = (cardView.getX() * view.getW()) / oldWidth;
            int y = (cardView.getY() * view.getH()) / oldHeight;
            cardView.setWidth(cardWidth);
            cardView.setHeight(cardHeight);
            cardView.setX(x);
            cardView.setY(y);
        }
        return cards;
    }

    public List<CardView> createViewCards() {
        List<CardView> cardsToPaint = new ArrayList<>();
        for (Player player : blackjackGame.getPlayers()) {
            addPlayerCards(cardsToPaint, player, player.getPosition());
        }
        addCrupierCards(cardsToPaint);
        return cardsToPaint;
    }

    public CardView revealHideCard() {
        int x = (int) (NumericalConstraints.DCARD_X.getValue() * view.getW());
        int y = (int) (NumericalConstraints.DCARD_Y.getValue() * view.getH());
        Card card = blackjackGame.getCrupier().getCrupierCards().get(1);
        return new CardView(x + (1 * 30), y, cardWidth, cardHeight, ImgManager.getImage(card.getName()));
    }

    private void addCrupierCards(List<CardView> cards) {
        List<Card> crupierCards = blackjackGame.getCrupier().getCrupierCards();
        int x = (int) (NumericalConstraints.DCARD_X.getValue() * view.getW());
        int y = (int) (NumericalConstraints.DCARD_Y.getValue() * view.getH());
        int i = 0;
        for (Card card : crupierCards) {
            if (!card.isState()) {
                CardView cardView = new CardView(x + (i * 30), y, cardWidth, cardHeight);
                cardView.setImage(
                        i == 1 ? ImgManager.getImage("cardback_red")
                                : ImgManager.getImage(crupierCards.get(i).getName()));
                cards.add(cardView);
                crupierCards.get(i).setState(true);
            }
            i++;
        }
    }

    private void addPlayerCards(List<CardView> cards, Player player, int playerIndex) {
        int x = getXPosition(playerIndex);
        int y = getYPosition(playerIndex);
        addCards(cards, player.getCardsInGame(), x, y);
        if (player.isSplit()) {
            addCards(cards, player.getCardsInSecondHand(), x - 30, y + 60);
        }
    }

    private void addCards(List<CardView> cards, List<Card> playerCards, int x, int y) {
        int i = 0;
        for (Card card : playerCards) {
            if (!card.isState()) {
                CardView cardView = new CardView(x + (i * 30), y, cardWidth, cardHeight);
                cardView.setImage(ImgManager.getImage(card.getName()));
                card.setState(true);
                cards.add(cardView);
            }
            i++;
        }
    }

    private int getXPosition(int playerIndex) {
        if (playerIndex == 0)
            return (int) (view.getW() * NumericalConstraints.FP_X.getValue());
        if (playerIndex == 1)
            return (int) (view.getW() * NumericalConstraints.SP_X.getValue());
        if (playerIndex == 2)
            return (int) (view.getW() * NumericalConstraints.TP_X.getValue());
        return 0;
    }

    private int getYPosition(int playerIndex) {
        if (playerIndex == 0 || playerIndex == 2)
            return (int) (view.getH() * NumericalConstraints.FP_Y.getValue());
        if (playerIndex == 1)
            return (int) (view.getH() * NumericalConstraints.SP_Y.getValue());
        return 0;
    }
}
