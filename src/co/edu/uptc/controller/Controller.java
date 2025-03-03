package co.edu.uptc.controller;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.model.BlackjackGame;
import co.edu.uptc.model.Player;
import co.edu.uptc.utils.RandomUtil;
import co.edu.uptc.view.CardView;
import co.edu.uptc.view.MainView;

public class Controller {

    private BlackjackGame blackjackGame;
    private MainView view;
    private CardsViewCreator cardsCreator;

    public Controller() {
        blackjackGame = new BlackjackGame();
        view = new MainView(this);
        cardsCreator = new CardsViewCreator(blackjackGame, view);
        test();
    }

    public List<CardView> createViewCards() {
        return cardsCreator.createViewCards();
    }

    public CardView sendHideCard() {
        return cardsCreator.revealHideCard();
    }

    public List<CardView> resize(List<CardView> actuallCards, int oldWidth, int oldHeight) {
        return cardsCreator.resize(actuallCards, oldWidth, oldHeight);
    }

    public void test() {

        // Agregar jugadores con saldo inicial
        Player player1 = new Player("Juan", 1000);
        Player player2 = new Player("Majo", 1000);
        Player player3 = new Player("Juli", 1000);

        blackjackGame.addPlayer(player1);
        blackjackGame.addPlayer(player2);
        blackjackGame.addPlayer(player3);
        for (Player player : blackjackGame.getPlayers()) {
            double bet = 150 + RandomUtil.getRandomNumber(200); // Apuesta entre 50 y 200
            System.out.println("Apuesta de : " + player.getNickName() + " =" + bet);
            player.setBet(bet);
        }

        // Iniciar el juego
        System.out.println("o Repartiendo cartas iniciales...");
        blackjackGame.initialDeal();
        view.paintGame();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Mostrar manos iniciales
        System.out.println("🃏 Mano del crupier: " + blackjackGame.getCrupier());
        System.out.println("----");
        for (Player player : blackjackGame.getPlayers()) {
            System.out.println(player.getNickName() + " tiene: " + player);
            System.out.println("----");
        }
        int size = blackjackGame.getPlayers().size();
        // Simulación de turnos de jugadores
        for (int i = 0; i < size; i++) {
            Player player = blackjackGame.getPlayers().peek();
            System.out.println("\n ## Turno de " + player.getNickName());

            if (player.blackJack()) {
                System.out.println("🎉 ¡Blackjack para " + player.getNickName() + "!");
                blackjackGame.playerStay();
                continue;
            }

            // Verificar si el jugador puede dividir
            if (player.canSplit()) {
                int decisionSplit = (RandomUtil.getRandomNumber(99) < 99) ? 1 : 0; // 30% de dividir, 70% de no
                                                                                   // hacerlo
                if (decisionSplit == 1) {
                    System.out.println(player.getNickName() + " divide sus cartas.");
                    System.out.println(" Nuevas manos: " + player);
                    blackjackGame.playerDivide();
                }
                view.paintGame();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

            while (player.isInGame()) {
                int puntos = player.cardsValue()[0];
                int decision;

                if (puntos >= 18) {
                    decision = (RandomUtil.getRandomNumber(99) < 15) ? 1 : 0; // 15% de pedir carta, 85% de quedarse
                } else if (puntos <= 13) {
                    decision = (RandomUtil.getRandomNumber(99) < 80) ? 1 : 0; // 80% de pedir carta
                } else {
                    decision = RandomUtil.getRandomNumber(1); // 50% - 50%
                }

                if (decision == 1 && puntos < 21) {
                    blackjackGame.playerHit();
                    System.out.println(player.getNickName() + "  *pide carta. Nueva mano: " + player);
                } else {
                    System.out.println(player.getNickName() + "  *se planta.");
                    blackjackGame.playerStay();
                    break;
                }
                view.paintGame();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        // Turno del crupier
        System.out.println("\n🔹 Turno del crupier...");
        view.revealSecondCard();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        blackjackGame.crupierGame();
        view.paintGame();
        System.out.println("JUEGO DEL CRUPIER: " + blackjackGame.getCrupier());

        // Mostrar resultados finales
        System.out.println("\n ################Resultados finales ################");
        for (Player player : blackjackGame.getPlayers()) {
            System.out.println(player.getNickName() + " tiene: " + player + " | Puntos: " + player.getPoints());
        }
        System.out.println("🃏 Crupier tiene: " + blackjackGame.getCrupier());

        System.out.println("$$$$$$$$$$$$$$$ NUEVO JUEGO $$$$$$$$$$$$$$$");

        blackjackGame.resetGame();

    }
}
