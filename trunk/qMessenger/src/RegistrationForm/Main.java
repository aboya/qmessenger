/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author Администратор
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // TODO code application logic here
       // User u = new User();
        //load Tree.xml into string for test
        String filePath = "Tree.xml";
        byte[] buffer = new byte[(int) new File(filePath).length()];
        FileInputStream f = new FileInputStream(filePath);
        f.read(buffer);
        new RegistrationForm(new String(buffer)).run();
    }
}
