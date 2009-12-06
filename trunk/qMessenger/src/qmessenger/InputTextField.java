/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;


/**
 *
 * @author Администратор
 */
public class InputTextField extends BaseControl {
    Text txt;
    public InputTextField(Composite composite) {
          txt = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
          txt.setDoubleClickEnabled(true);
          GridData data = new GridData(GridData.FILL_BOTH);
          txt.setLayoutData(data);
    }
    public void Resize(Rectangle rect)
    {
        txt.setBounds(10, 150, 300, 100);
    }
}
