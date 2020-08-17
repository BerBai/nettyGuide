package com.tooyi.springbootnetty.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 处理聊天消息的handler
 *
 * @author tooyi
 */

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 管理所有客户端连接
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext
            , TextWebSocketFrame textWebSocketFrame) throws Exception {
        String msg = textWebSocketFrame.text();
        Channel channel = channelHandlerContext.channel();

        System.out.println("服务器接收到消息：" + msg + " ，时间：" + new Date());

        Channel testchannel = clients.find(channel.id());
        if (testchannel != null) {
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接收到了来自id："
                    + channelHandlerContext.channel().id().asLongText()
                    + "客户端的消息:]" + LocalDateTime.now() + ",消息为:" + msg));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开，连接id：" + ctx.channel().id().asLongText());
        clients.remove(ctx.channel());
    }
}
