import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MinesweeperView implements MouseListener, ActionListener{
    private MinesweeperController controller;
    private JTextField rowField;
    private JTextField columnField;
    private JTextField bombField;
    private JFrame intro;
    private MyButton[][] cells;
    private JButton submit;
    private JButton endLose;
    private JFrame gameOver;
    private JButton endWin;
    private JFrame victory;
    private JLabel flags;


    MinesweeperView() {
        intro = new JFrame();
        GridBagLayout introLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.ipady = 25;
        c.ipadx = 5;
        intro.setLayout(introLayout);
        addObjects(new JLabel("Welcome to Minesweeper", SwingConstants.CENTER), intro, introLayout, c, 0, 0, 2, 2);
        addObjects(new JLabel("Enter number of rows:"), intro, introLayout, c, 0, 2, 1, 2);
        addObjects(rowField = new JTextField("50", 20), intro, introLayout, c, 1, 2, 1, 2);
        addObjects(new JLabel("Enter number of columns:"), intro, introLayout, c, 0, 4, 1, 2);
        addObjects(columnField = new JTextField("50", 20), intro, introLayout, c, 1, 4, 1, 2);
        addObjects(new JLabel("Enter number of bombs:"), intro, introLayout, c, 0, 6, 1, 2);
        addObjects(bombField = new JTextField("250", 20), intro, introLayout, c, 1, 6, 1, 2);
        submit = new JButton("Submit");
        submit.addActionListener(this);
        addObjects(submit, intro, introLayout, c, 0, 8, 2, 1);
        intro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        intro.pack();
        intro.setLocationRelativeTo(null);
        intro.setVisible(true);
    }

    void setGameWindow(int rows, int cols, int bombs){
        cells = new MyButton[rows][cols];
        JPanel game = new JPanel();
        GridBagLayout gameLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 0;
        c.ipady = 0;
        game.setLayout(gameLayout);
        ImageIcon empty = new ImageIcon("Sprites\\TileU.png");
        for (int i = 0; i< rows; i++) {
            for (int j = 0; j < cols; j++) {
                MyButton cell = new MyButton(i, j);
                cell.setBorder(BorderFactory.createEmptyBorder());
                cell.setContentAreaFilled(false);
                cell.setPreferredSize(new Dimension(16,16));
                cell.addMouseListener(this);
                cell.setIcon(empty);
                cells[i][j] = cell;
                addObjects(cell, game, gameLayout, c, i, j, 1, 1);
            }
        }
        JScrollPane gameScroll = new JScrollPane(game);
        flags = new JLabel("You have " + bombs + " flags remaining.");
        JFrame gameWindow = new JFrame("Minesweeper: " + rows + " x " + cols);
        gameWindow.setLayout(new BorderLayout());
        gameWindow.add(flags, BorderLayout.NORTH);
        gameWindow.add(gameScroll, BorderLayout.CENTER);
        gameWindow.pack();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setLocationRelativeTo(null);
        intro.dispose();
        intro = null;
        gameWindow.setVisible(true);
    }

    void updateCell(int row, int col, String text, int flagCount) {
        cells[row][col].setIcon(new ImageIcon("Sprites\\Tile" + text + ".png"));
        flags.setText("You have " + flagCount + " flags remaining.");
    }

    public void gameOver(int row, int column, int[] time, int found, int safe) {
        cells[row][column].setIcon(new ImageIcon("Sprites\\TileU.png"));
        gameOver = new JFrame();
        GridLayout gameOverLayout = new GridLayout(2,1);
        gameOver.setLayout(gameOverLayout);
        JLabel endText = new JLabel("Game Over! You hit a bomb.\nYou took " + time[0] + ":" + time[1] + " to find " + found + " out of " + safe + " safe spaces.", JLabel.CENTER);
        endText.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        gameOver.add(endText);
        endLose = new JButton("Ok");
        endLose.addActionListener(this);
        gameOver.add(endLose);
        gameOver.pack();
        gameOver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOver.setLocationRelativeTo(null);
        gameOver.setVisible(true);
    }

    public void victory(int[] time) {
        victory = new JFrame();
        GridLayout victoryLayout = new GridLayout(2,1);
        victory.setLayout(victoryLayout);
        JLabel endText = new JLabel("You Win! You found all the bombs in " + time[0] + ":" + time[1] + ".", JLabel.CENTER);
        endText.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        victory.add(endText);
        endWin = new JButton("Ok");
        endWin.addActionListener(this);
        victory.add(endWin);
        victory.pack();
        victory.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        victory.setLocationRelativeTo(null);
        victory.setVisible(true);
    }

    void registerController(MinesweeperController c){
        controller = c;
    }

    static void addObjects(Component component, Container yourcontainer, GridBagLayout layout, GridBagConstraints gbc, int gridx, int gridy, int gridWidth, int gridHeight){

        gbc.gridx = gridx;
        gbc.gridy = gridy;

        gbc.gridwidth = gridWidth;
        gbc.gridheight = gridHeight;

        layout.setConstraints(component, gbc);
        yourcontainer.add(component);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == submit) {
            int rows, cols, bombs;
            rows = Integer.parseInt(rowField.getText());
            cols = Integer.parseInt(columnField.getText());
            bombs = Integer.parseInt(bombField.getText());
            controller.startGame(rows, cols, bombs);
            return;
        }
        if (source == endLose) {
            gameOver.dispose();
            System.exit(0);
        }
        if (source == endWin) {
            victory.dispose();
            System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MyButton source = (MyButton) e.getComponent();
        int[] location = source.getData();
        //reveal an empty cell
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (controller.isRevealed(location[0],location[1])) {
                controller.revealSurrounding(location[0], location[1]);
            } else {
                controller.revealCell(location[0], location[1]);
            }
        //swap the flag state
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            controller.flagCell(location[0], location[1]);
        } 

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }
}
