import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
 * color for cells
 * NA-Black
 * AB-BLUE
 * Below Threshold-orange
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
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (row >= 0 && column == 0) {
			l.setFont(new Font("Arial Black", Font.BOLD, 14));
			l.setForeground(Color.BLACK);
			l.setBackground(Color.WHITE);
			setHorizontalAlignment(JLabel.LEFT);
		} else if (((String) value).equalsIgnoreCase("")) {
			l.setFont(new Font("SansSerif", Font.BOLD, 13));
			l.setBackground(Color.BLACK);
			l.setForeground(Color.BLACK);
			setHorizontalAlignment(JLabel.CENTER);
		}  else if (((String) value).equalsIgnoreCase("ab")) {
			l.setFont(new Font("SansSerif", Font.BOLD, 13));
			  l.setBackground(Color.BLUE);
			  l.setForeground(Color.WHITE);
			  setHorizontalAlignment(JLabel.CENTER);
		}
		else if( below50.contains(new CellLocation(row,column)))
		{
			l.setFont(new Font("SansSerif", Font.BOLD, 13));
			l.setBackground(Color.RED);
			l.setForeground(Color.BLACK);
			setHorizontalAlignment(JLabel.CENTER);
		}
		else if( below60.contains(new CellLocation(row,column)))
		{
			l.setFont(new Font("SansSerifk", Font.BOLD, 13));
			l.setBackground(Color.YELLOW);
			l.setForeground(Color.BLACK);
			setHorizontalAlignment(JLabel.CENTER);
		}
		else if( th.contains(new CellLocation(row,column)))
		{
			l.setFont(new Font("SansSerif", Font.BOLD, 13));
			l.setBackground(Color.ORANGE);
			l.setForeground(Color.BLACK);
			setHorizontalAlignment(JLabel.CENTER);
		}
		else {
			l.setFont(new Font("SansSerif", Font.BOLD, 13));
			l.setBackground(Color.GREEN);
			l.setForeground(Color.BLACK); 
			setHorizontalAlignment(JLabel.CENTER);
		}
		
		if(isSelected) {
			l.setFont(new Font("Arial Black", Font.BOLD, 16));
			l.setBackground(Color.CYAN);
			l.setForeground(Color.BLACK);
			
		}
			 
		return l;
	}

}
