package netty.http.handler;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import netty.http.ExceptionResolver;
import netty.http.ServerConfig;
import netty.http.ServerContextManager;
import netty.tools.cache.Cache;
import netty.tools.cache.DefaultCache;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Don on 2016/1/13.
 */
@ChannelHandler.Sharable
public class DispatchHandler extends SimpleChannelInboundHandler<Object> {

    public static String resourceUrl = "/webapps";
    private final static String handleSuffix = ".do";
    private FileResource fileResource;
    private Cache<String, Map<String, Object>> sessionCache = new DefaultCache<>("session_cache", 2000000, 20*60*1000);

    private HttpRequestMethodHandler methodHandler = new HttpRequestMethodHandler();

    public DispatchHandler(String resourceUrl) {
        this.resourceUrl = resourceUrl;
        fileResource = new FileResource(resourceUrl);
    }

    public DispatchHandler(){
        fileResource = new FileResource(resourceUrl);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             Object msg) throws Exception {
        if(msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (!request.getDecoderResult().isSuccess()) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                return;
            }
        /*if (request.getMethod() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }*/

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            Map<String, Object> session = getSession(request);
            if (session == null) {
                synchronized (this) {
                    session = getSession(request);
                    if (session == null) {
                        session = createSession(response);
                    }
                }
            }

            //websocket
            if("websocket".equals(request.headers().get("Upgrade"))){
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                        ServerConfig.getProperty("ws.connection.url"), null, false);
                WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
                if (handshaker == null) {
                    WebSocketServerHandshakerFactory
                            .sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    handshaker.handshake(ctx.channel(), request);
                }
                return;
            }

            String uri = request.getUri();
            QueryStringDecoder decoder = new QueryStringDecoder(uri);
            if (decoder.path().endsWith(handleSuffix)) {
                try {
                    methodHandler.handle(request, response, session, ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                    List<ExceptionResolver> exceptionResolverList = ServerContextManager.getInstance().getExceptionResolvers();
                    if(exceptionResolverList.size() == 0) {
                        sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
                    }else{
                        for(ExceptionResolver er : exceptionResolverList){
                            er.doResolveException(ctx, e);
                        }
                    }
                    return;
                }
            } else {
                if (StringUtils.isBlank(uri) || "/".equals(uri)) {
                    uri = "/index.html";
                    fileResource.handle(ctx, request, uri);
                }else {
                    fileResource.handle(ctx, request, decoder.path());
                }
            }


            ChannelFuture lastContentFuture = ctx
                    .writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!HttpHeaders.isKeepAlive(request)) {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }else if(msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx, (WebSocketFrame)msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame) {

        if (frame instanceof CloseWebSocketFrame) {
            ctx.channel().writeAndFlush(frame.retain(), ctx.channel().newPromise()).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        String request = ((TextWebSocketFrame) frame).text();
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res, res.content().readableBytes());
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
                mimeTypesMap.getContentType(file.getPath()));
    }

    private Map<String, Object> getSession(HttpRequest request){
        Map<String, String> cookies = getCookies(request);
        String sessionId = cookies.get("JSESSIONID");
        return sessionCache.get(sessionId);
    }

    private Map<String, Object> createSession(HttpResponse response){
        String sessionId = UUID.randomUUID().toString();
        response.headers().set(HttpHeaders.Names.SET_COOKIE, "JSESSIONID="+sessionId+"; Path=/; HttpOnly");
        Map<String, Object> session = new HashMap<>();
        sessionCache.put(sessionId, session);
        return session;
    }

    private static Map<String, String> getCookies(HttpRequest request){
        String cookie =request.headers().get(HttpHeaders.Names.COOKIE);
        if(StringUtils.isNotBlank(cookie)) {
            Map<String, String> cookieMap = new HashMap<>();
            String[] values = cookie.split(";");
            for (String kv : values) {
                String[] args = kv.trim().split("=");
                cookieMap.put(args[0], args[1]);
            }
            return cookieMap;
        }
        return new HashMap<>();
    }

}
