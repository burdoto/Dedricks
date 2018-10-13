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
    private final static int BLOCK_SIDE_SIZE = 25;
    public final static String NAME_PATTERN = "%s/%s";
    private final GridLayout grid;
    private final List<Button> blocks;
    private final int x;
    private final int y;
    private final Dimension totalDim;

    @Override
    public int getWidth() {
        return totalDim.width;
    }

    @Override
    public int getHeight() {
        return totalDim.height;
    }

    public Board(int x, int y) {
        super();
        this.totalDim = new Dimension(x * BLOCK_SIDE_SIZE, y * BLOCK_SIDE_SIZE);
        setMinimumSize(totalDim);
        setMaximumSize(totalDim);
        this.x = x;
        this.y = y;
        this.grid = makeGrid();
        this.blocks = new ArrayList<>();

        setLayout(grid);
        initPanels();
    }

    private GridLayout makeGrid() {
        GridLayout grid = new GridLayout();
        grid.setColumns(x);
        grid.setRows(y);
        grid.layoutContainer(this);

        return grid;
    }

    private void initPanels() {
        Dimension dim = new Dimension(BLOCK_SIDE_SIZE, BLOCK_SIDE_SIZE);
        for (int y = 0; y < this.y; y++) {
            for (int x = 0; x < this.x; x++) {
                Button btn = new Button();
                btn.setName(blockName(x, y));
                btn.setVisible(true);
                btn.setMinimumSize(dim);
                btn.setMaximumSize(dim);
                btn.setEnabled(false);
                btn.setBackground(Color.LIGHT_GRAY);
                blocks.add(btn);
                add(btn);
                grid.addLayoutComponent(btn.getName(), btn);
            }
        }
    }

    public Button blockByPos(int x, int y) throws IndexOutOfBoundsException, NoSuchElementException {
        if (x < 0 || x > this.x) throw new IndexOutOfBoundsException("X is too "+(x < 0 ? "small" : "big")+"!");
        if (y < 0 || y > this.y) throw new IndexOutOfBoundsException("Y is too "+(y < 0 ? "small" : "big")+"!");
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

    private static String blockName(int x, int y) {
        return String.format(NAME_PATTERN, x, y);
    }
}
