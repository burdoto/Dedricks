package de.kaleidox.dedricks.part;

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
    protected Button[] blocks;
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

    static Button[] getBlocks(Board board, final Point[] points, int x, int y) {
        Button[] block = new Button[points.length];
        for (int i = 0; i < points.length; i++)
            block[i] = board.blockByPos(x + points[i].x, y + points[i].y);
        return block;
    }

    static Point getPoint(final Button button) {
        String split = Board.NAME_PATTERN.replace("%s", "");
        String[] coor = button.getName().split(split);
        return new Point(Integer.parseInt(coor[0]), Integer.parseInt(coor[1]));
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
        Button[] blocks = getBlocks(board, point[tilt], x, y);
        for (Button block : blocks) {
            if (block.getBackground().getRGB() != Color.LIGHT_GRAY.getRGB()) return false;
            block.setBackground(color);
            block.setLabel(type+"");
        }
        this.blocks = blocks;
        origin = new Point(x, y);
        return true;
    }

    public Point[][] simulateMove(Motion motion) {
        Point[][] point = getBaseArray(type);
        Point[][] ps = new Point[2][4];
        for (int i = 0; i < blocks.length; i++)
            ps[0][i] = getPoint(blocks[i]);
        switch (motion) {
            case DOWN:
            case LEFT:
            case RIGHT:
                Point[] block = new Point[blocks.length];
                for (int i = 0; i < blocks.length; i++) block[i] = motion.apply(getPoint(blocks[i]));
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
            Button btn = board.blockByPos(point);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setLabel("");
        }
        blocks = new Button[4];
        int i = 0;
        for (Point point : points[1]) {
            Button btn = board.blockByPos(point);
            btn.setBackground(color);
            btn.setLabel(type+"");
            blocks[i++] = btn;
        }
    }

    public boolean canMove(Motion motion) {
        Point[] affected = simulateMove(motion)[1];
        for (Point point : affected) {
            Button btn = board.blockByPos(point);
            if (btn.getBackground().getRGB() == Color.BLACK.getRGB()) return false;
            if (btn.getBackground().getRGB() != Color.LIGHT_GRAY.getRGB()) {
                boolean own = false;
                for (Button block : blocks) if (btn.getName().equals(block.getName())) own = true;
                if (!own) return false;
            }
        }
        return true;
    }
}
