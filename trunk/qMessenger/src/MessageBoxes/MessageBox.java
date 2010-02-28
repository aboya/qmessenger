/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MessageBoxes;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Администратор
 */
public class MessageBox {
   public void ShowDialogBox(){
        JFrame frame = new JFrame("Show Message Dialog");
        JButton button = new JButton("Click Me");
       //button.addActionListener(new MyAction());
        frame.add(button);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
  }


}
