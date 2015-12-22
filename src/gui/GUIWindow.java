package gui;

import game.*;
import player.Player;
import player.PlayerFactory;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.LinkedList;

import static game.COLOR.*;

public class GUIWindow {

    private static final Color GREEN = new Color(0, 100, 0);
    private static final Color LIGHT_GREEN = new Color(30, 140, 30);
    private static final String TURN = "Turn!";
    private final JLabel txtWhite = new JLabel("White : ");
    private final JLabel txtBlack = new JLabel("Black : ");
    private final String DISPLAY = "Board";
    private final int BOARD_SIZE = 8;

    private ImageIcon picture;
    private JPanel slotsPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
    private JPanel[][] panelBoard = new JPanel[BOARD_SIZE][BOARD_SIZE];
    private JLabel txtCurrentPlayer = new JLabel("Black");
    private JLabel txtWhiteScore = new JLabel(" __ ");
    private JLabel txtBlackScore = new JLabel(" __ ");
    private int wCount = 0;
    private int bCount = 0;
    private Game game;
    private JButton actionButton;


    private PlayerFactory playerFactory = new PlayerFactory();
    private String whiteString = null;
    private String blackString = null;


    private Player newPlayer(String name, COLOR color) {
      return playerFactory.newPlayer(name, color);
    }


    public GUIWindow() {

        CreateUI();


    }

    public void CreateUI() {
        JFrame window = new JFrame("Othello");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(200, 200, 800, 700);
        window.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(CreateDisplay(), BorderLayout.CENTER);
        panel.add(CreateMenu(), BorderLayout.EAST);
        window.add(panel, BorderLayout.CENTER);
        window.setVisible(true);

    }

    private JPanel CreateMenu() {
        JPanel panel = new JPanel(new GridLayout(15, 0));
        JButton newGame = new JButton("New Manual Game");
        JButton white = new JButton("White Move");
        JButton black = new JButton("Black Move");
        TitledBorder border = new TitledBorder("Menu");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });
        panel.add(newGame);

        JButton automaticGame = new JButton("Auto Play");
        automaticGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartInAutoMode();
            }
        });
        panel.add(automaticGame);





        Border border2 = BorderFactory.createTitledBorder("Pizza Toppings");
        panel.setBorder(border2);

        JComboBox blackPlayers = new JComboBox(playerFactory.availablePlayers);
        blackString = playerFactory.availablePlayers[0];

        blackPlayers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                blackString = blackPlayers.getSelectedItem().toString();
            }
        });

        JLabel jlabel = new JLabel("Black");
        jlabel.setFont(new Font("Verdana",1,12));
        panel.add(jlabel);
        JTextField whiteText = new JTextField(WHITE.toString());

        JComboBox whitePlayers = new JComboBox(playerFactory.availablePlayers);
        whiteString = playerFactory.availablePlayers[0];
        whitePlayers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteString = whitePlayers.getSelectedItem().toString();
            }
        });

        panel.add(blackPlayers);
        jlabel = new JLabel("White");
        jlabel.setFont(new Font("Verdana", 1, 12));
        panel.add(jlabel);
        panel.add(whitePlayers);


        this.actionButton = new JButton(TURN);
        actionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                turn();
            }
        });
        panel.add(actionButton);
        panel.setBorder(border);
        return panel;
    }


    private void turn() {
        Position m = game.nextTurn();
        if (m == null) {
            return;
        }
        game.flip(m);
        updateGameStatusPanel();
        updateGameBoard();

    }

    private void restartInAutoMode() {
        restart();
        for(int i = 0; i <  200; i++) {
            turn();
            if(game.isFinished()) break;
        }
    }

    private JPanel CreateDisplay() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new FlowLayout());


        TitledBorder border = new TitledBorder(DISPLAY);

        JLabel txtCurrent = new JLabel("   Next turn: ");
        mainPanel.setBorder(border);

        panel2.add(txtWhite);
        panel2.add(txtWhiteScore);
        panel2.add(txtBlack);
        panel2.add(txtBlackScore);
        panel2.add(txtCurrent);
        panel2.add(txtCurrentPlayer);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Border black = BorderFactory.createLineBorder(Color.black);
                final JPanel slots = new JPanel();
                slots.setBorder(black);
                slots.setBackground(GREEN);
                slotsPanel.add(slots);
                panelBoard[i][j] = slots;
            }
        }

        mainPanel.add(slotsPanel, BorderLayout.CENTER);
        mainPanel.add(panel2, BorderLayout.SOUTH);
        return mainPanel;
    }

    public void setPicture(COLOR color, Position position) {

        JPanel currentPanel = panelBoard[position.row][position.column];
        currentPanel.removeAll();
        JLabel picLabel;
        if (color == BLACK) {
            picture = createImageIcon("./black.png");
            picLabel = new JLabel((picture));
            currentPanel.add(picLabel);

        } else if (color == WHITE) {
            picture = createImageIcon("./white.png");
            picLabel = new JLabel((picture));
            currentPanel.add(picLabel);

        }

        currentPanel.setBackground(GREEN);

    }

    public void updateGameBoard() {
        LinkedList<Position> legalPositions = game.getSlotsToColor();
        updateColorsOfSlots(legalPositions);

        if(game.isFinished()) GUIConsole.display("The winner is .... (this functionallity is not implemented yet..)");

        slotsPanel.updateUI();
    }

    private void updateColorsOfSlots(LinkedList<Position> legalPositions) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JPanel currentSlot = panelBoard[i][j];
                currentSlot.removeAll();
                COLOR colorOfPosition = game.colorOfPosition(new Position(i,j));
                switch (colorOfPosition) {
                    case BLACK:
                        panelBoard[i][j].setBackground(GREEN);
                        setPicture(BLACK, new Position(i, j));
                        break;
                    case WHITE:
                        panelBoard[i][j].setBackground(GREEN);
                        setPicture(WHITE, new Position(i, j));
                        break;
                    case EMPTY:
                        setPicture(EMPTY, new Position(i, j));

                        if (game.isLegal(new Position(i, j))) {

                            for(Position m : legalPositions){
                                if(m.row == i || m.column == j) {
                                    panelBoard[i][j].setBackground(LIGHT_GREEN);
                                }
                            }
                        }
                        break;
                }



            }
        }
    }

    public void restart() {
       playerFactory = new PlayerFactory();

        if(whiteString == null) whiteString = "EdgeEddie";
        if(blackString == null) blackString = "EdgeEddie";

        game = new Game(playerFactory.newPlayer(whiteString, WHITE), playerFactory.newPlayer(blackString, BLACK));

        updateGameBoard();
        slotsPanel.updateUI();
        updateGameStatusPanel();

    }

    public void updateGameStatusPanel() {
        wCount = 0;
        bCount = 0;

        String text = game.getNextTurn().NAME + " " + game.getNextTurn().COLOR;

        txtCurrentPlayer.setText(text);


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                COLOR positionColor = game.colorOfPosition(new Position(i,j));
                if (positionColor == WHITE) {
                    wCount++;
                }
                else if (positionColor == BLACK) {
                    bCount++;
                }
            }
        }
        txtWhiteScore.setText("" + wCount);
        txtBlackScore.setText("" + bCount);
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find the file: " + path);
            return null;
        }
    }



}
