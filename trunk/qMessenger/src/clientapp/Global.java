/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;



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
        public static String defaultSavePath = System.getProperty("user.dir") + File.separator + "ReceivedFiles" + File.separator;;
        public static String codePage = "UTF-8";
        public static int DontDownloadThisFile = 23;
        public static int DownloadingOk = 12;
        public static Image applicationIcon = new ImageIcon("TrayIcon.png").getImage();

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

