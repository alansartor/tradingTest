package ar.com.signals.trading.util.support;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Solo para XSSF y se debe usar con el custom MyCellUtil
 * Usar colores XSSFColor, NO usar IndexedColors!
 * @author Pepe
 *
 */
public enum MyStyle {
	//properties.put(CellUtil.BORDER_TOP, BorderStyle.MEDIUM);
	//properties.put(CellUtil.BORDER_BOTTOM, BorderStyle.MEDIUM);
	//properties.put(CellUtil.BORDER_LEFT, BorderStyle.MEDIUM);
	//properties.put(CellUtil.BORDER_RIGHT, BorderStyle.MEDIUM);
	DECIMAL(new HashMap<String, Object>() {{
		put(CellUtil.DATA_FORMAT, "0.00");}}),
	PORCENTAJE(new HashMap<String, Object>() {{
		put(CellUtil.DATA_FORMAT, "0.00%");}}),
	BOLD(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null));}}),
	BOLD_RIGHT(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.ALIGNMENT, HorizontalAlignment.RIGHT);}}),
	BOLD_GRAY(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(192, 192, 192));//GREY_25_PERCENT
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_GRAY_CENTER(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null));
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(192, 192, 192));//GREY_25_PERCENT
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.ALIGNMENT, HorizontalAlignment.CENTER);}}),
	BOLD_LIGTH_GRAY(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(224, 224, 224));//GREY_12_PERCENT
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	GREEN(new HashMap<String, Object>() {{ 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(210, 255, 210));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	RED(new HashMap<String, Object>() {{
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 210, 210));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_GREEN(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(210, 255, 210));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_RED(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 210, 210));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	LIGTH_GREEN(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(240, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_LIGTH_GREEN(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(240, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_CENTER_LIGTH_GREEN(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(240, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.ALIGNMENT, HorizontalAlignment.CENTER);}}),
	LIGTH_RED(new HashMap<String, Object>() {{
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 240, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_LIGTH_RED(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 240, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_CENTER_LIGTH_RED(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 240, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.ALIGNMENT, HorizontalAlignment.CENTER);}}),
	LIGTH_YELLOW(new HashMap<String, Object>() {{
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_LIGTH_YELLOW(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	BOLD_CENTER_LIGTH_YELLOW(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, null)); 
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.ALIGNMENT, HorizontalAlignment.CENTER);}}),
	LIGTH_DECIMAL_RED(new HashMap<String, Object>() {{
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 240, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.DATA_FORMAT, "0.00");}}),
	LIGTH_DECIMAL_GREEN(new HashMap<String, Object>() {{
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(240, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.DATA_FORMAT, "0.00");}}),
	LIGTH_DECIMAL_YELLOW(new HashMap<String, Object>() {{
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(255, 255, 240));
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.DATA_FORMAT, "0.00");}}),
	TITLE(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, 18d));
		put(CellUtil.ALIGNMENT, HorizontalAlignment.CENTER);}}),
	TITLE_2(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, 16d));
		put(CellUtil.ALIGNMENT, HorizontalAlignment.LEFT);}}),
	ETAPA(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, 16D));
		put(CellUtil.ALIGNMENT, HorizontalAlignment.LEFT);
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(139, 176, 112));//verdoso
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	ETAPA_GRAY(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, 16D));
		put(CellUtil.ALIGNMENT, HorizontalAlignment.LEFT);
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(112, 112, 112));//gris
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);}}),
	ETAPA_PORCENTAJE(new HashMap<String, Object>() {{
		put(CellUtil.FONT, new FontInstruction(FontInstruction.ColorProperties.BOLD, 16D));
		put(CellUtil.ALIGNMENT, HorizontalAlignment.LEFT);
		put(CellUtil.FILL_FOREGROUND_COLOR, new ColorInstruction(139, 176, 112));//verdoso
		put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		put(CellUtil.DATA_FORMAT, "0.00%");}});
	
	private final Map<String, Object> properties;

	private MyStyle(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getProperties(XSSFWorkbook workbook, MyWorkbookProperties workbookProperties) {
		Map<String, Object> propertiesValues = workbookProperties.getMapProperties().get(this);
		if(propertiesValues != null) {
			return propertiesValues;
		}
		//clono las propiedades y le asigno index a las FontInstruction
		propertiesValues = new HashMap<String, Object>(properties);
		for (Entry<String, Object> property : propertiesValues.entrySet()) {
			if(property.getValue() instanceof FontInstruction) {
				Short fontIndex = workbookProperties.getFontInstructionIndexMap().get(property.getValue());
				if(fontIndex == null) {
					Font font = ((FontInstruction) property.getValue()).createFont(workbook);
					fontIndex = font.getIndex();
					workbookProperties.getFontInstructionIndexMap().put((FontInstruction) property.getValue(), fontIndex);
				}
				property.setValue(fontIndex);
			}else if(property.getValue() instanceof ColorInstruction) {
				XSSFColor color = workbookProperties.getColorInstructionIndexMap().get(property.getValue());
				if(color == null) {
					color = ((ColorInstruction) property.getValue()).createColor(workbook);
					workbookProperties.getColorInstructionIndexMap().put((ColorInstruction) property.getValue(), color);
				}
				property.setValue(color);
			}else if(CellUtil.DATA_FORMAT.equals(property.getKey())) {
				Short dataFormatIndex = workbookProperties.getDataFormatIndexMap().get(property.getValue());
				if(dataFormatIndex == null) {
					dataFormatIndex = workbook.createDataFormat().getFormat((String) property.getValue());
					workbookProperties.getDataFormatIndexMap().put((String) property.getValue(), dataFormatIndex);
				}
				property.setValue(dataFormatIndex);
			}
		}
		workbookProperties.getMapProperties().put(this, propertiesValues);
		return propertiesValues;
	}
}
