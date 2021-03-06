package org.ch24.networking.game.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 */
public class DiscardServer extends Thread{

    public EventLoopGroup bossGroup;
    public EventLoopGroup workerGroup;
    private int port;

    public DiscardServer(int port) throws InterruptedException {
        this.port = port;
    }

    @Override
    public void run() {
// NioEventLoopGroup is a multithreaded event loop that handles I/O operation.
// Netty provides various EventLoopGroup implementations for different kind of transports.
// We are implementing a server-side application in this example, and therefore two NioEventLoopGroup will be used.
// The first one, often called 'boss', accepts an incoming connection.
// The second one, often called 'worker', handles the traffic of the accepted connection once the boss accepts the
// connection and registers the accepted connection to the worker. How many Threads are used and how they are
// mapped to the created Channels depends on the EventLoopGroup implementation and may be even configurable via a constructor.
        this.bossGroup = new NioEventLoopGroup(); // (1)
        this.workerGroup = new NioEventLoopGroup();
        try {
//ServerBootstrap is a helper class that sets up a server. You can set up the server using a Channel directly.
// However, please note that this is a tedious process, and you do not need to do that in most cases.
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    //is used to instantiate a new Channel to accept incoming connections
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer())
//            The handler specified here will always be evaluated by a newly accepted Channel.
//                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
//                        @Override
//                        public void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new DiscardServerHandler());
//                        }
//                    })
//            TCP/IP server, so we are allowed to set the socket options such as tcpNoDelay and keepAlive
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
// option() is for the NioServerSocketChannel that accepts incoming connections.
// childOption() is for the Channels accepted by the parent ServerChannel, which is NioServerSocketChannel in this case.
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = null; // (7)
            try {
                f = b.bind(port).sync();

                // Wait until the server socket is closed.
                // In this example, this does not happen, but you can do that to gracefully
                // shut down your server.
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
