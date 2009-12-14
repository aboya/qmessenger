/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

/**
 *
 * @author Администратор
 */
import java.util.LinkedList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;


public class RegistrationForm {
    RegistrationFormControls userControls;
    Shell shell;
    String xmlTree;
     public RegistrationForm(String xml) {
         this.xmlTree = xml;
     }
     public void run()
     {
        Display display = new Display();
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
        StructureTree structTree = (StructureTree)  userControls.getControlByName("StructureTree");
        structTree.fillTree(xmlTree);

        shell.open();
        while (!shell.isDisposed()) {
           if (!display.readAndDispatch()) {
             display.sleep();
           }
         }
         display.dispose();
         //display.addListener(SWT.CLOSE, resizeListner);

   }

}
