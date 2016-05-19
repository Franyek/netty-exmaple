package org.ch24.networking.game.server;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    Gson gson = new Gson();

    public static final Map<SocketAddress, InputMessage> inputPackets = Collections.synchronizedMap(new HashMap<>());
    //3 exception points and the team banned from the game
    //TeamID-exception points
//    private Map<Integer, Integer> exceptionNumbers = new HashMap<>();
    private Map<SocketAddress, Integer> exceptionNumbers = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        InputMessage inputMessage = gson.fromJson(msg, InputMessage.class);
        inputPackets.put(remoteAddress, inputMessage);
        System.err.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handleMsg(SocketAddress address, String msg){
        //TODO Get team number by IP address and uses that number in the game
        //String teamID
        SocketAddress teamID = address;

        int badPoints = 0;

        if (exceptionNumbers.containsKey(teamID)) {
            badPoints = this.exceptionNumbers.get(teamID);
        }

        this.exceptionNumbers.put(teamID, ++badPoints);

        if (badPoints < 3) {
            //TODO team is part of the game and msg should be handled
        } else {
            //TODO team out of the game
        }
    }
}
