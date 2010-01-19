/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

import clientapp.Log;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import qmessenger.BaseControl;
import org.w3c.dom.*;
import org.xml.sax.InputSource;


/**
 *
 * @author Администратор
 */
public class StructureTree extends BaseControl {
    GridData data;
    Tree tree;
    Composite shell;
    String xml;
    Map <String, Integer> treeIds;

    public StructureTree(Composite shell) {
        this.shell = shell;
        tree = new Tree(shell,  SWT.BORDER|SWT.CHECK);
	data = new GridData(GridData.FILL_BOTH);
	tree.setLayoutData(data);
        this.ControlName = "StructureTree";
        treeIds = new TreeMap<String, Integer>();
        SelectionAdapter sAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent se) {
                TreeItem item = (TreeItem) se.item;
                if(item.getItemCount() != 0) {
                    item.setChecked(false);
                    return ;
                }
                LinkedList<TreeItem> Q = new LinkedList<TreeItem>();
                TreeItem []items = tree.getItems();
                // uncheck all tree items
                // because i have small time to research i implement native way to uncheck item
                // this may be replaced by some function in jdk if its exists
                for(int i = 0; i < items.length; i++)
                    Q.add(items[i]);
                while(!Q.isEmpty())
                {
                    TreeItem it = Q.getFirst();
                    Q.removeFirst();
                    it.setChecked(false);
                    items = it.getItems();
                    for(int i = 0; i < items.length; i++)
                        Q.addLast(items[i]);
                }
                item.setChecked(true);
            }

        };


        tree.addSelectionListener(sAdapter);
    }
    public  void fillTree(String _xml)
    {

        try {
            this.xml = _xml;
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

    @Override
    public void Resize(Rectangle rect) {
        tree.setBounds(shell.getBounds().width-160, 10, 150, shell.getBounds().height-80);
    }
    public int findIdByName(String name)
    {
        return treeIds.get(name);
    }
    public int getSelectedId()
    {
        int i, n;
        TreeItem [] items = tree.getItems();
        LinkedList<TreeItem> Q = new LinkedList<TreeItem>();
        n = items.length;
        for(i = 0; i < n; i++) Q.addLast(items[i]);
        TreeItem treeItem = null;
        while(!Q.isEmpty())
        {
            treeItem = Q.getFirst();
            Q.removeFirst();
            if(treeItem.getChecked()) break;
            for(i = 0; i < treeItem.getItemCount(); i++)
                Q.addLast(treeItem.getItem(i));
        }
        if(treeItem == null) return 0;
        return this.findIdByName(treeItem.getText());
    }




}
