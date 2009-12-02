/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 *
 * @author Администратор
 */
public class ButtonSendMessage extends Controll {
    Button button;
    public ButtonSendMessage(Composite composite) {
        button = new Button(composite, SWT.PUSH);
       
	button.setText("Im a Push Button");
	button.pack();
        SelectionListener sListner = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                int a;
                a = 0;
                System.out.println("wow");
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                int a;
                a = 0;
            }

            
        };
        button.addSelectionListener(sListner);

    }
    public void Resize(Rectangle rect)
    {
         button.setLocation(200, 280);
    }
}
