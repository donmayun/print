package netty.http;

import io.netty.channel.ChannelHandlerContext;
import netty.http.context.HttpMethodRequest;
import netty.http.context.HttpMethodResponse;

/**
 * Created by zxw on 2016/1/15.
 */
public interface Filter {

    public boolean before(ChannelHandlerContext ctx, HttpMethodRequest req, HttpMethodResponse resp);

    public void after(ChannelHandlerContext ctx, HttpMethodRequest req, HttpMethodResponse resp);

    public String pattern();

}
