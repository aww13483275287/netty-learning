package com.crossoverjie.netty.websocket.learning.channel.initial;

import com.crossoverjie.netty.websocket.learning.handle.HttpRequestHandle;
import com.crossoverjie.netty.websocket.learning.handle.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 24/02/2018 19:52
 * @since JDK 1.8
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

    private ChannelGroup group ;

    public ChatServerInitializer(ChannelGroup group){
        this.group = group ;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //http 服务器请求的 编码器和解码器的聚合
        pipeline.addLast(new HttpServerCodec()) ;

        //写文件
        pipeline.addLast(new ChunkedWriteHandler()) ;

        pipeline.addLast(new HttpObjectAggregator(64 * 1024)) ;

        //处理不是 /ws 的 http 请求
        pipeline.addLast(new HttpRequestHandle("/ws")) ;

        //处理 websocket 升级  handler
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws")) ;

        //真正的 websocket 处理文本 handler
        pipeline.addLast(new TextWebSocketFrameHandler(group)) ;




    }
}
