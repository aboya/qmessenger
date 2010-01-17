/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;


import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStreamWriter;

/**
 *
 * @author Серёжа
 */  
public class Log {
    public static void WriteException(Exception e)
    {
        System.out.println("---------------------------------------");
        System.out.println(java.util.Calendar.getInstance().getTime());
        System.out.println(e.toString());
        System.out.println("Stack Trace:");
        StackTraceElement elems[] = e.getStackTrace();
        for(int i = 0; i < elems.length; i++)
            System.out.println(elems[i].toString());
        System.out.println("---------------------------------------");
    }
    public static void Write(String s)
    {
        
        System.out.println(">>>>>>---------------------------------");
        System.out.println(java.util.Calendar.getInstance().getTime());
        System.out.println(s);
        System.out.println("---------------------------------<<<<<<");
        
    }

}
