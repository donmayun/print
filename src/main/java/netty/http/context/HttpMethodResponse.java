package netty.http.context;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.Charset;

/**
 * Created by zxw on 2016/1/13.
 */
public class HttpMethodResponse {

    private HttpResponseStatus status = HttpResponseStatus.OK;
    private ByteBuf byteBuf;
    private int contentLength = 0;

    public HttpMethodResponse(){
        byteBuf = Unpooled.buffer(1024);
    }

    public void write(byte[] bytes){
        contentLength = contentLength + bytes.length;
        byteBuf.writeBytes(bytes);
    }

    public void write(String str){
        write(str.getBytes(Charset.forName("UTF-8")));
    }

    public void println(String str){
        write((str + "\n").getBytes(Charset.forName("UTF-8")));
    }

    public void clear(){
        byteBuf.clear();
        contentLength = 0;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
