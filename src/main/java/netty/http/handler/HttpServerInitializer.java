package netty.http.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	private DispatchHandler requestHandler = new DispatchHandler();

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("logger", new LoggingHandler(LogLevel.DEBUG));
		ch.pipeline().addLast("com.Don.http-decoder", new HttpRequestDecoder());
		ch.pipeline().addLast("com.Don.http-aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
		ch.pipeline().addLast("com.Don.http-encoder", new HttpResponseEncoder());
		ch.pipeline().addLast("com.Don.http-chunked", new ChunkedWriteHandler());
		ch.pipeline().addLast("handler", requestHandler);
	}
}