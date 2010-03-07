/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MainWindow;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Администратор
 */
public class DisableEditingTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    public DisableEditingTableModel(Object [] columns, int rowCount)
    {
        super(columns, rowCount);
    }
    public  DisableEditingTableModel()
    {
        super();
    }
}
