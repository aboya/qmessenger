/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

/**
 *
 * @author Администратор
 */
public class TrayMenu extends BaseControl{
    private TrayItem trayItem;
    final private  Menu menu;
    final private  Tray tray;
    private MenuItem hideShellMenuItem;
    private MenuItem showShellMenuItem;
    final private Shell shell;

    public TrayMenu(Shell _shell, Display display) {
        this.shell = _shell;
        menu = new Menu(shell);
        tray = shell.getDisplay().getSystemTray();
        trayItem = new TrayItem(tray, SWT.NONE);
        trayItem.setToolTipText("SWT TrayItem");
        Image img = new Image(display, "TrayIcon.png");
        trayItem.setImage(img);
       

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
            @Override
            public void widgetSelected(SelectionEvent e) {
                showShell();
            }
        });

        hideShellMenuItem = new MenuItem(menu, SWT.NONE);
        hideShellMenuItem.setText("Hide Shell");
        hideShellMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                hideShell();
            }
        });
        MenuItem closeShellMenuItem = new MenuItem(menu, SWT.NONE);
        closeShellMenuItem.setText("quit");
        closeShellMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
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
    public TrayItem getTrayItem()
    {
        return trayItem;
    }
    public Tray getTray()
    {
        return tray;
    }

}
