package ar.com.signals.trading.util.support;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FontInstruction {
	public enum ColorProperties {BOLD, BOLD_ITALIC, ITALIC}
	
	private ColorProperties color;
	private Double height;
	public FontInstruction(ColorProperties color, Double height) {
		super();
		this.color = color;
		this.height = height;
	}
	
	public Font createFont(XSSFWorkbook workbook) {
		XSSFFont font = workbook.createFont();
		if(color != null) {
			switch(color) {
            case BOLD: font.setBold(true); font.setBold(true); break;
            case BOLD_ITALIC: font.setBold(true); font.setBold(true); font.setItalic(true); break;
            case ITALIC: font.setItalic(true); break;
			}
        }
		if(height!= null) {
			font.setFontHeight(18);
		}
		return font;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
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
		FontInstruction other = (FontInstruction) obj;
		if (color != other.color)
			return false;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		return true;
	}
}
