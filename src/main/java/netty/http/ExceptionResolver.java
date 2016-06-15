package netty.http;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zxw on 2016/1/15.
 */
public interface ExceptionResolver {

    void doResolveException(ChannelHandlerContext ctx, Throwable throwable);

}
