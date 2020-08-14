package com.tooyi;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现客户端发送请求，服务器给予响应
 * @author tooyi
 */
public class HelloNettyServer {
    public static void main(String[] args) throws Exception {
        // 创建一组线程池组
        // 主线程池：用于接收客户端的请求链接，不做任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程池：主线程池会将任务交给从线程池，进行操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置主从线程组
            serverBootstrap.group(bossGroup,workerGroup)
                    // 设置NIO双向通道
                    .channel(NioServerSocketChannel.class)
                    // 添加子处理器 用于处理从线程池的任务
                    .childHandler(new HelloNettyServerInitializer());
            // 启动服务，并设置端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            // 监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭主线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
