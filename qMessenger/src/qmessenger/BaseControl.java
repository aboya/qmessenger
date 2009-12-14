/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import org.eclipse.swt.graphics.Rectangle;



/**
 *
 * @author Администратор
 */
public class BaseControl {
    protected String ControlName = "";
    public void Resize(Rectangle rect) {
       
    }
    public String getControlName()
    {
        return ControlName;
    }

}
