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
import java.util.TreeMap;
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
	     fillTree(Global.user.structTreeXml);
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
        tree.setBounds(shell.getBounds().width-160, 10, 140, shell.getBounds().height-60);
    }
    public  void fillTree(String xml)
    {

        try {
            
           //File fp = new File("Tree.Xml");
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder db = factory.newDocumentBuilder();
           InputSource inStream = new InputSource();
           inStream.setCharacterStream(new StringReader(xml));
           Document doc = db.parse(inStream);

           Element rootElement = doc.getDocumentElement();
           String s = rootElement.getNodeName();
           TreeItem rootItem = new TreeItem(tree, SWT.NONE);
           rootItem.setText(s);
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
}
