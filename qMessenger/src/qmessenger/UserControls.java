/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

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

    public UserControls(Composite composite, Display display) {
        userList = new UserList(composite, display);
        inputTextField = new InputTextField(composite);
        outputTextField = new OutputTextField(composite);
        buttonSendMessage = new ButtonSendMessage(composite);
    }
    public void Resize(Rectangle rect)
    {
        userList.Resize(rect);
        inputTextField.Resize(rect);
        outputTextField.Resize(rect);
        buttonSendMessage.Resize(rect);
    }


}
