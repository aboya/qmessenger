/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

import clientapp.Log;
import java.util.Vector;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import qmessenger.BaseControl;
import qmessenger.InputTextField;

/**
 *
 * @author Администратор
 */
public class RegistrationFormControls extends BaseControl{

    Vector<BaseControl> controlsList;
    UserNameField userName;
    StructureTree structureTree;


    public RegistrationFormControls(Composite composite, Display display) {
        controlsList = new Vector<BaseControl>();
        userName = new UserNameField(composite);
        controlsList.add(userName);
        structureTree = new StructureTree(composite);
        controlsList.add(structureTree);
        
       //  userName.Resize(new Rectangle(0, 0, 10, 20));

    }
    @Override
    public void Resize(Rectangle rect)
    {
        
        int i, n  = controlsList.size();
        for(i = 0; i < n; i++)
        {
            try {
                controlsList.get(i).Resize(rect);
            }
            catch(Exception e) {
                Log.WriteException(e);

            }
        }
        
    }

}
