/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package adagent;

import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
/**
 *
 * @author Kawai
 */
public class PageTypeTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String str = "";
        if(value != null)
        switch((Short)value) {
            case 1: str = "Top";    break;
            case 2: str = "Second"; break;
            case 3: str = "Middle"; break;
            case 4: str = "Last";   break;
            default: break;
        }
        setText(str);
        return this;
    }
}
