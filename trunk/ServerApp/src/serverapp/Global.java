/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.File;

/**
 *
 * @author Серёжа
 */
public class Global {
        public static int PACKET_SIZE = 4096;
        public static int ClientPort = 4899;
        public static int IncomingPort = 4898;
        public static int IncomingFilePort = 4900;
        public static int OutcomingFilePort = 4901;
        public static int max_users = 10000;
        public static int MAXLEN = 15; // max len of metadata
        public static String dbUrl = "jdbc:mysql://127.0.0.1:3306/qMessenger";
        public static String codePage = "UTF8";
        public static String saveFilePath = System.getProperty("java.io.tmpdir") + "qServer" + File.separator;
        public static String DataBaseLogin = "root";
        public static String DataBasePassword = "admin";
        public static int DontUploadThisFile = 23;
        public static int UploadOk = 12;


        public static String XmlTree;
}
