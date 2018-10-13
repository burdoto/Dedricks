package de.kaleidox.dedricks.part;

import de.kaleidox.dedricks.Board;
import de.kaleidox.dedricks.Motion;
import java.awt.Button;
import java.awt.Color;
import java.awt.Point;

public abstract class Piece {
    protected final char type;
    protected final Board board;
    protected final int startX;
    protected final Color color;
    protected Button[] blocks;
    protected Point origin;
    protected int tilt = 0;

    protected Piece(char type, Board board, int startX, Color color) {
        this.type = type;
        this.board = board;
        this.startX = startX;
        this.color = color;
    }

    public void draw(int x, int y, int tilt) {
        Point[][] point = getBaseArray(type);
        tilt = tilt % 4; // ensure the tilt is not bigger than 4
        if (tilt != 0) y++; // because its T, if the tilt is not 0; move the origin down by 1
        Button[] blocks = getBlocks(board, point[tilt], x, y);
        for (Button block : blocks) block.setBackground(color);
        this.blocks = blocks;
        origin = new Point(x, y);
    }

    public Point[][] simulateMove(Motion motion) {
        Point[][] point = getBaseArray(type);
        Point[][] ps = new Point[2][4];
        for (int i = 0; i < blocks.length; i++) ps[0][i] = getPoint(blocks[i]);
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
        for (Point point : points[0]) board.blockByPos(point).setBackground(Color.LIGHT_GRAY);
        for (Point point : points[1]) board.blockByPos(point).setBackground(color);
    }

    public boolean canMove(Motion motion) {
        Point[] affected = simulateMove(motion)[1];
        for (Point point : affected)
            if (board.blockByPos(point).getBackground() != Color.LIGHT_GRAY) return false;
        return true;
    }

    static Button[] getBlocks(Board board, final Point[] points, int x, int y) {
        Button[] block = new Button[points.length];
        for (int i = 0; i < points.length; i++) block[i] = board.blockByPos(x + points[i].x, y + points[i].y);
        return block;
    }

    static Point getPoint(final Button button) {
        String split = Board.NAME_PATTERN.replace("%s", "");
        String[] coor = button.getName().split(split);
        int i1 = -1, i2;
        String one = "", two = "";
        while (one.isEmpty()) one = coor[++i1];
        i2 = i1 + 1;
        while (two.isEmpty()) two = coor[++i2];
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
}
