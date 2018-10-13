package de.kaleidox.dedricks.part;

import de.kaleidox.dedricks.Board;

import de.kaleidox.dedricks.Game;
import java.awt.*;

public class T extends Piece {
    public final static Point[][] point;

    static {
        point = new Point[4][4];

        point[0] = new Point[4];
        point[0][0] = new Point(0, 0); // origin / center
        point[0][1] = new Point(0, 1); // bottom
        point[0][2] = new Point(-1, 0); // left
        point[0][3] = new Point(1, 0); // right

        point[1] = new Point[4];
        point[1][0] = new Point(0, 0); // origin / center
        point[1][1] = new Point(-1, 0); // bottom
        point[1][2] = new Point(0, 1); // left
        point[1][3] = new Point(0, -1); // right

        point[2] = new Point[4];
        point[2][0] = new Point(0, 0); // origin / center
        point[2][1] = new Point(0, -1); // bottom
        point[2][2] = new Point(1, 0); // left
        point[2][3] = new Point(-1, 0); // right

        point[3] = new Point[4];
        point[3][0] = new Point(0, 0); // origin / center
        point[3][1] = new Point(1, 0); // bottom
        point[3][2] = new Point(0, -1); // left
        point[3][3] = new Point(0, 1); // right
    }

    protected T(Board board, Game game, int startX, Color color) {
        super('T', game, board, startX, color, new int[]{0,1,1,1});
    }
}
