/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import clientapp.Global;
import clientapp.Log;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 *
 * @author Администратор
 */
public class UserList extends  BaseControl {
    GridData data;
    Tree tree;
    Composite shell;
    Map <String, Integer> treeIds;

    public UserList(Composite composite, Display display) {
             this.shell = composite;
             treeIds = new TreeMap <String, Integer>();

             tree = new Tree(composite, SWT.CHECK | SWT.BORDER);
	     data = new GridData(GridData.FILL_BOTH);
	     tree.setLayoutData(data);
	     fillTree(Global.getUser().structTreeXml);
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
    @Override
    public  void Resize(Rectangle rect)
    {
        tree.setBounds(shell.getBounds().width-160, 10, 140, shell.getBounds().height-67);
    }
    public  void fillTree(String xml)
    {

        try {

           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder db = factory.newDocumentBuilder();
           InputSource inStream = new InputSource();
           inStream.setCharacterStream(new StringReader(xml));
           Document doc = db.parse(inStream);

           Element rootElement = doc.getDocumentElement();
           String s = rootElement.getNodeName();
           TreeItem rootItem = new TreeItem(tree, SWT.NONE);
           rootItem.setText(s);
           treeIds.clear();
           treeIds.put(s, Integer.parseInt(rootElement.getAttribute("id")));
           BuildTNUTree(rootItem,(Node) rootElement.getChildNodes().item(1));
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
	tree.setRedraw(true);
    }
    private void BuildTNUTree(TreeItem treeItem, Node element)
    {
        String s = element.getNodeName();
        if(s.equals("#text")) return;

        String id = element.getAttributes().getNamedItem("id").getNodeValue();
        treeIds.put(s, Integer.valueOf(id));

        TreeItem newItem = new TreeItem(treeItem, SWT.NONE);
        newItem.setText(element.getNodeName());
        NodeList list = element.getChildNodes();
        for(int i = 0; i < list.getLength(); i++)
            BuildTNUTree(newItem, list.item(i));
    }
    public int findIdByName(String name)
    {
        return treeIds.get(name);
    }
    public Set <Integer> getSelectedId()
    {
        int i, n;
        TreeItem [] items = tree.getItems();
        LinkedList<TreeItem> Q = new LinkedList<TreeItem>();
        Set <Integer> selectedIds = new TreeSet<Integer>();
        n = items.length;
        for(i = 0; i < n; i++) Q.addLast(items[i]);
        TreeItem treeItem = null;
        while(!Q.isEmpty())
        {
            treeItem = Q.getFirst();

            Q.removeFirst();
            if(treeItem.getChecked() && treeItem.getItemCount() == 0) // select only last node
                selectedIds.add(this.findIdByName(treeItem.getText()));
            
            for(i = 0; i < treeItem.getItemCount(); i++)
                Q.addLast(treeItem.getItem(i));
        }
        return selectedIds;
    }
}
