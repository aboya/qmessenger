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
import javax.swing.JButton;
import javax.swing.JFrame;

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
        while(true) {
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
        }
    }
    public static String getAdekvatSize(Long size)
    {
        String res;
        Long a = 1024L;
        if(size < a) res = size.toString() + " байт";
        else if(size < a * a) res = String.format("%.2f Килобайт",  size / 1024.0);
        else if(size < a * a * a) res = String.format("%.2f Мегабайт",  size / 1024.0 / 1024.0);
        else if(size < a * a * a * a) res = String.format("%.2f Гигабайт",  size / 1024.0 / 1024.0 / 1024.0);
        else res = String.format("%.2lf Терабайт",  size / 1024.0 / 1024.0 / 1024.0 / 1024.0);
        return res;
    }
    public static boolean CheckIp(String ip)
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
            parts = null;
        }catch(Exception e)
        {
            return false;
        }
        return true;
    }
    public static boolean CheckDirectoryExists(String path)
    {
        File f = new File(path);
        boolean res = f.isDirectory();
        f = null;
        return res;
    }


}
