package netty.http;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServerConfig {
	private static Properties props;

    public static void init(String filePath){
        InputStream is = getInputStream(filePath);
        props = new Properties();
        if(is == null){
            return;
        }
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static InputStream getInputStream(String filePath){
        if(filePath == null || filePath.trim().equals("")){
            return null;
        }
        if(filePath.startsWith("/")){
            filePath = filePath.substring(1);
        }
        int lastSplit = filePath.lastIndexOf("/");
        String dir = "";
        final String filename;
        if(lastSplit > 0) {
            dir = filePath.substring(0, lastSplit);
            filename = filePath.substring(lastSplit + 1);
        }else{
            filename = filePath;
        }
        URL path = ServerConfig.class.getResource("/" + dir);
        if(path == null){
            path = ServerConfig.class.getResource("" + dir);
        }
        File file = new File(path.getPath());
        if(!file.getAbsolutePath().contains(".jar!")) {
            File[] files = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String[] args = filename.split("\\*");
                    if(args.length == 2) {
                        return name.startsWith(args[0]) && name.endsWith(args[1]);
                    }else{
                        return name.equals(filename);
                    }
                }
            });
            if(files.length > 0) {
                try {
                    return new FileInputStream(files[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            try {
                String filePath1 = file.getPath().substring(5, file.getPath().indexOf("!"));
                JarFile jarFile = new JarFile(filePath1);
                Enumeration files = jarFile.entries();
                while (files.hasMoreElements()) {
                    JarEntry entry = (JarEntry) files.nextElement();
                    String[] args = filename.split("\\*");
                    if(args.length == 2) {
                        if(entry.getName().startsWith(dir + "/" + args[0]) && entry.getName().endsWith(args[1])){
                            InputStream is=ServerConfig.class.getResourceAsStream("/" + entry.getName());
                            return is;
                        }
                    }else{
                        if(entry.getName().equals(filename)){
                            InputStream is=ServerConfig.class.getResourceAsStream("/" + entry.getName());
                            return is;
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	public static String getProperty(String key){
        if(props == null){
            throw new IllegalAccessError("com.cgeel.com.Don.utils.Configuration 未初始化");
        }
		return props.getProperty(key);
	}

    public static String getProperty(String key, String defaultValue){
        String value = getProperty(key);
        if(value == null){
            return defaultValue;
        }
        return value;
    }

    public static int getPropertyInt(String key){
        String value = getProperty(key, "0");
        return Integer.parseInt(value);
    }

    public static boolean getPropertyBoolean(String key){
        String value = getProperty(key, "false");
        return Boolean.parseBoolean(value);
    }

    public static Properties getProps(){
        return props;
    }

    public static void main(String [] args){
        //init("/config/data_handle-*.properties");
    }

    private static String resourcePath;

    public static String getResourcePath(){
        if(resourcePath != null){
            return resourcePath;
        }
        String result = null;
        URL path = ServerConfig.class.getResource("/");
        if(path == null){
            path = ServerConfig.class.getResource("");
        }
        if(path.getPath().contains(".jar!")){
            result = new File("").getAbsolutePath();
            if(result.endsWith("bin")){
                result = result.substring(0, result.length()-4);
            }
        }else {
            result = path.getPath();
        }
        resourcePath = result;
        return resourcePath;
    }
	
}
