/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SendFileDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import qmessenger.BaseControl;

/**
 *
 * @author Администратор
 */
public class FileList extends BaseControl{
    final Table table ;
    public FileList(Shell _composite) {
         table = new Table(_composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
         TableColumn col1  = new TableColumn(table, SWT.LEFT);
         col1.setText("Files");
         TableColumn col2 = new TableColumn(table, SWT.LEFT);
         col2.setText("Progress");
         this.ControlName = "FileList";
         TableItem item1 = new TableItem(table, 0);
         item1.setText(new String[]{"a","b"});
         item1.setText(0, "asdasd");
         TableItem item2 = new TableItem(table, 1);
         item2.setText("asd");
         //table.setHeaderVisible(true);
         table.setLinesVisible(true);
    }

    @Override
    public void Resize(Rectangle rect) {
        table.setBounds(10, 10, rect.width - 100, rect.height - 50);
      //  table.getColumn(0).setWidth(rect.width - 50);
        table.getColumn(0).setWidth(rect.width - 164);
        table.getColumn(1).setWidth(59);
    }

    @Override
    public String getControlName() {
        return ControlName;
    }
    public Table getTable()
    {
        return table;
    }
    public void AddFileName(String fileName)
    {
        TableItem item = new TableItem(table, table.getItemCount());
        item.setText(fileName);
    }
}
