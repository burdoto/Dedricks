package de.kaleidox.dedricks;

import java.awt.Point;
import java.util.function.Function;

public enum Motion implements Function<Point, Point> {
    DOWN('s', p -> new Point(p.x, p.y+1)),
    LEFT('a', p -> new Point(p.x-1, p.y)),
    RIGHT('d', p -> new Point(p.x+1, p.y)),
    FULL_DOWN('x'),
    ROTATE_LEFT('q'),
    ROTATE_RIGHT('e');

    private final char key;
    private final Function<Point, Point> pointChanger;

    Motion(char key, Function<Point, Point> pointChanger) {
        this.key = key;
        this.pointChanger = pointChanger;
    }

    Motion(char key) {
        this.key = key;
        this.pointChanger = p -> p;
    }

    @Override
    public Point apply(Point point) {
        return pointChanger.apply(point);
    }

    public static Motion getByKey(char key) {
        for (Motion value : values()) if (value.key == key) return value;
        return null;
    }
}
