package netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.http.handler.HttpServerInitializer;
import org.apache.log4j.Logger;

/**
 * Created by zxw on 2016/1/13.
 */
public class HttpServerStarter {

    Logger logger = Logger.getLogger(this.getClass());

    private int port = 9191;

    HttpServerInitializer httpServerInitializer = new HttpServerInitializer();

    public HttpServerStarter(int port){
        this.port = port;
    }

    public HttpServerStarter(){}

    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(20);
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()-1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(httpServerInitializer);

            logger.info("start..." + port);
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
