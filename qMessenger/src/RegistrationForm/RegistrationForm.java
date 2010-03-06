/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

/**
 *
 * @author Администратор
 */

import clientapp.Global;
import javax.swing.JTree;
import org.jdesktop.application.SingleFrameApplication;


public class RegistrationForm extends SingleFrameApplication{
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

