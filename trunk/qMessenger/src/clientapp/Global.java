/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;



/**
 *
 * @author Серёжа
 */
public class Global {
        public static int PACKET_SIZE = 4096;
        public static int IncomingPort = 4899;
        public static int ServerPort = 4898;
        public static int ServerFileUploadPort = 4900;
        public static int ServerFileDownloadPort = 4901;
        public static String ServAddr = "192.168.2.2";
        public static int ReconnectInterval = 10000;
        private static User user = null;
        public static String lastOpenPath = "C:\\2\\";
        public static String lastSavePath = "C:\\2\\";
        public static String defaultSavePath = "C:\\2\\";
        public static String codePage = "UTF-8";
        public static int DontDownloadThisFile = 23;
        public static int DownloadingOk = 12;

    /**
     * @return the user
     */
    public static User getUser() {
        return user;
    }


    /**
     * @param aUser the user to set
     */
    public static void setUser(User aUser) {
        user = aUser;
    }
}

