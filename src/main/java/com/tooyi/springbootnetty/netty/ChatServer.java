package com.tooyi.springbootnetty.netty;

import com.tooyi.websocket.ChatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component
public class ChatServer {
    private static class SingletionChatServer {
        static final ChatServer instance = new ChatServer();
    }

    public static ChatServer getInstance() {
        return SingletionChatServer.instance;
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    public ChatServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChatServerInitializer());
    }

    public void start() {
        this.channelFuture = serverBootstrap.bind(8888);
        System.out.println("Netty 服务器启动成功...");
    }

    public static void main(String[] args) {
        EventLoopGroup bossGroup1 = new NioEventLoopGroup();
        EventLoopGroup workerGroup1 = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup1, workerGroup1)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitializer());
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup1.shutdownGracefully();
            workerGroup1.shutdownGracefully();
        }
    }
}
