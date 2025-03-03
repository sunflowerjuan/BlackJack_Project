package co.edu.uptc.view.gameboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.utils.ImgManager;
import co.edu.uptc.utils.NumericalConstraints;
import co.edu.uptc.view.CardView;
import co.edu.uptc.view.MainView;

public class GameBoardPane extends JPanel {

    private Image boardBackground;
    private List<CardView> cardElements;
    private List<CardView> animatedCards;
    private MainView view;
    private int deckCardsLeft = 15; // Cartas visibles en el mazo
    private Timer animationTimer;
    private static final int ANIMATION_SPEED = 6; // Velocidad del movimiento (ms)
    private static final int STEP_SIZE = 20; // Cantidad de pixeles por frame

    private boolean inAnimation;

    public GameBoardPane(MainView view) {
        this.view = view;
        inAnimation = false;
        cardElements = new ArrayList<>();
        animatedCards = new ArrayList<>();
        try {
            boardBackground = ImgManager.getImage("gameboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBakground(g);
        paintDeck(g);
        paintCards(g);
    }

    private void paintBakground(Graphics g) {
        if (boardBackground != null) {
            g.drawImage(boardBackground, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void paintDeck(Graphics g) {
        int x = (int) (view.getWidth() * NumericalConstraints.DECK_X.getValue());
        int y = (int) (view.getHeight() * NumericalConstraints.DECK_Y.getValue());
        int width = (int) (view.getWidth() * NumericalConstraints.CARD_WIDTH.getValue());
        int height = (int) (view.getHeight() * NumericalConstraints.CARD_HEIGTH.getValue());
        for (int i = deckCardsLeft; i >= 0; i--) {
            g.drawImage(ImgManager.getImage("cardback_red"), x, y + (i * 3), width, height, this);
        }
    }

    private void paintCards(Graphics g) {
        for (CardView card : cardElements) {
            g.drawImage(card.getImage(), card.getX(), card.getY(), card.getWidth(), card.getHeight(), this);
        }
        for (CardView card : animatedCards) {
            g.drawImage(card.getImage(), card.getX(), card.getY(), card.getWidth(), card.getHeight(), this);
        }
    }

    public void paintCards() {
        List<CardView> newCards = view.sendViewCards();
        animateCardDistribution(newCards);
        repaint();
    }

    private void animateCardDistribution(List<CardView> newCards) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animatedCards.clear();
        int deckX = (int) (view.getWidth() * NumericalConstraints.DECK_X.getValue());
        int deckY = (int) (view.getHeight() * NumericalConstraints.DECK_Y.getValue());

        for (CardView card : newCards) {
            if (!card.isPaint()) {
                card.setPaint(true);
                animatedCards.add(new CardView(deckX, deckY, card.getWidth(), card.getHeight(), card.getImage()));
            }
        }

        final int[] index = { 0 };
        inAnimation = true;

        animationTimer = new Timer(ANIMATION_SPEED, e -> {
            if (index[0] >= animatedCards.size()) {
                ((Timer) e.getSource()).stop();
                inAnimation = false;
                repaint();
                return;
            }
            CardView card = animatedCards.get(index[0]);
            int targetX = newCards.get(newCards.indexOf(newCards.get(index[0]))).getX();
            int targetY = newCards.get(newCards.indexOf(newCards.get(index[0]))).getY();

            if (Math.abs(card.getX() - targetX) > STEP_SIZE) {
                card.setX(card.getX() + (card.getX() < targetX ? STEP_SIZE : -STEP_SIZE));
            } else {
                card.setX(targetX);
            }

            if (Math.abs(card.getY() - targetY) > STEP_SIZE) {
                card.setY(card.getY() + (card.getY() < targetY ? STEP_SIZE : -STEP_SIZE));
            } else {
                card.setY(targetY);
            }

            if (card.getX() == targetX && card.getY() == targetY) {
                cardElements.add(animatedCards.get(index[0]));
                index[0]++;
                if (index[0] % 2 == 0 && deckCardsLeft > 0) {
                    deckCardsLeft--;
                }
            }

            repaint();
        });
        animationTimer.start();
    }

    public void revealDealerSecondCard() {
        CardView revealCard = view.sendHideCards();
        int x = revealCard.getX();
        int y = revealCard.getY();
        for (CardView card : cardElements) {
            if (card.getX() == x && card.getY() == y) {
                System.out.println("change");
                animatedCards.add(card);
                Timer flipTimer = new Timer(ANIMATION_SPEED, e -> {
                    card.setImage(revealCard.getImage());
                    repaint();
                    ((Timer) e.getSource()).stop();
                });
                flipTimer.setInitialDelay(100);
                flipTimer.start();
                break;
            }
        }
    }

    public List<CardView> getCardElements() {
        return cardElements;
    }

    public void setCardElements(List<CardView> cardElements) {
        this.cardElements = cardElements;
    }

    public List<CardView> getAnimatedCards() {
        return animatedCards;
    }

    public void setAnimatedCards(List<CardView> animatedCards) {
        this.animatedCards = animatedCards;
    }
}
