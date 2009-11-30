/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

/**
 *
 * @author Администратор
 */
public class Utils {
    public static String GetIpFromMessage(String message)
    {
        // assume message in form xxx.xxx.xxx.xxx: message
        int i, n = message.length();
        for(i = 0; i < n; i++)
        {
            if(message.charAt(i) == ':') break;
        }
        String ip = message.substring(0, i);
        return ip;
    }

}
