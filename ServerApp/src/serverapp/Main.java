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
        new File(Global.saveFilePath).mkdirs();
        Log.Write("Receive Dir:" + Global.saveFilePath);
        ApplicationSettings.Initialize();
        ApplicationSettings.saveProperties();
        FacutlyTree tr = new FacutlyTree("Tree.xml");
        ListenIncomingConnections m =  new ListenIncomingConnections();
        m.Listen();
    }

}


