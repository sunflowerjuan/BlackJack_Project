package co.edu.uptc.view;

import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import co.edu.uptc.controller.Controller;
import co.edu.uptc.utils.ImgManager;
import co.edu.uptc.view.gameboard.GameBoardPane;
import co.edu.uptc.view.title.MainTitlePane;

public class MainView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private GameBoardPane gameBoard;

    private Controller controller;

    private int h, w;

    public MainView(Controller controller) {
        this.controller = controller;
        initComponets();
        setVisible(true);
        showPanel("GameBoardPane");
        h = this.getHeight();
        w = this.getWidth();
    }

    public void initComponets() {
        configureFrame();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        cardLayoutSet();
        resizeListener();
    }

    public void configureFrame() {
        this.setSize(920, 517);
        this.setResizable(false);
        this.setIconImage(ImgManager.getImage("icon"));
        this.setLocationRelativeTo(null);
        setTitle("BLACKJACK 21");
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void resizeListener() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int oldH = h;
                int oldW = w;
                h = getHeight();
                w = getWidth();
                gameBoard.setCardElements(controller.resize(gameBoard.getCardElements(), oldW, oldH));
                gameBoard.setAnimatedCards(controller.resize(gameBoard.getAnimatedCards(), oldW, oldH));
                gameBoard.repaint();
            }
        });

    }

    public void cardLayoutSet() {
        gameBoard = new GameBoardPane(this);
        mainPanel.add(gameBoard, "GameBoardPane");
        mainPanel.add(new MainTitlePane(), "MainTitlePane");
        add(mainPanel);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public List<CardView> sendViewCards() {
        return controller.createViewCards();
    }

    public CardView sendHideCards() {
        return controller.sendHideCard();
    }

    public void paintGame() {
        gameBoard.paintCards();
    }

    public void revealSecondCard() {
        gameBoard.revealDealerSecondCard();
    }

    public int getH() {
        return h;
    }

    public void setH(int height) {
        this.h = height;
    }

    public int getW() {
        return w;
    }

    public void setW(int width) {
        this.w = width;
    }

}
