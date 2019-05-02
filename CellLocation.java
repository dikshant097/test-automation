
public class CellLocation {

	private int row, col;

	public CellLocation(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean equals(Object o) {
		CellLocation c = (CellLocation) o;
		if (this.row == c.getRow() && this.col == c.getCol())
			return true;

		return false;
	}

}
