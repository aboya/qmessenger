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
            if( (property = vProp.getProperty("ServAddr")) != null && checkIp(property))  Global.ServAddr = property;
            if( (property = vProp.getProperty("ServerPort")) != null) Global.ServerPort = Integer.parseInt(property);
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
            vProp.storeToXML(oFile = new FileOutputStream(fileName), "qMessengerProperties");
            if(oFile != null) oFile.close();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public static boolean checkIp(String ip)
    {
        try {
            String[] parts = ip.split( "\\." );
            for ( String s : parts )
            {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) )
                {
                    return false;
                }
            }
        }catch(Exception e)
        {
            return false;
        }
        return true;
    }
}
