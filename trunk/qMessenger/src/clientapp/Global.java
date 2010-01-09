/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import org.eclipse.swt.widgets.Display;

/**
 *
 * @author Серёжа
 */
public class Global {
        public static int PACKET_SIZE = 4096;
        public static int IncomingPort = 4899;
        public static int ServerPort = 4898;
        public static int ServerFileUploadPort = 4900;
        public static String ServAddr = "192.168.2.2";
        public static int ReconnectInterval = 10000;
        private static User user = null;
        private static Display display = new Display();
        public static String lastOpenPath = "C:\\";
        public static String lastSavePath = "C:\\";
        public static String defaultSavePath = "C:\\";
        public static String codePage = "UTF-8";

    /**
     * @return the user
     */
    public static User getUser() {
        return user;
    }
    public static Display getDisplay()
    {
        return display;
    }

    /**
     * @param aUser the user to set
     */
    public static void setUser(User aUser) {
        user = aUser;
    }
}

