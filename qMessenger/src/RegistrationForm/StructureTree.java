/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

import clientapp.Log;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.swt.SWT;
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

    public StructureTree(Composite shell) {
        this.shell = shell;
        tree = new Tree(shell,  SWT.BORDER|SWT.CHECK);
	data = new GridData(GridData.FILL_BOTH);
	tree.setLayoutData(data);
        this.ControlName = "StructureTree";
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




}
