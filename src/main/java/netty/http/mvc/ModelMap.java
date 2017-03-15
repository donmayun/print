package netty.http.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Don on 2016/1/13.
 */
public class ModelMap {

    private Map<String, Object> map = new HashMap<>();

    public void put(String key, Object value){
        map.put(key, value);
    }

    public Map<String, Object> getMap(){
        return map;
    }

}
