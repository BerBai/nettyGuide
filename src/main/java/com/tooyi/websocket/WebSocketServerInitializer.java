package com.tooyi.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 初始化器，channel注册之后，会执行里边的响应的初始化方法
 *
 * @author tooyi
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取channel管道
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        // websocket 基于http协议所需要的http编解码器
        channelPipeline.addLast("HttpServerCode",new HttpServerCodec());
        // Netty处理http数据流读写支持
        channelPipeline.addLast("ChunkedWritedHandler",new ChunkedWriteHandler());
        // 对httpMessage进行聚合处理，聚合成request或reponse
        channelPipeline.addLast("HttpObjectAggregator",new HttpObjectAggregator(1024*64));
        // 处理复杂事情，如握手动作handshaking（close/ping/pong）ping+pong=心跳
        // 对于websocket，都是以frams进行传输的，不同的数据类型对应的frams不同
        channelPipeline.addLast("WebSocketServerProtocolHandler",new WebSocketServerProtocolHandler("/ws"));

        channelPipeline.addLast("ChatHandler", new ChatHandler());
    }
}
