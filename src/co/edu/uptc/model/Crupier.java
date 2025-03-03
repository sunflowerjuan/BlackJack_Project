package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.utils.RandomUtil;

public class Crupier {
    private List<Card> crupierCards;
    private int cardsValue;
    private boolean win;

    public Crupier() {
        crupierCards = new ArrayList<>();
        win = true;
    }

    public void addCard(Card card) {
        crupierCards.add(card);
        crupierDecision();

    }

    public void crupierDecision() {
        int[] possibleValues = countCardsValue();
        if (possibleValues.length == 0) {
            cardsValue = -1;
            win = false;
        } else if (possibleValues.length == 1) {
            cardsValue = possibleValues[0];
        } else {
            int select = RandomUtil.getRandomNumber(possibleValues.length - 1);
            cardsValue = possibleValues[select];
        }
    }

    public int[] countCardsValue() {
        int sum = 0;
        int numberOfAs = countNumberOfAs();

        for (Card card : crupierCards) {
            sum += card.getValue();
        }

        if (numberOfAs == 0) {
            return new int[] { sum };
        }

        int[] possibleValues = new int[numberOfAs + 1];
        possibleValues[0] = sum;
        for (int i = 1; i <= numberOfAs; i++) {
            sum -= 10;
            possibleValues[i] = sum;
        }

        return positiveValues(possibleValues);
    }

    public int[] positiveValues(int[] possibleValues) {
        List<Integer> positiveValues = new ArrayList<>();
        int positive = 0;
        for (int value : possibleValues) {
            if (value <= 21) {
                positiveValues.add(value);
                positive++;
            }
        }
        if (positive == 0) {
            return new int[0];
        }
        return positiveValues.stream().mapToInt(Integer::intValue).toArray();
    }

    public int countNumberOfAs() {
        int number = 0;
        for (Card card : crupierCards) {
            if (card.getRank() == 'A') {
                number++;
            }
        }
        return number;
    }

    public void resetCrupier() {
        crupierCards.clear();
        cardsValue = 0;
        win = true;
    }

    public List<Card> getCrupierCards() {
        return crupierCards;
    }

    public void setCrupierCards(List<Card> crupierCards) {
        this.crupierCards = crupierCards;
    }

    public int getCardsValue() {
        return cardsValue;
    }

    public void setCardsValue(int cardsValue) {
        this.cardsValue = cardsValue;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean crupiersWin) {
        this.win = crupiersWin;
    }

    @Override
    public String toString() {
        return "Crupier [crupierCards=" + crupierCards + ", cardsValue=" + cardsValue + " is win: " + isWin();
    }

}
