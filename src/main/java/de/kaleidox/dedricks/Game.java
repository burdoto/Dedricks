package de.kaleidox.dedricks;

import de.kaleidox.dedricks.part.Piece;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Game {
    private int TICK_SPEED_MS = 1300;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> nextAction;
    private JFrame root;
    private KeyListener keyEvents;
    Piece currentElement;
    public final Random rnd;
    private int nextId;
    Motion nextMotion;
    private Board board;
    private boolean over = false;

    public Game() {
        scheduler= Executors.newSingleThreadScheduledExecutor();
        keyEvents = new KeyEvents(this);
        rnd = new Random();
        nextId = rnd.nextInt(5);
    }

    public void init() {
        initWindow();
    }

    public void start() {
        newPiece();
        scheduleStep();
    }

    public synchronized void step() {
        if (!over) {
            if (currentElement.canMove(Motion.DOWN)) currentElement.move(Motion.DOWN);
            else newPiece();
            scheduleStep();
        }
    }

    public void input(Motion motion) {
        if (motion == Motion.FULL_DOWN) {
            nextAction.cancel(true);
            while (currentElement.canMove(Motion.DOWN)) {
                try {
                    currentElement.move(Motion.DOWN);
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            drop();
            newPiece();
            return;
        } else {
            if (motion == Motion.DOWN) nextAction.cancel(true);
            if (currentElement == null) newPiece();
            if (currentElement.canMove(motion)) currentElement.move(motion);
        }
        scheduleStep();
    }

    private void newPiece() {
        drop();
        int startX = (board.getX() / 2)-1;
        currentElement = Piece.get(this, board, randomColor(), startX, rnd.nextInt(50));
        if (!currentElement.draw(startX, 0, rnd.nextInt(12)))
            over = true;
    }

    private Color randomColor() {
        switch (rnd.nextInt(4)) {
            case 0:
                return Color.RED;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
        }
        throw new AssertionError();
    }

    private void scheduleStep() {
        if (nextAction != null) if (!nextAction.isCancelled()) nextAction.cancel(true);
        nextAction = scheduler.schedule(this::step, TICK_SPEED_MS, TimeUnit.MILLISECONDS);
    }

    public void drop() {
        currentElement = null;
        if (TICK_SPEED_MS > 300) TICK_SPEED_MS -= 20;
    }

    private void initWindow() {
        int optWidth = 200;
        root = new JFrame();
        GridLayout grid = new GridLayout(1, 2, 0, 10);

        this.board = new Board(10, 25);
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
