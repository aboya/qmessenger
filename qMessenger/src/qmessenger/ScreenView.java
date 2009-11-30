/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

/**
 *
 * @author Администратор
 */
import java.util.LinkedList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;


public class ScreenView {
    UserControls userControls;
    Shell shell;
 private TrayItem trayItem;
     private MenuItem hideShellMenuItem;
    private MenuItem showShellMenuItem;



    

    public ScreenView() {
    }

    LinkedList<Object> viewObjects = new LinkedList<Object>();
     public void run()
     {
         Display display = new Display();
         shell = new Shell(display);
         shell.setText("qMessenger");
         //createContents(shell);
        Listener resizeListner = new Listener() {
               public void handleEvent(Event e) {
                   Rectangle rect = shell.getBounds();
                   rect.width = Math.max(rect.width, 300);
                   rect.height = Math.max(rect.height, 500);
                   shell.setBounds(rect);
                   System.out.println(rect.toString());
                   userControls.Resize(rect);
                }
         };
        shell.addListener(SWT.Resize, resizeListner);
        shell.addListener(SWT.Move, resizeListner);
        userControls = new UserControls(shell, display);
        shell.setSize(500, 400);
        final Menu menu = new Menu(shell);

         shell.open();
        final Tray tray = shell.getDisplay().getSystemTray();
        trayItem = new TrayItem(tray, SWT.NONE);
        trayItem.setToolTipText("SWT TrayItem");
        //Image image1 = new Image(display, "");
       // trayItem.setImage(image1);


        trayItem.addListener(SWT.Show, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("show menu");
            }
        });
        trayItem.addListener(SWT.Hide, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("hide menu");
            }
        });
        trayItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("selection menu (left button clicked)");
            }
        });
        trayItem.addListener(SWT.DefaultSelection, new Listener() {
            public void handleEvent(Event event) {
                System.out.println("default selection menu (left button double-clicked)");
                showShell();
            }
        });
        trayItem.addListener(SWT.MenuDetect, new Listener() {
            public void handleEvent(Event event) {
                menu.setVisible(true);
            }
        });
        
        shell.setMenu(menu);

        showShellMenuItem = new MenuItem(menu, SWT.NONE);
        showShellMenuItem.setText("Show Shell");
        showShellMenuItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                showShell();
            }
        });

        hideShellMenuItem = new MenuItem(menu, SWT.NONE);
        hideShellMenuItem.setText("Hide Shell");
        hideShellMenuItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                hideShell();
            }
        });
        MenuItem closeShellMenuItem = new MenuItem(menu, SWT.NONE);
        closeShellMenuItem.setText("quit");
        closeShellMenuItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                shell.dispose(); // strange - it may be hard tested !
            }
        });
        shell.addListener(SWT.Close, new Listener() {

            public void handleEvent(Event e) {
                e.doit = false;
                hideShell();

            }
        });




        showShellMenuItem.setEnabled(false);
        hideShellMenuItem.setEnabled(true);


         while (!shell.isDisposed()) {
           if (!display.readAndDispatch()) {
             display.sleep();
           }
         }
         display.dispose();
         //display.addListener(SWT.CLOSE, resizeListner);
         
   }
    private void showShell() {
        shell.setVisible(true);
        shell.setActive();
        showShellMenuItem.setEnabled(false);
        hideShellMenuItem.setEnabled(true);

    }

    private void hideShell() {
        shell.setVisible(false);
        showShellMenuItem.setEnabled(true);
        hideShellMenuItem.setEnabled(false);
    }



   private void createContents(Composite composite) {


     
     
  
   }
}
