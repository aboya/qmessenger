/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 *
 * @author Администратор
 */
public class ApplicationSettings {
    private static String fileName = "AppSettings.properties";
    private static Properties vProp;
    public static void Initialize()
    {
        FileInputStream iFile = null;
        vProp = new java.util.Properties();
        try {
            iFile = new java.io.FileInputStream(fileName);
        }catch(Exception e)
        {
            saveProperties();
        }
        try {
            iFile = new java.io.FileInputStream(fileName);
            vProp.loadFromXML(iFile);
            String property;
            if( (property = vProp.getProperty("IncomingPort")) != null) Global.IncomingPort = Integer.parseInt(property) ;
            if( (property = vProp.getProperty("ServAddr")) != null && Utils.CheckIp(property))  Global.ServAddr = property;
            if( (property = vProp.getProperty("ServerPort")) != null) Global.ServerPort = Integer.parseInt(property);
            if( (property = vProp.getProperty("DefaultSavePath")) != null && Utils.CheckDirectoryExists(property)) Global.defaultSavePath = property;

            iFile.close();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public  static  void saveProperties()
    {
        FileOutputStream oFile = null;
        try {
            vProp.setProperty("IncomingPort",String.valueOf(Global.IncomingPort));
            vProp.setProperty("ServAddr", Global.ServAddr) ;
            vProp.setProperty("ServerPort", String.valueOf(Global.ServerPort));
            vProp.setProperty("DefaultSavePath", Global.defaultSavePath);
            vProp.storeToXML(oFile = new FileOutputStream(fileName), "qMessengerProperties");
            if(oFile != null) oFile.close();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public static void SetProperty(String key, String value)
    {
        vProp.setProperty(key, value);
        saveProperties();
    }


}
