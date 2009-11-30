/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

/**
 *
 * @author Серёжа
 */  
public class Log {
    public static void WriteException(Exception e)
    {
        System.out.println("Exception:>>>>>>-----------------------");
        System.out.println(java.util.Calendar.getInstance().getTime());
        System.out.println(e.toString());
        System.out.println("---------------------------------<<<<<<");
    }
    public static void Write(String s)
    {
        System.out.println(">>>>>>---------------------------------");
        System.out.println(java.util.Calendar.getInstance().getTime());
        System.out.println(s);
        System.out.println("---------------------------------<<<<<<");
    }

}
