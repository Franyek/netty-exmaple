/*
 * Copyright 2009 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.ch24.networking.game.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {

    private ByteBuf content;
    private ChannelHandlerContext ctx;
    private double cnt;
    private int exponent;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.cnt = 1;
        this.exponent = 1;

        // Initialize the message.
//        content = ctx.alloc().directBuffer(DiscardClient.SIZE).writeZero(DiscardClient.SIZE).writeBytes("alma".getBytes());
        content = ctx.alloc().buffer();

        // Send the initial messages.
        generateTraffic();
        //sendMsg();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        content.release();
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Server is supposed to send nothing, but if it sends something, discard it.
    }


    @Override

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    private void generateTraffic() {
        // Flush the outbound buffer to the socket.
        // Once flushed, generate the same amount of traffic again.
        ctx.writeAndFlush(content.duplicate().retain()).addListener(trafficGenerator);
    }

    private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws InterruptedException {
            if (future.isSuccess()) {
                content.clear().writeBytes(getMessage().getBytes());
                Thread.sleep(2000);
                generateTraffic();
            } else {
                future.cause().printStackTrace();
                future.channel().close();
            }
        }
    };

    private String getMessage() {
//        return (new java.sql.Timestamp(System.currentTimeMillis()) + " Client number: " + DiscardClient.CLIENT_NUMBER + " \n");
        cnt = cnt * Math.pow(10, exponent);
        exponent++;
        return (("{ \"acceleration\": " + cnt + ", \"angle\": " + cnt + " }"));
    }

    public void sendMsg() {
        ByteBuf buffer = ctx.alloc().buffer();
        //buffer.writeBytes("{ \"x\": 1, \"y\": " + 10 + " }", Charset.forName("UTF-8").newEncoder());
        buffer.writeBytes(("{ \"acceleration\": 1, \"angle\": " + 10 + " }").getBytes());
        ctx.writeAndFlush(buffer);
    }
}
