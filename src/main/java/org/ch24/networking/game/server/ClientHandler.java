package org.ch24.networking.game.server;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    Gson gson = new Gson();

    public static final Map<SocketAddress, InputMessage> inputPackets = Collections.synchronizedMap(new HashMap<>());

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
}
