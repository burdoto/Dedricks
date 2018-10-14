package de.kaleidox.dedricks.part;

import de.kaleidox.dedricks.Block;
import de.kaleidox.dedricks.Board;
import de.kaleidox.dedricks.Game;
import de.kaleidox.dedricks.Motion;
import java.awt.Button;
import java.awt.Color;
import java.awt.Point;

public abstract class Piece {
    protected final char type;
    protected final Board board;
    protected final int startX;
    protected final Color color;
    protected final int[] tiltY;
    protected Block[] blocks;
    protected Point origin;
    protected int tilt = 0;
    private Game game;

    protected Piece(char type, Game game, Board board, int startX, Color color, int[] tiltY) {
        this.type = type;
        this.game = game;
        this.board = board;
        this.startX = startX;
        this.color = color;
        this.tiltY = tiltY;
    }

    static Block[] getBlocks(Board board, final Point[] points, int x, int y) {
        Block[] block = new Block[points.length];
        for (int i = 0; i < points.length; i++)
            block[i] = board.blockByPos(x + points[i].x, y + points[i].y);
        return block;
    }

    static Point applyPoint(Point p, Point o) {
        return new Point(p.x + o.x, p.y + o.y);
    }

    static Point[][] getBaseArray(char type) {
        switch (type) {
            case 'T':
                return T.point;
            case 'O':
                return O.point;
            case 'I':
                return I.point;
            case 'J':
                return J.point;
            case 'L':
                return L.point;
        }
        throw new AssertionError();
    }

    public static Piece get(Game game, Board board, Color color, int startX, int i) {
        switch (i % 5) {
            case 0:
                return new T(board, game, startX, color);
            case 1:
                return new O(board, game, startX, color);
            case 2:
                return new I(board, game, startX, color);
            case 3:
                return new J(board, game, startX, color);
            case 4:
                return new L(board, game, startX, color);
        }
        throw new AssertionError();
    }

    public boolean draw(int x, int y, int tilt) {
        Point[][] point = getBaseArray(type);
        tilt = tilt % 4; // ensure the tilt is not bigger than 4
        if (tilt != 0) y += tiltY[tilt];
        Block[] blocks = getBlocks(board, point[tilt], x, y);
        for (Block block : blocks) {
            if (block.button.getBackground().getRGB() != Color.LIGHT_GRAY.getRGB()) return false;
            block.button.setBackground(color);
            block.button.setLabel(type+"");
        }
        this.blocks = blocks;
        origin = new Point(x, y);
        return true;
    }

    public Point[][] simulateMove(Motion motion) {
        Point[][] point = getBaseArray(type);
        Point[][] ps = new Point[2][4];
        for (int i = 0; i < blocks.length; i++)
            ps[0][i] = blocks[i].point;
        switch (motion) {
            case DOWN:
            case LEFT:
            case RIGHT:
                Point[] block = new Point[blocks.length];
                for (int i = 0; i < blocks.length; i++) block[i] = motion.apply(blocks[i].point);
                ps[1] = block;
                return ps;
            case ROTATE_LEFT:
                tilt--;
                if (tilt == -1) tilt = 3;
                break;
            case ROTATE_RIGHT:
                tilt++;
                if (tilt == 4) tilt = 0;
                break;
            default:
                throw new AssertionError();
        }
        for (int i = 0; i < ps[0].length; i++) ps[1][i] = applyPoint(point[tilt][i], origin);
        return ps;
    }

    public void move(Motion motion) {
        Point[][] points = simulateMove(motion);
        for (Point point : points[0]) {
            Button btn = board.blockByPos(point).button;
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setLabel("");
        }
        blocks = new Block[4];
        int i = 0;
        for (Point point : points[1]) {
            Block btn = board.blockByPos(point);
            btn.button.setBackground(color);
            btn.button.setLabel(type+"");
            blocks[i++] = btn;
        }
    }

    public boolean canMove(Motion motion) {
        Point[] affected = simulateMove(motion)[1];
        for (Point point : affected) {
            Block blk = board.blockByPos(point);
            if (blk.button.getBackground().getRGB() == Color.BLACK.getRGB()) return false;
            if (blk.button.getBackground().getRGB() != Color.LIGHT_GRAY.getRGB()) {
                boolean own = false;
                for (Block block : blocks) if (blk.button.getName().equals(block.button.getName())) own = true;
                if (!own) return false;
            }
        }
        return true;
    }
}
