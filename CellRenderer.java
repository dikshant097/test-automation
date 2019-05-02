import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
 * color for cells
 * NA-Black
 * AB-BLUE
 * Below Threshold-Red
 * Percentage < 60-yellow
 * Percentage < 50-Red
 * */
public class CellRenderer extends DefaultTableCellRenderer {

	private ArrayList<CellLocation> na, ab, below50, below60, th;

	public CellRenderer(HashMap<String, ArrayList<CellLocation>> typeOfCell) {
		if (typeOfCell.containsKey("ab"))
			ab = typeOfCell.get("ab");

		if (typeOfCell.containsKey("na"))
			na = typeOfCell.get("na");

		if (typeOfCell.containsKey("th"))
			th = typeOfCell.get("th");

		if (typeOfCell.containsKey("50"))
			below50 = typeOfCell.get("50");

		if (typeOfCell.containsKey("60"))
			below60 = typeOfCell.get("60");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (row >= 0 && column == 0) {
			c.setFont(new Font("Arial Black", Font.BOLD, 15));
			c.setForeground(Color.BLACK);
		} else if (((String) value).equalsIgnoreCase("ab")) {
			c.setBackground(Color.BLACK);

		} /*
			 * else if (ab.contains(new CellLocation(row, column))) { //
			 * c.setBackground(Color.BLUE); c.setForeground(Color.WHITE); }
			 */
		return c;
	}

}
