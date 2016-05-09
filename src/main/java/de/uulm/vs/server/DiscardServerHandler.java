package de.uulm.vs.server;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.logging.Logger;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = Logger.getLogger(DiscardServerHandler.class.getName());

    //This method is called with the received message, whenever new data is received from a client.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
//TODO: Reading msg form different pipelines in a same time -> separate the msg from different pipelines

// This code print the message to the console
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { // (1)
//                LOGGER.info(in.toString());
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
