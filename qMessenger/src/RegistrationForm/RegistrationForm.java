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
import clientapp.Global;
import clientapp.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


public class RegistrationForm {
     RegistrationFormControls userControls;
     Shell shell;
     String xmlTree;
     public static int ReturnCode = 0;
     public RegistrationForm(String xml) {
         this.xmlTree = xml;
     }
     public int run()
     {

        final Display display = new Display();
        shell = new Shell(display);
        shell.setText("Sign Up");
        Listener resizeListner = new Listener() {
               public void handleEvent(Event e)  {
                   Rectangle rect = shell.getBounds();
                   rect.width = Math.max(rect.width, 300);
                   rect.height = Math.max(rect.height, 500);
                   shell.setBounds(rect);
                   System.out.println(rect.toString());
                   userControls.Resize(rect);
                }
         };
        shell.addListener(SWT.Resize, resizeListner);
        shell.addListener(SWT.Move, resizeListner);
        userControls = new RegistrationFormControls(shell, display);
        shell.setSize(500, 400);
        final StructureTree structTree = (StructureTree)  userControls.getControlByName("StructureTree");
        structTree.fillTree(xmlTree);

        Listener pushRegButton = new Listener() {

            public void handleEvent(Event arg0)  {

                int id = structTree.getSelectedId();
                boolean isRegister;
                if(id == 0) {
                    uMessageBox msg = new uMessageBox("Select your name", SWT.ERROR);
                    msg.open();
                    return;
                }
                try {
                   isRegister =  Global.getUser().RegisterUser(id);
                }catch(Exception e)
                {
                    uMessageBox msg = new uMessageBox("Registration failed", SWT.ERROR);
                    msg.open();
                    Log.WriteException(e);
                    ReturnCode = SWT.CLOSE;
                    return;
                }
                if(isRegister == false)
                {
                    uMessageBox msg = new uMessageBox("Registration failed", SWT.ERROR);
                    msg.open();
                    ReturnCode = SWT.CLOSE;
                    return;
                }
                uMessageBox msg = new uMessageBox("Registration Sucessfull", SWT.OK);
                msg.open();

                ReturnCode = 0;
                display.close();
            }
        };
        RegButton regButton = (RegButton) userControls.getControlByName("RegButton");
        regButton.addListner(pushRegButton, SWT.Selection);

        shell.open();
        //shell.setActive();
        Listener closeListner = new Listener() {

            public void handleEvent(Event arg0) {
                ReturnCode = SWT.CLOSE;

            }
        };

         
        shell.addListener(SWT.Close, closeListner);
        while (!shell.isDisposed()) {
           if (!display.readAndDispatch()) {
             display.sleep();
           }
         }
         display.dispose();
         return ReturnCode;
         

   }

}
