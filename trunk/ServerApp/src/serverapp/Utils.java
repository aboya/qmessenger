/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Администратор
 */
public class Utils {

    public static String GetDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        Date date = new Date();
        return dateFormat.format(date);
   }


}
