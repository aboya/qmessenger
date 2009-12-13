/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;
import clientapp.User;

/**
 *
 * @author Администратор
 */
public class Main {

    /**
     * @param args the command line arguments
     */

public static void main(String args[]) throws Exception {
        User u = new User();
        u.Connect();
        u.AuthenticateUser();
        //new ScreenView().run();
        // new RegistrationForm().run();
    

  }
}

