/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

/**
 *
 * @author Администратор
 */
import UserGUIControls.uMessageBox;
import clientapp.FormatCharacters;
import clientapp.Global;
import clientapp.Log;
import javax.swing.JTree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


public class RegistrationForm extends SingleFrameApplication{
     RegistrationFormControls userControls;
     RegistrationFormFrame registrationFormFrame;
     public RegistrationForm() {
     }
    @Override
    protected void startup() {
       show(registrationFormFrame = new RegistrationFormFrame(this));
       JTree jt = registrationFormFrame.getTree();
       registrationFormFrame.fillTree(Global.getUser().structTreeXml);
    }
 

}

