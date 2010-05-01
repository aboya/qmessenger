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
        System.out.println("Stack Trace:");
        StackTraceElement [] elems = e.getStackTrace();
        for(int i = 0; i < elems.length; i++)
            System.out.println(elems[i]);
        System.out.println("---------------------------------------");
    }
    public static void WriteException(Exception e, String info)
    {
        System.out.println(info);
        Log.WriteException(e);
    }

}

