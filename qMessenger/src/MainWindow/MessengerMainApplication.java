/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MainWindow;

import clientapp.Global;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
/**
 *
 * @author Администратор
 * если не использовать SingleFrameApplication то контролы под виндовс - гнусные.
 */
public class MessengerMainApplication extends SingleFrameApplication {
     /*** At startup create and show the main frame of the application.
     */

    @Override protected void startup() {

    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MessengerMainApplication
     */
    public static MessengerMainApplication getApplication() {
        return Application.getInstance(MessengerMainApplication.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(MessengerMainApplication.class, args);

    }
}
