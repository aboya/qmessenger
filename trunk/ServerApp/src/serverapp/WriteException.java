/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

/**
 *
 * @author Серёжа
 */
public class WriteException {
    public static void Write(Exception e)
    {
        System.out.println("---------------------------------------");
        System.out.println(java.util.Calendar.getInstance().getTime());
        System.out.println(e.toString());
        System.out.println("Stack Trace:");
        StackTraceElement elems[] = e.getStackTrace();
        for(int i = 0; i < elems.length; i++)
            System.out.print(elems[i].toString());
        System.out.println("---------------------------------------");
    }

}
