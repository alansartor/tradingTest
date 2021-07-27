package ar.com.signals.trading.util.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFColor;

public class MyWorkbookProperties {
	private final Map<FontInstruction, Short> fontInstructionIndexMap = new HashMap<>();
	private final Map<ColorInstruction, XSSFColor> colorInstructionIndexMap = new HashMap<>();
	private final Map<String, Short> dataFormatIndexMap = new HashMap<>();	
	private final Map<MyStyle, Map<String, Object>> mapProperties = new HashMap<>();
	
	
	public Map<FontInstruction, Short> getFontInstructionIndexMap() {
		return fontInstructionIndexMap;
	}
	public Map<MyStyle, Map<String, Object>> getMapProperties() {
		return mapProperties;
	}
	public Map<ColorInstruction, XSSFColor> getColorInstructionIndexMap() {
		return colorInstructionIndexMap;
	}
	public Map<String, Short> getDataFormatIndexMap() {
		return dataFormatIndexMap;
	}	
}
