/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 *
 * @author Администратор
 */
public class Utils {

    public static String GetDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss.SSSS");
        Date date = new Date();
        return dateFormat.format(date);
   }
    public static String GetShortDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH.mm");
        Date date = new Date();
        return dateFormat.format(date);
    }
   public static long Checksum(String fileName) {
        CheckedInputStream cis = null;
        try {
            long fileSize = 0;
            cis = new CheckedInputStream( new FileInputStream(fileName), new CRC32());
            fileSize = new File(fileName).length();
            byte[] buf = new byte[4096];
            while(cis.read(buf) >= 0);
          }
          catch (Exception ee) {
              Log.WriteException(ee);
              return 0;
          }
        return cis.getChecksum().getValue();
    }
}
