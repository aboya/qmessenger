/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

/**
 *
 * @author Администратор
 */
import MainWindow.MessengerMainFrame;
import clientapp.Global;
import clientapp.Log;
import java.io.StringReader;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.RepaintManager;
import javax.swing.tree.DefaultMutableTreeNode;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdesktop.application.SingleFrameApplication;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
public class ScreenView extends SingleFrameApplication {

    UserControls userControls;
    Shell shell;
    Display display;
    MessengerMainFrame messengerMainFrame;
    public ScreenView() {
        /*
        display = Global.getDisplay();
        shell = new Shell(display, SWT.CLOSE | SWT.RESIZE);
        shell.setText("qMessenger");
        Listener resizeListner = new Listener() {
               public void handleEvent(Event e)  {
                   shell.setRedraw(false);
                   Rectangle rect = shell.getBounds();
                   rect.width = Math.max(rect.width, 500);
                   rect.height = Math.max(rect.height, 500);
                   shell.setBounds(rect);
                   userControls.Resize(rect);
                   shell.setRedraw(true);
                }
         };
        shell.addListener(SWT.Resize, resizeListner);
        shell.addListener(SWT.Move, resizeListner);
        userControls = new UserControls(shell, display);
        shell.setSize(500, 400);
        shell.open();
        shell.setActive();
         *
         */
    }
     @Override protected void startup() {
        show(messengerMainFrame = new MessengerMainFrame());
        //fillTree(Global.getUser().structTreeXml);
        messengerMainFrame.fillTree(Global.getUser().structTreeXml);
        // comments this line on http://weblogs.java.net/blog/2008/04/13/repaintmanagers-side-effect
        // for correct updating window
        RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
        RepaintManager.currentManager(null).setDoubleBufferingEnabled(true);
        try
        {
            Global.getUser().getOfflineMessages();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
     }

     public void run() 
     {
         /*
        try
        {
            Global.getUser().getOfflineMessages();

        }catch(Exception e)
        {
            Log.WriteException(e);
        }
         while (!shell.isDisposed()) {
           if (!display.readAndDispatch()) {
             display.sleep();
           }
         }
         display.dispose();
         Global.getUser().Disconnect();
          * 
          */
         
   }
   public void AddMessageToScreen(String message)
   {
      userControls.AddMessageToScreen(message);
   }
   public void Close()
   {

       messengerMainFrame.setVisible(false);
       

   }
   public void setStatusText(final String txt)
   {
       /*
       if(shell.isDisposed()) return;
       display.syncExec(
            new Runnable() {
                public void run(){
                    if(shell.isDisposed()) return;
                    userControls.SetStatusText(txt);
                }
            }
        );
        * 
        */

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
           DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("TNU");
           JTree facultyTree = messengerMainFrame.getFacultyTree();

           //TreeItem rootItem = new TreeItem(tree, SWT.NONE);
           //rootItem.setText(s);
          // treeIds.clear();
          // treeIds.put(s, Integer.parseInt(rootElement.getAttribute("id")));
           //treeNode1.setUserObject(Integer.parseInt(rootElement.getAttribute("id")));
           BuildTNUTree(treeNode1,(Node) rootElement.getChildNodes().item(1));
           facultyTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        }catch(Exception e)
        {
            Log.WriteException(e);
        }

    }
    private void BuildTNUTree(DefaultMutableTreeNode treeItem, Node element)
    {
        String s = element.getNodeName();
        if(s.equals("#text")) return;

        String id = element.getAttributes().getNamedItem("id").getNodeValue();
       // treeIds.put(s, Integer.valueOf(id));

        DefaultMutableTreeNode newItem = new DefaultMutableTreeNode(element.getNodeName());
        

        //newItem.setText(element.getNodeName());
        NodeList list = element.getChildNodes();
        for(int i = 0; i < list.getLength(); i++)
            BuildTNUTree(newItem, list.item(i));
        treeItem.add(newItem);
    }
    public JFrame getFrame()
    {
        return messengerMainFrame;
    }
}
