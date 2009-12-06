/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import java.util.LinkedList;
import java.util.Queue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 *
 * @author Администратор
 */
public class UserList extends  BaseControl {
    GridData data;
    Tree tree;
    Composite shell;

    public UserList(Composite composite, Display display) {
             this.shell = composite;

             tree = new Tree(composite, SWT.CHECK | SWT.BORDER);
	     data = new GridData(GridData.FILL_BOTH);
	     tree.setLayoutData(data);
	     fillTree(tree);
             tree.setBounds(composite.getBounds().width - 120, 10, 100, composite.getBounds().height - 60);
             SelectionAdapter adapter = new SelectionAdapter() {

            @Override
                public void widgetSelected(SelectionEvent e) {
                    TreeItem ti = (TreeItem) e.item;
                    TreeItem [] items = ti.getItems();
                    TreeItem item;
                    boolean checked = ti.getChecked();
                    //go down
                    int i;
                    LinkedList<TreeItem> Q = new LinkedList<TreeItem>();
                    for(i = 0; i < items.length; i++)
                        Q.add(items[i]);
                    while(!Q.isEmpty())
                    {
                        item = Q.getFirst();
                        Q.removeFirst();
                        item.setChecked(checked);
                        for(i = 0; i < item.getItemCount(); i++)
                            Q.addLast(item.getItem(i));

                    }
                    // go up
                    item = ti;
                    Q.clear();
                    while(item.getParentItem() != null) {
                        Q.add(item);
                        item = item.getParentItem();
                    }
                    while(!Q.isEmpty())
                    {
                        item = Q.getFirst();
                        Q.removeFirst();
                        int cnt = 0, n = item.getParentItem().getItemCount();
                        for(i = 0; i < n; i++)
                            if(item.getParentItem().getItem(i).getChecked() == false) cnt++;

                        if(cnt == 0)
                            item.getParentItem().setChecked(true);
                        if(cnt > 0)
                            item.getParentItem().setChecked(false);

                    }
                }

             };
            tree.addSelectionListener(adapter);

    }

    public  void Resize(Rectangle rect)
    {
        tree.setBounds(shell.getBounds().width-160, 10, 140, shell.getBounds().height-60);
    }

    
     private void fillTree(Tree tree) {
	     // Turn off drawing to avoid flicker
	     tree.setRedraw(false);

	     // Create five root items
	     for (int i = 0; i < 1; i++) {
	       TreeItem item = new TreeItem(tree, SWT.NONE);
	       item.setText("TNU" + i);

	       // Create three children below the root
	       for (int j = 0; j < 3; j++) {
	         TreeItem child = new TreeItem(item, SWT.NONE);
	         child.setText("Child Item " + i + " - " + j);

	         // Create three grandchildren under the child
	         for (int k = 0; k < 3; k++) {
	           TreeItem grandChild = new TreeItem(child, SWT.NONE);
	           grandChild.setText("Grandchild Item " + i + " - " + j + " - " + k);

                   for(int n = 0; n < 3; n++) {
                        TreeItem ggrandChild = new TreeItem(grandChild, SWT.NONE);
                        ggrandChild.setText("ggrand child ");
                    }

	         }
	       }
	     }
	     // Turn drawing back on!
	     tree.setRedraw(true);
	   }
}
