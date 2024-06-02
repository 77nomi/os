package detailPage;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class CustomRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 设置行颜色交替
        if (row % 2 == 0) {
            setBackground(Color.WHITE);
        } else {
            setBackground(new Color(245, 245, 245));
        }

        if (column == 1) {
            if ("F".equals(value)) {
                cellComponent.setForeground(Color.RED);
                cellComponent.setFont(cellComponent.getFont().deriveFont(Font.BOLD));
            } else {
                // Reset font color and style
                cellComponent.setForeground(table.getForeground());
                cellComponent.setFont(table.getFont());
            }
        } else {
            if(row==0 && column == 2){
                cellComponent.setForeground(Color.RED);
                cellComponent.setFont(cellComponent.getFont().deriveFont(Font.BOLD));
            }
            else if (row > 0 && !value.equals(table.getValueAt(row - 1, column))) {
                cellComponent.setForeground(Color.RED);
                cellComponent.setFont(cellComponent.getFont().deriveFont(Font.BOLD));
            } else {
                cellComponent.setForeground(table.getForeground());
                cellComponent.setFont(table.getFont());
            }
        }

        return cellComponent;
    }
}