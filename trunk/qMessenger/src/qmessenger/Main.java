/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;
 
import RegistrationForm.RegistrationForm;
import clientapp.ApplicationSettings;
import clientapp.Global;
import clientapp.User;
import java.io.FileOutputStream;
import java.util.Properties;
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
        User u = new User();
        Global.user = u;
        u.Connect();
 
        //new ScreenView().run();
        /// new RegistrationForm().run();

  }
}

