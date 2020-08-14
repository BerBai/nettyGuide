package com.tooyi.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalTime;

/**
 * 处理消息助手类
 * 数据chuans使用的frame，在Netty中是用于wewebsocket专门处理文本对象的，是消息的载体
 * 此类名：TextWebSocketFrame
 *
 * @author tooyi
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 用于记录管理所有的客户端channel
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输的消息数据
        String content = msg.text();
        System.out.println("接收数据：" + content);

        // 将数据刷新到客户端上
        channels.writeAndFlush(
                new TextWebSocketFrame(
                        "[服务器在]" + LocalTime.now()
                                + "接收到的消息内容为：" + content
                )
        );

    }

    /**
     * 客户端连接 获取双向通道
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());

    }

    /**
     * 客户端离开 移除通道
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 客户端离开时会自动移除
//        channels.remove(ctx.channel());
        System.out.println("客户端离开，channel对应的长id：" + ctx.channel().id().asLongText());
        System.out.println("客户端离开，channel对应的短id：" + ctx.channel().id().asShortText());
    }
}
