package de.kaleidox.dedricks;

import de.kaleidox.dedricks.part.Piece;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Game {
    private JFrame root;
    private KeyListener keyEvents;
    Piece currentElement;
    public final Random rnd;
    private int nextId;
    Motion nextMotion;

    public Game() {
        keyEvents = new KeyEvents(this);
        rnd = new Random();
        nextId = rnd.nextInt(5);
    }

    public void step() {
        if (nextId == -1 && currentElement != null) nextId = rnd.nextInt(5);
        else if (currentElement == null) {
        }
    }

    void drop() {
        currentElement = null;
    }

    public void init() {
        initWindow();
    }

    private void initWindow() {
        int optWidth = 200;
        root = new JFrame();
        GridLayout grid = new GridLayout(1, 2, 0, 10);

        JPanel board = new Board(10, 25);
        JPanel opt = new JPanel();

        board.setVisible(true);
        opt.setVisible(true);

        root.add(board);
        grid.addLayoutComponent("board", board);
        root.add(opt);
        grid.addLayoutComponent("options", opt);

        Dimension dim = new Dimension(board.getWidth() + optWidth, board.getHeight());
        root.addKeyListener(keyEvents);
        root.setMinimumSize(dim);
        root.setMaximumSize(dim);
        root.setResizable(false);
        root.setLayout(grid);
        root.setVisible(true);
        root.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
