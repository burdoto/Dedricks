package de.kaleidox.dedricks;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.JPanel;

public class Board extends JPanel {
    private final GridLayout grid;
    private final List<Block> blocks;
    private final int x;
    private final int y;
    private final Dimension totalDim;

    public Board(int x, int y) {
        super();
        this.x = x + 1;
        this.y = y + 1;
        this.totalDim = new Dimension(this.x * Block.BLOCK_SIDE_SIZE, this.y * Block.BLOCK_SIDE_SIZE);
        setMinimumSize(totalDim);
        setMaximumSize(totalDim);
        this.grid = makeGrid();
        this.blocks = new ArrayList<>();

        setLayout(grid);
        initPanels();
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
        Dimension dim = new Dimension(Block.BLOCK_SIDE_SIZE, Block.BLOCK_SIDE_SIZE);
        for (int y = -1; y < this.y; y++) {
            for (int x = -1; x < this.x; x++) {
                Block block = new Block(this, x, y);
                blocks.add(block);
            }
        }
    }

    public Block blockByPos(int x, int y) throws IndexOutOfBoundsException, NoSuchElementException {
        if (x < -1 || x > this.x-1) throw new IndexOutOfBoundsException("X is too " + (x < 0 ? "small" : "big") + "!");
        if (y < -1 || y > this.y-1) throw new IndexOutOfBoundsException("Y is too " + (y < 0 ? "small" : "big") + "!");
        return blocks.stream()
                .filter(blk -> blk.x == x && blk.y == y)
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    public Block blockByPos(Point point) throws IndexOutOfBoundsException, NoSuchElementException {
        return blockByPos(point.x, point.y);
    }
}
