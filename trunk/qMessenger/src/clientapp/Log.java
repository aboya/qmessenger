/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

/**
 *
 * @author Серёжа
 */
public class Log {
    public static void WriteException(Exception e)
    {
        System.out.println("---------------------------------------");
        System.out.println(java.util.Calendar.getInstance().getTime());
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
        System.out.println("---------------------------------------");
    }

}

