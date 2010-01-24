/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 *
 * @author Администратор
 */
public class Utils {

    public static long GetDate() {
        // DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
         Date date = new Date();
         //Calendar cal = Calendar.getInstance();
        // date = cal.getTime();
         return date.getTime();        
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
   // проверяет существует ли файл
   // если да тогда добавляет к имени файла(1) (2) ... и тд
    public static String GenerateName(String path) {
        File file = new File(path);
        if(!file.exists()) return path;
        String fname = file.getName();
        String end = "";
        int ipoint;
        path = path.substring(0, path.length() - fname.length());
        ipoint = fname.lastIndexOf('.');
        if(ipoint != -1)
        {
            end = fname.substring(ipoint, fname.length());
            fname = fname.substring(0, ipoint);
        }
        int n = fname.length(), Lind, Rind;
        Lind = fname.lastIndexOf("(");
        Rind = fname.lastIndexOf(")");
        if(Rind != n-1 || Rind == -1 || Lind == -1)
        {
            fname += "(1)";
        }else {
             boolean ok = true;
             int d = 0;
             try {
                d = Integer.parseInt(fname.substring(Lind + 1, Rind));
             }catch(NumberFormatException ee)
             {
                 ok = false;
             }
             if(!ok || d < 1) fname += "(1)";
             else {
                 d++;
                 fname = String.format("%s(%d)",fname.substring(0, Lind), d );
             }
          }
        fname += end;
        path += fname;
        return path;
    }
}
