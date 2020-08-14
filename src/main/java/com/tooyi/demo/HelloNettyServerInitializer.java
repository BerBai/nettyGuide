package com.tooyi.demo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化器，channel注册之后，会执行里边的响应的初始化方法
 * @author tooyi
 */
public class HelloNettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 通过SocketChannel获取对应的管道
        ChannelPipeline pipeline = socketChannel.pipeline();
         /*
          * 通过管道添加handler
          * HttpServerCode: 是由Netty自身提供了助手类，可以理解为拦截器
          * 当请求到服务器，我们需要解码，响应到客服端做编码
         */
        pipeline.addLast("HttpServerCode",new HttpServerCodec());
        // 添加自定义助手类，给客户端浏览器Hello Netty
        pipeline.addLast("CustomHandler",new CustomHandler());
    }
}
