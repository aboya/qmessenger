/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import java.io.*;

/**
 *
 * @author Серёжа
 */
public class Main {
    public static void main(String[] args) {
        FacutlyTree tr = new FacutlyTree("Tree.xml");
        ListenIncomingConnections m =  new ListenIncomingConnections();
      // new dbConnection();
         m.Listen();
    }

}


