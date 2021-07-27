package ar.com.signals.trading.util.support;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ColorInstruction {
	private int r;
	private int g;
	private int b;
	
	public ColorInstruction(int r, int g, int b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public XSSFColor createColor(XSSFWorkbook workbook) {
		return new XSSFColor( new byte[] {(byte) r, (byte) g, (byte) b}, workbook.getStylesSource().getIndexedColors());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColorInstruction other = (ColorInstruction) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}
}
