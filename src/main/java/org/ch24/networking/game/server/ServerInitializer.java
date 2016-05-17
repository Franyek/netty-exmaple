package org.ch24.networking.game.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class ServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(100, false, new ByteBuf[]{
                Unpooled.wrappedBuffer(new byte[]{'}'})}));
        ch.pipeline().addLast(new StringDecoder());
        ch.pipeline().addLast(new ClientHandler());
    }
}
