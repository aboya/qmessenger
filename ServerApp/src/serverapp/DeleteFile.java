/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.File;

/**
 *
 * @author Администратор
 */
public class DeleteFile extends Thread{
    File file;

    public DeleteFile(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        int attemt = 5;
        //force call to gc, if we don't call this file not deleted !
        System.gc();
        while(attemt > 0)
        {
            try {
                sleep(1000);
                if(file.delete()) break;
                attemt--;
            }catch(Exception ee){}
        }
    }


}
