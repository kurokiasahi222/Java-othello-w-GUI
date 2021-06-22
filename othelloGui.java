import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class othelloGui extends JFrame implements ActionListener {
    private JPanel panel;
    private JButton[][] buttons;
    private String[][] board;
    private String[][] hintBoard;
    private othelloCopy game;

    public othelloGui() {
        game = new othelloCopy(8);
        this.setBoard(game.getBoard());
        initUI();
        this.setVisible(true);
    }

    public void initUI() {
        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(8, 8, 5, 5));

        this.buttons = new JButton[8][8];

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.buttons[i][j] = new JButton(this.board[i][j]);
                panel.add(this.buttons[i][j]);
                buttons[i][j].addActionListener(this);
            }
            panel.setVisible(true);
        }

        ArrayList<ArrayList<Integer>> valid = new ArrayList<>();
        valid = game.get_valid_moves();
        for(int m = 0; m < valid.size(); m++){
            int index0 = valid.get(m).get(0);
            int index1 = valid.get(m).get(1);
            buttons[index0][index1].setText(".");
        }

        add(panel);
        setTitle("Othello");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setBoard(String [][] board) {
        this.board = board;
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j] == e.getSource()) {
                    System.out.println("e.getSource()" + i + " " +  j);
                    if (humanPart(i, j)){
                        computerPart();
                    }
                }
            }
        }
    }

    public boolean humanPart(int i, int j) {
        boolean tf = false;
        if (this.game.humansTurn(i, j))
            tf = true;
        setBoard(game.getBoard());
        this.printBoardGUI();
        if (game.game_over()) {
            System.out.println("The game has ended");
            System.out.println("The winner is.... " + game.get_winner());
        }
        System.out.println(tf);
        return tf;
    }

//    public void computerPart(){
//
//        ArrayList<Integer> list = new ArrayList<>();
//        list = game.get_random();
//        System.out.println(Arrays.deepToString(list.toArray()));
//        int i = list.get(0);
//        int j = list.get(1);
//        game.flip_tiles(i, j);
//        game.toggle();
//        setBoard(game.getBoard());
//        printBoardGUI();
//        if (game.game_over()) {
//            System.out.println("The game has ended. ");
//            System.out.println(game.get_winner());
//        }
//    }

    public void computerPart() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                ArrayList<Integer> list = new ArrayList<>();
                list = game.get_random();
                System.out.println(Arrays.deepToString(list.toArray()));
                int i = list.get(0);
                int j = list.get(1);
                game.flip_tiles(i, j);
                game.toggle();
                setBoard(game.getBoard());
                printBoardGUI();
            }
        };
        thread.start();
        if (game.game_over()) {
            System.out.println("The game has ended. ");
            System.out.println("The winner is... " + game.get_winner());
        }
    }

    public void printBoardGUI() {
        game.printBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                buttons[i][j].setText(board[i][j]);
//                if (board[i][j] != null)
//                    buttons[i][j].setText(board[i][j]);
//                if (buttons[i][j].getText().equals("X"))
//                    buttons[i][j].setBackground(Color.BLACK);
//                else if (buttons[i][j].getText().equals("O"))
//                    buttons[i][j].setBackground(Color.WHITE);
            }
        }
        if (game.getTurn() == 'W'){
            ArrayList<ArrayList<Integer>> valid = new ArrayList<>();
            valid = game.get_valid_moves();
            for(int m = 0; m < valid.size(); m++){
                int index0 = valid.get(m).get(0);
                int index1 = valid.get(m).get(1);
                buttons[index0][index1].setText(".");
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                othelloGui q = new othelloGui();
            }
        });
    }


}