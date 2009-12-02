/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 *
 * @author Администратор
 */
public class UserControls {
    UserList userList;
    InputTextField inputTextField;
    OutputTextField outputTextField;
    ButtonSendMessage buttonSendMessage;

    Vector<Controll> controlsList;

    public UserControls(Composite composite, Display display) {
        controlsList = new Vector<Controll>();
        userList = new UserList(composite, display);
        controlsList.add(userList);
        inputTextField = new InputTextField(composite);
        controlsList.add(inputTextField);
        outputTextField = new OutputTextField(composite);
        controlsList.add(outputTextField);
        buttonSendMessage = new ButtonSendMessage(composite);
        controlsList.add(buttonSendMessage);
    }
    public void Resize(Rectangle rect) throws Exception
    {
        int i, n  = controlsList.size();
        for(i = 0; i < n; i++)
            controlsList.get(i).Resize(rect);
    }


}
