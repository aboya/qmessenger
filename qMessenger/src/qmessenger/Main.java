/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;
 
import SendFileDialog.SendFileDialogView;
import clientapp.ApplicationSettings;
import clientapp.Global;
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
        //new SendFileDialogView("SEND").run();
       // new SendFileDialogView().launch(SendFileDialogView.class, null);
        
        ApplicationSettings.Initialize();
        ApplicationSettings.saveProperties();
        User u = new User();
        Global.setUser(u);
        u.Connect();
   

  }
}

