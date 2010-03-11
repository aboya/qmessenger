/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RegistrationFormFrame.java
 *
 * Created on 04.02.2010, 18:56:31
 */

package RegistrationForm;


import clientapp.Global;
import clientapp.Log;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.StringReader;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
/**
 *
 * @author Администратор
 */
public class RegistrationFormFrame extends javax.swing.JFrame {
    RegistrationForm registrationForm;
    Map <String, Integer> treeIds = new TreeMap<String, Integer>();
    Vector < File > fileList = new Vector<File>();

    /** Creates new form RegistrationFormFrame */
    public RegistrationFormFrame(RegistrationForm _parent) {
        registrationForm = _parent;
        initComponents();
        this.setResizable(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tnuTree = new javax.swing.JTree();
        jScrollPane1 = new javax.swing.JScrollPane();
        JPanel jPanel1;
        jPanel1 = new javax.swing.JPanel();
        txtLastName = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtFirstName = new javax.swing.JTextField();
        lblFirstName = new javax.swing.JLabel();
        lblLastName = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblMobile = new javax.swing.JLabel();
        txtMobile = new javax.swing.JTextField();
        btnRegister = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMiddleName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtBuiding = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApartment = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Регистрация");
        setMinimumSize(new java.awt.Dimension(379, 409));

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(225, 382));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("ТНУ");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Факультеты");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("blue");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("violet");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("red");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("yellow");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Декататы");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("basketball");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("soccer");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("football");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("hockey");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Деканы");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("hot dogs");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("pizza");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ravioli");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("bananas");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        tnuTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tnuTree.setName("tnuTree"); // NOI18N
        tnuTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
        tnuTree.setCellRenderer(renderer);
        tnuTree.setCellEditor(new CheckBoxNodeEditor(tnuTree));
        tnuTree.setEditable(true);
        jScrollPane1.setViewportView(tnuTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jPanel1.setName("jPanel1"); // NOI18N

        txtLastName.setDocument(new JTextFieldLimit(15));
        txtLastName.setName("txtLastName"); // NOI18N

        txtPhone.setDocument(new JTextFieldLimit(15));
        txtPhone.setName("txtPhone"); // NOI18N

        txtFirstName.setDocument(new JTextFieldLimit(15));
        txtFirstName.setName("txtFirstName"); // NOI18N

        lblFirstName.setText("Имя:");
        lblFirstName.setName("lblFirstName"); // NOI18N

        lblLastName.setText("Фамилия:");
        lblLastName.setName("lblLastName"); // NOI18N

        lblPhone.setText("Рабочий телефон:");
        lblPhone.setName("lblPhone"); // NOI18N

        lblMobile.setText("Мобильный телефон:");
        lblMobile.setName("lblMobile"); // NOI18N

        txtMobile.setDocument(new JTextFieldLimit(15));
        txtMobile.setName("txtMobile"); // NOI18N

        btnRegister.setText("Зарегистрировать меня");
        btnRegister.setName("btnRegister"); // NOI18N
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                registrationButtonPress(evt);
            }
        });

        btnExit.setText("Закончить без регистрации");
        btnExit.setName("btnExit"); // NOI18N

        jLabel1.setText("Отчество:");
        jLabel1.setName("jLabel1"); // NOI18N

        txtMiddleName.setName("txtMiddleName"); // NOI18N

        jLabel2.setText("Корпус:");
        jLabel2.setName("jLabel2"); // NOI18N

        txtBuiding.setName("txtBuiding"); // NOI18N

        jLabel3.setText("Комната:");
        jLabel3.setName("jLabel3"); // NOI18N

        txtApartment.setName("txtApartment"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(176, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRegister)
                        .addGap(18, 18, 18)
                        .addComponent(btnExit)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(lblPhone)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(lblFirstName)
                            .addComponent(lblLastName)
                            .addComponent(lblMobile))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                        .addComponent(txtLastName)
                                        .addComponent(txtMiddleName))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtApartment, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtBuiding, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(7, 7, 7))
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLastName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMiddleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuiding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPhone))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMobile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(btnRegister))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public int getSelectedId()
    {
        int i;
        DefaultTreeModel model = (DefaultTreeModel)tnuTree.getModel();
        DefaultMutableTreeNode rootNode =(DefaultMutableTreeNode) model.getRoot();
        LinkedList<DefaultMutableTreeNode> Q = new LinkedList<DefaultMutableTreeNode>();
        Q.addLast(rootNode);
        DefaultMutableTreeNode treeItem = null;
        while(!Q.isEmpty())
        {
            treeItem = Q.getFirst();
            Q.removeFirst();
            if(treeItem.isLeaf()) // select only last node
            {
                Object o = treeItem.getUserObject();
                if(o instanceof CheckBoxNode)
                {
                     CheckBoxNode chk = (CheckBoxNode)o;
                     if(chk.selected) return findIdByName(chk.toString());
                }
            }
            for(i = 0; i < treeItem.getChildCount(); i++)
                Q.addLast((DefaultMutableTreeNode)treeItem.getChildAt(i));
        }
        return 0;
    }
    public int findIdByName(String name)
    {
        return treeIds.get(name);
    }
    private void registrationButtonPress(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrationButtonPress
        int id = this.getSelectedId();
        boolean isRegister;
        if(id == 0) {
            JOptionPane.showMessageDialog(this, "Выберите факультет");
            return;
        }else if(txtLastName.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Вы не ввели имя");
            return;
        }else if(txtFirstName.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Вы не ввели фамилию");
            return;
        }else if( txtPhone.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Вы не ввели телефон");
            return;
        }
        else if(txtMobile.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Вы не ввели мобильный телефон");
            return;
        }
        RegistrationData rData = new RegistrationData();
        rData.firstName = txtLastName.getText();
        rData.lastName = txtFirstName.getText();
        rData.mobile = txtMobile.getText();
        rData.phone = txtPhone.getText();
        rData.structureId = id;


        try {
           isRegister =  Global.getUser().RegisterUser(rData);
        }catch(Exception e)
        {
            isRegister = false;
            Log.WriteException(e);
        }
        if(isRegister == false)
        {
            JOptionPane.showMessageDialog(this, "Registarion failed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Регистрация успешна", "Информация", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
        Global.getUser().CreateMainWindow();


        //ReturnCode = 0;
        //display.close();
       
    }//GEN-LAST:event_registrationButtonPress
    public JTree getTree()
    {
        return tnuTree;
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
           JTree facultyTree = getTree();

           //TreeItem rootItem = new TreeItem(tree, SWT.NONE);
           //rootItem.setText(s);
           treeIds.clear();
           treeIds.put(s, Integer.parseInt(rootElement.getAttribute("id")));
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
        treeIds.put(s, Integer.valueOf(id));

        DefaultMutableTreeNode newItem = new DefaultMutableTreeNode(element.getNodeName());


        //newItem.setText(element.getNodeName());
        NodeList list = element.getChildNodes();
        for(int i = 0; i < list.getLength(); i++)
            BuildTNUTree(newItem, list.item(i));
        treeItem.add(newItem);
    }
    /**
    * @param args the command line arguments
    */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblMobile;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JTree tnuTree;
    private javax.swing.JTextField txtApartment;
    private javax.swing.JTextField txtBuiding;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMiddleName;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
public class CheckBoxNodeRenderer implements TreeCellRenderer {
  private JCheckBox leafRenderer = new JCheckBox();

  private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

  Color selectionBorderColor, selectionForeground, selectionBackground,
      textForeground, textBackground;
  protected JCheckBox getLeafRenderer() {
    return leafRenderer;
  }

  public CheckBoxNodeRenderer() {
    Font fontValue;
    fontValue = UIManager.getFont("Tree.font");
    if (fontValue != null) {
      leafRenderer.setFont(fontValue);
    }
    Boolean booleanValue = (Boolean) UIManager
        .get("Tree.drawsFocusBorderAroundIcon");
    leafRenderer.setFocusPainted((booleanValue != null)
        && (booleanValue.booleanValue()));

    selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
    selectionForeground = UIManager.getColor("Tree.selectionForeground");
    selectionBackground = UIManager.getColor("Tree.selectionBackground");
    textForeground = UIManager.getColor("Tree.textForeground");
    textBackground = UIManager.getColor("Tree.textBackground");
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {

    Component returnValue;
    if (leaf) {

      String stringValue = tree.convertValueToText(value, selected,
          expanded, leaf, row, false);
      leafRenderer.setText(stringValue);
      leafRenderer.setSelected(false);

      leafRenderer.setEnabled(tree.isEnabled());

      if (selected) {
        leafRenderer.setForeground(selectionForeground);
        leafRenderer.setBackground(selectionBackground);
      } else {
        leafRenderer.setForeground(textForeground);
        leafRenderer.setBackground(textBackground);
      }

      if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
        Object userObject = ((DefaultMutableTreeNode) value)
            .getUserObject();
        if (userObject instanceof CheckBoxNode) {
          CheckBoxNode node = (CheckBoxNode) userObject;
          leafRenderer.setText(node.getText());
          leafRenderer.setSelected(node.isSelected());
        }
      }
      returnValue = leafRenderer;
    } else {
      returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,
          value, selected, expanded, leaf, row, hasFocus);
    }
    return returnValue;
  }
}

public class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

  CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();

  ChangeEvent changeEvent = null;

  JTree tree;

  public CheckBoxNodeEditor(JTree tree) {
    this.tree = tree;
  }

  public Object getCellEditorValue() {

    JCheckBox checkbox = renderer.getLeafRenderer();
    CheckBoxNode checkBoxNode = new CheckBoxNode(checkbox.getText(),
        checkbox.isSelected());
    return checkBoxNode;
  }




  public boolean isCellEditable(EventObject event) {
    boolean returnValue = false;
    if (event instanceof MouseEvent) {
      MouseEvent mouseEvent = (MouseEvent) event;
      TreePath path = tree.getPathForLocation(mouseEvent.getX(),
          mouseEvent.getY());
      if (path != null) {
        Object node = path.getLastPathComponent();
        if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
          DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
          Object userObject = treeNode.getUserObject();
         // returnValue = ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
           returnValue = treeNode.isLeaf();

        }
      }
    }
    if(returnValue == true)
    {
        
    }
    return returnValue;
    //return returnValue;
  }

  public Component getTreeCellEditorComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row) {

    Component editor = renderer.getTreeCellRendererComponent(tree, value,
        true, expanded, leaf, row, true);

    // editor always selected / focused
    ItemListener itemListener = new ItemListener() {
      public void itemStateChanged(ItemEvent itemEvent) {
        if (stopCellEditing()) {
          fireEditingStopped();
        }
      }
    };
    if (editor instanceof JCheckBox) {
      ((JCheckBox) editor).addItemListener(itemListener);
    }

    return editor;
  }
    }

public class CheckBoxNode {
  String text;

  boolean selected;

  public CheckBoxNode(String text, boolean selected) {
    this.text = text;
    this.selected = selected;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean newValue) {
    selected = newValue;
  }

  public String getText() {
    return text;
  }

  public void setText(String newValue) {
    text = newValue;
  }

  public String toString() {
    return text;
  }
}

public class NamedVector extends Vector {
  String name;

  public NamedVector(String name) {
    this.name = name;
  }

  public NamedVector(String name, Object elements[]) {
    this.name = name;
    for (int i = 0, n = elements.length; i < n; i++) {
      add(elements[i]);
    }
  }

  public String toString() {
    return "[" + name + "]";
  }

}
public class JTextFieldLimit extends PlainDocument {
  private int limit;
  JTextFieldLimit(int limit) {
    super();
    this.limit = limit;
  }

  JTextFieldLimit(int limit, boolean upper) {
    super();
    this.limit = limit;
  }

  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
    if (str == null)
      return;

    if ((getLength() + str.length()) <= limit) {
      super.insertString(offset, str, attr);
    }
  }
}
}
