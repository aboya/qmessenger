/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;


/**
 *
 * @author Администратор
 */
public class Main {

    /**
     * @param args the command line arguments
     */

public static void main(String args[]) {
    new ScreenView().run();
    

  }
}

