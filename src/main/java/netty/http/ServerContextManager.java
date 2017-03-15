package netty.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Don on 2016/1/15.
 */
public class ServerContextManager {

    private static ServerContextManager serverContextManager = new ServerContextManager();

    private ServerContextManager(){}

    public static ServerContextManager getInstance(){
        return serverContextManager;
    }

    private List<ExceptionResolver> exceptionResolvers = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();

    public List<ExceptionResolver> getExceptionResolvers() {
        return exceptionResolvers;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
