/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;
 
import clientapp.ApplicationSettings;
import clientapp.Global;
import clientapp.User;
import java.io.File;
/**
 *
 * @author Администратор
 */
public class Main {

    /**
     * @param args the command line arguments
     */

public static void main(String args[]) throws Exception {      
        ApplicationSettings.Initialize();
        ApplicationSettings.saveProperties();
        new File(Global.defaultSavePath).mkdirs();
        User u = new User();
        Global.setUser(u);
        u.Connect();
  }
}

