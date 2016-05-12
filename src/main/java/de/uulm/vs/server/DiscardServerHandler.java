package de.uulm.vs.server;


import com.google.gson.Gson;
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
        
        ByteBuf in = (ByteBuf) msg;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            while (in.isReadable()) {
                stringBuffer.append((char) in.readByte());
            }
            Gson gson = new Gson();

            PObject object = gson.fromJson(stringBuffer.toString(), PObject.class);
            System.out.print(stringBuffer.toString());
            System.out.flush();

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
