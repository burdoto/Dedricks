package de.kaleidox.dedricks;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;

public class Block {
    public final static String NAME_PATTERN = "%s/%s";
    public final static int BLOCK_SIDE_SIZE = 25;
    public final static Dimension dim = new Dimension(BLOCK_SIDE_SIZE, BLOCK_SIDE_SIZE);
    public final int x;
    public final int y;
    public final Button button;
    public final Point point;
    private final Board board;

    public Block(Board board, int x, int y) {
        LayoutManager grid = board.getLayout();

        this.board = board;
        this.x = x;
        this.y = y;
        this.point = new Point(x, y);
        this.button = new Button();

        button.setName(blockName(x, y));
        button.setVisible(true);
        button.setMinimumSize(dim);
        button.setMaximumSize(dim);
        button.setEnabled(false);
        button.setBackground(y == -1 || x == -1 || x == this.x - 1 || y == this.y - 1 ?
                Color.BLACK : Color.LIGHT_GRAY);
        board.add(button);
        grid.addLayoutComponent(button.getName(), button);
    }

    public static String blockName(int x, int y) {
        return String.format(NAME_PATTERN, x, y);
    }
}
