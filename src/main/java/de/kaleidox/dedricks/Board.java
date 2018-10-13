package de.kaleidox.dedricks;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.JPanel;

public class Board extends JPanel {
    public final static String NAME_PATTERN = "%s/%s";
    private final static int BLOCK_SIDE_SIZE = 25;
    private final GridLayout grid;
    private final List<Button> blocks;
    private final int x;
    private final int y;
    private final Dimension totalDim;

    public Board(int x, int y) {
        super();
        this.x = x + 1;
        this.y = y + 1;
        this.totalDim = new Dimension(this.x * BLOCK_SIDE_SIZE, this.y * BLOCK_SIDE_SIZE);
        setMinimumSize(totalDim);
        setMaximumSize(totalDim);
        this.grid = makeGrid();
        this.blocks = new ArrayList<>();

        setLayout(grid);
        initPanels();
    }

    private static String blockName(int x, int y) {
        return String.format(NAME_PATTERN, x, y);
    }

    @Override
    public int getWidth() {
        return totalDim.width;
    }

    @Override
    public int getHeight() {
        return totalDim.height;
    }

    @Override
    public int getX() {
        return x-1;
    }

    @Override
    public int getY() {
        return y-1;
    }

    private GridLayout makeGrid() {
        GridLayout grid = new GridLayout();
        grid.setColumns(x + 1);
        grid.setRows(y + 1);
        grid.layoutContainer(this);

        return grid;
    }

    private void initPanels() {
        Dimension dim = new Dimension(BLOCK_SIDE_SIZE, BLOCK_SIDE_SIZE);
        for (int y = -1; y < this.y; y++) {
            for (int x = -1; x < this.x; x++) {
                Button btn = new Button();
                btn.setName(blockName(x, y));
                btn.setVisible(true);
                btn.setMinimumSize(dim);
                btn.setMaximumSize(dim);
                btn.setEnabled(false);
                btn.setBackground(y == -1 || x == -1 || x == this.x - 1 || y == this.y - 1 ?
                        Color.BLACK : Color.LIGHT_GRAY);
                blocks.add(btn);
                add(btn);
                grid.addLayoutComponent(btn.getName(), btn);
            }
        }
    }

    public Button blockByPos(int x, int y) throws IndexOutOfBoundsException, NoSuchElementException {
        if (x < -1 || x > this.x-1) throw new IndexOutOfBoundsException("X is too " + (x < 0 ? "small" : "big") + "!");
        if (y < -1 || y > this.y-1) throw new IndexOutOfBoundsException("Y is too " + (y < 0 ? "small" : "big") + "!");
        return Arrays.stream(getComponents())
                .filter(comp -> comp instanceof Button)
                .filter(comp -> comp.getName().equals(blockName(x, y)))
                .map(Button.class::cast)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public Button blockByPos(Point point) throws IndexOutOfBoundsException, NoSuchElementException {
        return blockByPos(point.x, point.y);
    }
}
