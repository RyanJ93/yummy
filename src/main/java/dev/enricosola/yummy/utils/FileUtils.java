package dev.enricosola.yummy.utils;

public class FileUtils {
    public static String getExtensionByStringHandling(String filename){
        int lastIndexOf = filename.lastIndexOf(".");
        if ( lastIndexOf == -1 ){
            return "";
        }
        return filename.substring(lastIndexOf + 1);
    }
}
