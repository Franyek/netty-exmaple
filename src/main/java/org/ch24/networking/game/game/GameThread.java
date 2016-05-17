package org.ch24.networking.game.game;

import org.ch24.networking.game.server.ClientHandler;
import org.ch24.networking.game.server.DiscardServer;
import org.ch24.networking.game.server.InputMessage;

import java.net.SocketAddress;
import java.util.Map;

public class GameThread extends Thread {

    private DiscardServer server;

    public GameThread() throws InterruptedException {
        this.server = new DiscardServer(8080);
        server.start();
    }

    @Override
    public void run() {
        try {
            //Waiting for inputs
            sleep(20000);
            Map<SocketAddress, InputMessage> map = ClientHandler.inputPackets;
            server.workerGroup.forEach(event -> event.isShutdown());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
