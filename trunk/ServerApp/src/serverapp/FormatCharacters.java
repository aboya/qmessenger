/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

/**
 *
 * @author Администратор
 */
public class FormatCharacters {
        //Reserved size append to message - indicates size of message
        // 9999999999999999999
        public static final Integer PreffixSize = 18;
        public static final char marker ='&';
       // public static final int TextMessage = 1;
        public static final int BinaryData = 2;
        public static final int UserAutorization = 3;

        public static String RequestRegistration = "reqReg";
        public static  String TextMessege = "txtMessage";
        public static String Auth = "authUser";
        public static String RequestStructureTree = "reqStructTree";
        public static String getOfflineMessages = "getOffline";
        public static String getFiles = "getFiles";
        public static String getUserStructureTree = "getUsrStruct";
}
