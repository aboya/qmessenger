/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

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
            int value;
            if( (property = vProp.getProperty("PACKET_SIZE")) != null
                && checkPacketSize(value = Integer.parseInt(property))) Global.PACKET_SIZE = value;

            if( (property = vProp.getProperty("ClientPort") ) != null
                    && checkClientPort(value = Integer.parseInt(property))) Global.ClientPort = value;

            if( (property = vProp.getProperty("IncomingPort")) != null
                    && checkIncomingPort(value = Integer.parseInt(property)))Global.IncomingPort = value;

            if( (property = vProp.getProperty("max_users")) != null
                    && checkMaxUsers(value = Integer.parseInt(property))) Global.max_users = value;

            if( (property = vProp.getProperty("MAXLEN")) != null
                    && checkMAXLEN(value = Integer.parseInt(property)))  Global.MAXLEN = value;

            iFile.close();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    private static boolean checkPacketSize(int packet_size)
    {
        return packet_size > 0 && packet_size <= (1<<20);
    }
    private static boolean checkClientPort(int port)
    {
        return port > 1024 && port < 65536;
    }
    private static boolean checkIncomingPort(int port)
    {
        return checkClientPort(port);
    }
    private static boolean checkMaxUsers(int nusers)
    {
        return nusers > 0 && nusers < (1<<31);
    }
    private static boolean checkMAXLEN(int len)
    {
        return len >0 && len <= 18;
    }
    public  static  void saveProperties()
    {
        FileOutputStream oFile = null;
        try {
            vProp.setProperty("PACKET_SIZE",String.valueOf(Global.PACKET_SIZE));
            vProp.setProperty("ClientPort", String.valueOf(Global.ClientPort)) ;
            vProp.setProperty("IncomingPort", String.valueOf(Global.IncomingPort));
            vProp.setProperty("max_users", String.valueOf(Global.max_users));
            vProp.setProperty("MAXLEN", String.valueOf(Global.MAXLEN));
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
