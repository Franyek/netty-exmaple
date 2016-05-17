package org.ch24.networking.game;

import org.ch24.networking.game.game.GameThread;

public class Main {

    public static void main(String[] args) throws Exception {
        new GameThread().start();
    }
}
