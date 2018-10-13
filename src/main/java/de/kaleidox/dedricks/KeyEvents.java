package de.kaleidox.dedricks;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEvents implements KeyListener {
    private final Game game;

    public KeyEvents(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char keyChar = e.getKeyChar();
        Motion motion = Motion.getByKey(keyChar);

        if (motion == null) {
            // no valid motion
        } else game.input(motion);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
