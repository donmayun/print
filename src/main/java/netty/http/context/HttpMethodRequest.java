package netty.http.context;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpMethodRequest {
	private String url;
	private HttpPostRequestDecoder params;
	private HttpHeaders headers;
	private Map<String, List<String>> urlParams;
	private String userId;
	private String path;
	private Map<String, Object> session;
	private Map<String, FileUpload> fileUploadMap;
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUrl() {
		return url;
	}

	public HttpPostRequestDecoder getParams() {
		return params;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public String getPath() {
		return path;
	}


	public String getUrlOrigin() {
		String result = "";

		if (null == this.url) {
			return result;
		}

		return this.url;
	}

	public HttpMethodRequest(HttpRequest request, Map<String, Object> session) {
		this.url = request.getUri();
		QueryStringDecoder decoder = new QueryStringDecoder(url);
		urlParams = decoder.parameters();
		if(request.getMethod() != HttpMethod.GET) {
			this.params = new HttpPostRequestDecoder(request);
		}
		uploadFilesInit();
		path = decoder.path();
		this.headers = request.headers();
		this.session = session;
	}

	private void uploadFilesInit(){
		fileUploadMap = new HashMap<>();
		if(params != null) {
			List<InterfaceHttpData> bodyHttpData = params.getBodyHttpDatas();
			if (CollectionUtils.isNotEmpty(bodyHttpData)) {
				for (InterfaceHttpData data : bodyHttpData) {
					if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
						FileUpload fu = (FileUpload) data;
						fileUploadMap.put(data.getName(), fu);
					}
				}
			}
		}
	}

	public List<String> getParamters(String name){
		List<String> list = urlParams.get(name);
		if(list != null){
			return list;
		}
		if(params != null) {
			List<InterfaceHttpData> bodyHttpData = params.getBodyHttpDatas(name);
			if (CollectionUtils.isNotEmpty(bodyHttpData)) {
				List<String> datas = new ArrayList<>();
				for (InterfaceHttpData data : bodyHttpData) {
					if (data != null) {
						try {
							if(data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
								datas.add(((Attribute) data).getValue());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return datas;
			}
		}
		return new ArrayList<>(0);
	}

	public final String getParameter(String name) {
		List<String> list = getParamters(name);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getSession(){
		return session;
	}

	public Map<String, FileUpload> getFileUploadMap() {
		return fileUploadMap;
	}
}
