package de.kaleidox.dedricks.part;

import de.kaleidox.dedricks.Board;
import java.awt.Color;
import java.awt.Point;

public class J extends Piece {
    public final static Point[][] point;

    static {
        point = new Point[4][4];

        point[0][0] = new Point(0, 0); // origin / center
        point[0][1] = new Point(1, 1); // bottom
        point[0][2] = new Point(-1, 0); // left
        point[0][3] = new Point(1, 0); // right

        point[0][0] = new Point(0, 0); // origin / center
        point[0][1] = new Point(-1, 1); // bottom
        point[0][2] = new Point(0, -1); // left
        point[0][3] = new Point(0, 1); // right

        point[0][0] = new Point(0, 0); // origin / center
        point[0][1] = new Point(-1, -1); // bottom
        point[0][2] = new Point(1, 0); // left
        point[0][3] = new Point(-1, 0); // right

        point[0][0] = new Point(0, 0); // origin / center
        point[0][1] = new Point(1, -1); // bottom
        point[0][2] = new Point(0, 1); // left
        point[0][3] = new Point(0, -1); // right
    }

    protected J(Board board, int startX, Color color) {
        super('J', board, startX, color);
    }
}
