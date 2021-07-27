package ar.com.signals.trading.util.support;

import java.util.Arrays;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.signals.trading.util.service.ExcelSrv;

import org.apache.poi.ss.util.CellUtil;

public class MyCellUtil {
    
	private static final POILogger log = POILogFactory.getLogger(MyCellUtil.class);
	
    private static final Set<String> shortValues = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(
            		CellUtil.INDENTION,
            		CellUtil.DATA_FORMAT,
            		CellUtil.FONT,
            		CellUtil.ROTATION
    )));
    private static final Set<String> colorValues = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(
            		CellUtil.BOTTOM_BORDER_COLOR,
            		CellUtil.LEFT_BORDER_COLOR,
            		CellUtil.RIGHT_BORDER_COLOR,
            		CellUtil.TOP_BORDER_COLOR,
            		CellUtil.FILL_FOREGROUND_COLOR,
            		CellUtil.FILL_BACKGROUND_COLOR
    )));
    private static final Set<String> booleanValues = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(
            		CellUtil.LOCKED,
            		CellUtil.HIDDEN,
            		CellUtil.WRAP_TEXT
    )));
    private static final Set<String> borderTypeValues = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(
            		CellUtil.BORDER_BOTTOM,
            		CellUtil.BORDER_LEFT,
            		CellUtil.BORDER_RIGHT,
            		CellUtil.BORDER_TOP
    )));
    


    private static UnicodeMapping unicodeMappings[];

    private static final class UnicodeMapping {

        public final String entityName;
        public final String resolvedValue;

        public UnicodeMapping(String pEntityName, String pResolvedValue) {
            entityName = "&" + pEntityName + ";";
            resolvedValue = pResolvedValue;
        }
    }

    private MyCellUtil() {
        // no instances of this class
    }

    /**
     * Get a row from the spreadsheet, and create it if it doesn't exist.
     *
     * @param rowIndex The 0 based row number
     * @param sheet The sheet that the row is part of.
     * @return The row indicated by the rowCounter
     */
    public static Row getRow(int rowIndex, Sheet sheet) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }


    /**
     * Get a specific cell from a row. If the cell doesn't exist, then create it.
     *
     * @param row The row that the cell is part of
     * @param columnIndex The column index that the cell is in.
     * @return The cell indicated by the column.
     */
    public static Cell getCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);

        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        return cell;
    }


    /**
     * Creates a cell, gives it a value, and applies a style if provided
     *
     * @param  row     the row to create the cell in
     * @param  column  the column index to create the cell in
     * @param  value   The value of the cell
     * @param  style   If the style is not null, then set
     * @return         A new Cell
     */
    public static Cell createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = getCell(row, column);

        cell.setCellValue(cell.getRow().getSheet().getWorkbook().getCreationHelper()
                .createRichTextString(value));
        if (style != null) {
            cell.setCellStyle(style);
        }
        return cell;
    }


    /**
     * Create a cell, and give it a value.
     *
     *@param  row     the row to create the cell in
     *@param  column  the column index to create the cell in
     *@param  value   The value of the cell
     *@return         A new Cell.
     */
    public static Cell createCell(Row row, int column, String value) {
        return createCell(row, column, value, null);
    }

    /**
     * Take a cell, and align it.
     * 
     * This is superior to cell.getCellStyle().setAlignment(align) because
     * this method will not modify the CellStyle object that may be referenced
     * by multiple cells. Instead, this method will search for existing CellStyles
     * that match the desired CellStyle, creating a new CellStyle with the desired
     * style if no match exists.
     *
     * @param cell the cell to set the alignment for
     * @param align the horizontal alignment to use.
     *
     * @see HorizontalAlignment for alignment options
     * @since POI 3.15 beta 3
     */
    public static void setAlignment(XSSFCell cell, HorizontalAlignment align) {
        setCellStyleProperty(cell, CellUtil.ALIGNMENT, align);
    }
    
    /**
     * Take a cell, and vertically align it.
     * 
     * This is superior to cell.getCellStyle().setVerticalAlignment(align) because
     * this method will not modify the CellStyle object that may be referenced
     * by multiple cells. Instead, this method will search for existing CellStyles
     * that match the desired CellStyle, creating a new CellStyle with the desired
     * style if no match exists.
     *
     * @param cell the cell to set the alignment for
     * @param align the vertical alignment to use.
     *
     * @see VerticalAlignment for alignment options
     * @since POI 3.15 beta 3
     */
    public static void setVerticalAlignment(XSSFCell cell, VerticalAlignment align) {
        setCellStyleProperty(cell, CellUtil.VERTICAL_ALIGNMENT, align);
    }
    
    /**
     * Take a cell, and apply a font to it
     *
     * @param cell the cell to set the alignment for
     * @param font The Font that you want to set.
     * @throws IllegalArgumentException if <tt>font</tt> and <tt>cell</tt> do not belong to the same workbook
     */
    public static void setFont(XSSFCell cell, Font font) {
        // Check if font belongs to workbook
        Workbook wb = cell.getSheet().getWorkbook();
        final short fontIndex = font.getIndex();
        if (!wb.getFontAt(fontIndex).equals(font)) {
            throw new IllegalArgumentException("Font does not belong to this workbook");
        }

        // Check if cell belongs to workbook
        // (checked in setCellStyleProperty)

        setCellStyleProperty(cell, CellUtil.FONT, fontIndex);
    }

    /**
     * <p>This method attempts to find an existing CellStyle that matches the <code>cell</code>'s 
     * current style plus styles properties in <code>properties</code>. A new style is created if the
     * workbook does not contain a matching style.</p>
     * 
     * <p>Modifies the cell style of <code>cell</code> without affecting other cells that use the
     * same style.</p>
     * 
     * <p>This is necessary because Excel has an upper limit on the number of styles that it supports.</p>
     * 
     * <p>This function is more efficient than multiple calls to
     * {@link #setCellStyleProperty(org.apache.poi.ss.usermodel.Cell, String, Object)}
     * if adding multiple cell styles.</p>
     * 
     * <p>For performance reasons, if this is the only cell in a workbook that uses a cell style,
     * this method does NOT remove the old style from the workbook.
     * <!-- NOT IMPLEMENTED: Unused styles should be
     * pruned from the workbook with [@link #removeUnusedCellStyles(Workbook)] or
     * [@link #removeStyleFromWorkbookIfUnused(CellStyle, Workbook)]. -->
     * </p>
     *
     * @param cell The cell to change the style of
     * @param properties The properties to be added to a cell style, as {propertyName: propertyValue}.
     * @since POI 3.14 beta 2
     */
    public static XSSFCellStyle setCellStyleProperties(XSSFCell cell, Map<String, Object> properties) {
    	XSSFWorkbook workbook = cell.getSheet().getWorkbook();
        XSSFCellStyle originalStyle = cell.getCellStyle();
        XSSFCellStyle newStyle = null;
        Map<String, Object> values = getFormatProperties(originalStyle);
        putAll(properties, values);

        // index seems like what index the cellstyle is in the list of styles for a workbook.
        // not good to compare on!
        int numberCellStyles = workbook.getNumCellStyles();

        for (int i = 0; i < numberCellStyles; i++) {
        	XSSFCellStyle wbStyle = workbook.getCellStyleAt(i);
            Map<String, Object> wbStyleMap = getFormatProperties(wbStyle);

            // the desired style already exists in the workbook. Use the existing style.
            if (wbStyleMap.equals(values)) {
                newStyle = wbStyle;
                break;
            }
        }

        // the desired style does not exist in the workbook. Create a new style with desired properties.
        if (newStyle == null) {
            newStyle = workbook.createCellStyle();
            setFormatProperties(newStyle, workbook, values);
        }

        cell.setCellStyle(newStyle);
        return newStyle;
    }

    /**
     * <p>This method attempts to find an existing CellStyle that matches the <code>cell</code>'s
     * current style plus a single style property <code>propertyName</code> with value
     * <code>propertyValue</code>.
     * A new style is created if the workbook does not contain a matching style.</p>
     * 
     * <p>Modifies the cell style of <code>cell</code> without affecting other cells that use the
     * same style.</p>
     * 
     * <p>If setting more than one cell style property on a cell, use
     * {@link #setCellStyleProperties(org.apache.poi.ss.usermodel.Cell, Map)},
     * which is faster and does not add unnecessary intermediate CellStyles to the workbook.</p>
     * 
     * @param cell The cell that is to be changed.
     * @param propertyName The name of the property that is to be changed.
     * @param propertyValue The value of the property that is to be changed.
     */
    public static void setCellStyleProperty(XSSFCell cell, String propertyName, Object propertyValue) {
        Map<String, Object> property = Collections.singletonMap(propertyName, propertyValue);
        setCellStyleProperties(cell, property);
    }

    /**
     * Returns a map containing the format properties of the given cell style.
     * The returned map is not tied to <code>style</code>, so subsequent changes
     * to <code>style</code> will not modify the map, and changes to the returned
     * map will not modify the cell style. The returned map is mutable.
     *
     * @param style cell style
     * @return map of format properties (String -> Object)
     * @see #setFormatProperties(org.apache.poi.ss.usermodel.CellStyle, org.apache.poi.ss.usermodel.Workbook, java.util.Map)
     */
    private static Map<String, Object> getFormatProperties(XSSFCellStyle style) {
        Map<String, Object> properties = new HashMap<String, Object>();
        put(properties, CellUtil.ALIGNMENT, style.getAlignmentEnum());
        put(properties, CellUtil.VERTICAL_ALIGNMENT, style.getVerticalAlignmentEnum());
        put(properties, CellUtil.BORDER_BOTTOM, style.getBorderBottomEnum());
        put(properties, CellUtil.BORDER_LEFT, style.getBorderLeftEnum());
        put(properties, CellUtil.BORDER_RIGHT, style.getBorderRightEnum());
        put(properties, CellUtil.BORDER_TOP, style.getBorderTopEnum());
        put(properties, CellUtil.BOTTOM_BORDER_COLOR, style.getBottomBorderXSSFColor());
        put(properties, CellUtil.DATA_FORMAT, style.getDataFormat());
        put(properties, CellUtil.FILL_PATTERN, style.getFillPatternEnum());
        put(properties, CellUtil.FILL_FOREGROUND_COLOR, style.getFillForegroundColorColor());
        put(properties, CellUtil.FILL_BACKGROUND_COLOR, style.getFillBackgroundColorColor());
        put(properties, CellUtil.FONT, style.getFontIndex());
        put(properties, CellUtil.HIDDEN, style.getHidden());
        put(properties, CellUtil.INDENTION, style.getIndention());
        put(properties, CellUtil.LEFT_BORDER_COLOR, style.getLeftBorderXSSFColor());
        put(properties, CellUtil.LOCKED, style.getLocked());
        put(properties, CellUtil.RIGHT_BORDER_COLOR, style.getRightBorderXSSFColor());
        put(properties, CellUtil.ROTATION, style.getRotation());
        put(properties, CellUtil.TOP_BORDER_COLOR, style.getTopBorderXSSFColor());
        put(properties, CellUtil.WRAP_TEXT, style.getWrapText());
        return properties;
    }
    
    /**
     * Copies the entries in src to dest, using the preferential data type
     * so that maps can be compared for equality
     *
     * @param src the property map to copy from (read-only)
     * @param dest the property map to copy into
     * @since POI 3.15 beta 3
     */
    private static void putAll(final Map<String, Object> src, Map<String, Object> dest) {
        for (final String key : src.keySet()) {
            if (shortValues.contains(key)) {
                dest.put(key, getShort(src, key));
            } else if (colorValues.contains(key)) {
                dest.put(key, getColor(src, key));
            } else if (booleanValues.contains(key)) {
                dest.put(key, getBoolean(src, key));
            } else if (borderTypeValues.contains(key)) {
                dest.put(key, getBorderStyle(src, key));
            } else if (CellUtil.ALIGNMENT.equals(key)) {
                dest.put(key, getHorizontalAlignment(src, key));
            } else if (CellUtil.VERTICAL_ALIGNMENT.equals(key)) {
                dest.put(key, getVerticalAlignment(src, key));
            } else if (CellUtil.FILL_PATTERN.equals(key)) {
                dest.put(key, getFillPattern(src, key));
            } else {
                if (log.check(POILogger.INFO)) {
                    log.log(POILogger.INFO, "Ignoring unrecognized CellUtil format properties key: " + key);
                }
            }
        }
    }

    /**
     * Sets the format properties of the given style based on the given map.
     *
     * @param style cell style
     * @param workbook parent workbook
     * @param properties map of format properties (String -> Object)
     * @see #getFormatProperties(CellStyle)
     */
    private static void setFormatProperties(XSSFCellStyle style, Workbook workbook, Map<String, Object> properties) {
        style.setAlignment(getHorizontalAlignment(properties, CellUtil.ALIGNMENT));
        style.setVerticalAlignment(getVerticalAlignment(properties, CellUtil.VERTICAL_ALIGNMENT));
        style.setBorderBottom(getBorderStyle(properties, CellUtil.BORDER_BOTTOM));
        style.setBorderLeft(getBorderStyle(properties, CellUtil.BORDER_LEFT));
        style.setBorderRight(getBorderStyle(properties, CellUtil.BORDER_RIGHT));
        style.setBorderTop(getBorderStyle(properties, CellUtil.BORDER_TOP));
        XSSFColor color = getColor(properties, CellUtil.BOTTOM_BORDER_COLOR);
        if(color != null) {
        	style.setBottomBorderColor(color);
        }else {
        	//style.setBottomBorderColor(IndexedColors.BLACK.getIndex());//para que no le ponga ningun colo
        }
        style.setDataFormat(getShort(properties, CellUtil.DATA_FORMAT));
        style.setFillPattern(getFillPattern(properties, CellUtil.FILL_PATTERN));
        color = getColor(properties, CellUtil.FILL_FOREGROUND_COLOR);
        if(color != null) {
        	style.setFillForegroundColor(color);
        }else {
        	//style.setFillForegroundColor(IndexedColors.AUTOMATIC.getIndex());//para que no le ponga ningun colo
        }
        color = getColor(properties, CellUtil.FILL_BACKGROUND_COLOR);
        if(color != null) {
        	style.setFillBackgroundColor(color);
        }else {
        	//style.setFillBackgroundColor(IndexedColors.AUTOMATIC.getIndex());//para que no le ponga ningun colo
        }
        style.setFont(workbook.getFontAt(getShort(properties, CellUtil.FONT)));
        style.setHidden(getBoolean(properties, CellUtil.HIDDEN));
        style.setIndention(getShort(properties, CellUtil.INDENTION));
        color = getColor(properties, CellUtil.LEFT_BORDER_COLOR);
        if(color != null) {
        	style.setLeftBorderColor(color);
        }else {
        	//style.setLeftBorderColor(IndexedColors.BLACK.getIndex());//para que no le ponga ningun colo
        }
        style.setLocked(getBoolean(properties, CellUtil.LOCKED));
        color = getColor(properties, CellUtil.RIGHT_BORDER_COLOR);
        if(color != null) {
        	style.setRightBorderColor(color);
        }else {
        	//style.setRightBorderColor(IndexedColors.BLACK.getIndex());//para que no le ponga ningun colo
        }
        style.setRotation(getShort(properties, CellUtil.ROTATION));
        color = getColor(properties, CellUtil.TOP_BORDER_COLOR);
        if(color != null) {
        	style.setTopBorderColor(color);
        }else {
        	//style.setTopBorderColor(IndexedColors.BLACK.getIndex());//para que no le ponga ningun colo
        }
        style.setWrapText(getBoolean(properties, CellUtil.WRAP_TEXT));
    }

    private static XSSFColor getColor(Map<String, Object> properties, String name) {
    	Object value = properties.get(name);
        if (value instanceof XSSFColor) {
            return ((XSSFColor) value);
        }/*else if (value instanceof Short) {
        	return ((Short) value).shortValue();
        }*/
        return null;
	}
    
/*    private static Object getColorOrShort(Map<String, Object> properties, String name) {
    	Object value = properties.get(name);
        if (value instanceof XSSFColor) {
            return ((XSSFColor) value);
        }else if (value instanceof Short) {
        	return ((Short) value).shortValue();
        }
        return 0;
	}*/
    

	/**
     * Utility method that returns the named short value form the given map.
     *
     * @param properties map of named properties (String -> Object)
     * @param name property name
     * @return zero if the property does not exist, or is not a {@link Short}
     *         otherwise the property value
     */
    private static short getShort(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        if (value instanceof Short) {
            return ((Short) value).shortValue();
        }
        return 0;
    }
    
    /**
     * Utility method that returns the named BorderStyle value form the given map.
     *
     * @param properties map of named properties (String -> Object)
     * @param name property name
     * @return Border style if set, otherwise {@link BorderStyle#NONE}
     */
    private static BorderStyle getBorderStyle(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        BorderStyle border;
        if (value instanceof BorderStyle) {
            border = (BorderStyle) value;
        }
        // @deprecated 3.15 beta 2. getBorderStyle will only work on BorderStyle enums instead of codes in the future.
        else if (value instanceof Short) {
            if (log.check(POILogger.WARN)) {
                log.log(POILogger.WARN, "Deprecation warning: CellUtil properties map uses Short values for "
                        + name + ". Should use BorderStyle enums instead.");
            }
            short code = ((Short) value).shortValue();
            border = BorderStyle.valueOf(code);
        }
        else if (value == null) {
            border = BorderStyle.NONE;
        }
        else {
            throw new RuntimeException("Unexpected border style class. Must be BorderStyle or Short (deprecated).");
        }
        return border;
    }
    
    /**
     * Utility method that returns the named FillPatternType value form the given map.
     *
     * @param properties map of named properties (String -> Object)
     * @param name property name
     * @return FillPatternType style if set, otherwise {@link FillPatternType#NO_FILL}
     * @since POI 3.15 beta 3
     */
    private static FillPatternType getFillPattern(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        FillPatternType pattern;
        if (value instanceof FillPatternType) {
            pattern = (FillPatternType) value;
        }
        // @deprecated 3.15 beta 2. getFillPattern will only work on FillPatternType enums instead of codes in the future.
        else if (value instanceof Short) {
            if (log.check(POILogger.WARN)) {
                log.log(POILogger.WARN, "Deprecation warning: CellUtil properties map uses Short values for "
                        + name + ". Should use FillPatternType enums instead.");
            }
            short code = ((Short) value).shortValue();
            pattern = FillPatternType.forInt(code);
        }
        else if (value == null) {
            pattern = FillPatternType.NO_FILL;
        }
        else {
            throw new RuntimeException("Unexpected fill pattern style class. Must be FillPatternType or Short (deprecated).");
        }
        return pattern;
    }
    
    /**
     * Utility method that returns the named HorizontalAlignment value form the given map.
     *
     * @param properties map of named properties (String -> Object)
     * @param name property name
     * @return HorizontalAlignment style if set, otherwise {@link HorizontalAlignment#GENERAL}
     * @since POI 3.15 beta 3
     */
    private static HorizontalAlignment getHorizontalAlignment(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        HorizontalAlignment align;
        if (value instanceof HorizontalAlignment) {
            align = (HorizontalAlignment) value;
        }
        // @deprecated 3.15 beta 2. getHorizontalAlignment will only work on HorizontalAlignment enums instead of codes in the future.
        else if (value instanceof Short) {
            if (log.check(POILogger.WARN)) {
                log.log(POILogger.WARN, "Deprecation warning: CellUtil properties map used a Short value for "
                        + name + ". Should use HorizontalAlignment enums instead.");
            }
            short code = ((Short) value).shortValue();
            align = HorizontalAlignment.forInt(code);
        }
        else if (value == null) {
            align = HorizontalAlignment.GENERAL;
        }
        else {
            throw new RuntimeException("Unexpected horizontal alignment style class. Must be HorizontalAlignment or Short (deprecated).");
        }
        return align;
    }
    
    /**
     * Utility method that returns the named VerticalAlignment value form the given map.
     *
     * @param properties map of named properties (String -> Object)
     * @param name property name
     * @return VerticalAlignment style if set, otherwise {@link VerticalAlignment#BOTTOM}
     * @since POI 3.15 beta 3
     */
    private static VerticalAlignment getVerticalAlignment(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        VerticalAlignment align;
        if (value instanceof VerticalAlignment) {
            align = (VerticalAlignment) value;
        }
        // @deprecated 3.15 beta 2. getVerticalAlignment will only work on VerticalAlignment enums instead of codes in the future.
        else if (value instanceof Short) {
            if (log.check(POILogger.WARN)) {
                log.log(POILogger.WARN, "Deprecation warning: CellUtil properties map used a Short value for "
                        + name + ". Should use VerticalAlignment enums instead.");
            }
            short code = ((Short) value).shortValue();
            align = VerticalAlignment.forInt(code);
        }
        else if (value == null) {
            align = VerticalAlignment.BOTTOM;
        }
        else {
            throw new RuntimeException("Unexpected vertical alignment style class. Must be VerticalAlignment or Short (deprecated).");
        }
        return align;
    }

    /**
     * Utility method that returns the named boolean value form the given map.
     *
     * @param properties map of properties (String -> Object)
     * @param name property name
     * @return false if the property does not exist, or is not a {@link Boolean},
     *         true otherwise
     */
    private static boolean getBoolean(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        //noinspection SimplifiableIfStatement
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return false;
    }
    
    /**
     * Utility method that puts the given value to the given map.
     *
     * @param properties map of properties (String -> Object)
     * @param name property name
     * @param value property value
     */
    private static void put(Map<String, Object> properties, String name, Object value) {
        properties.put(name, value);
    }

    /**
     *  Looks for text in the cell that should be unicode, like &alpha; and provides the
     *  unicode version of it.
     *
     *@param  cell  The cell to check for unicode values
     *@return       translated to unicode
     */
    public static Cell translateUnicodeValues(Cell cell) {

        String s = cell.getRichStringCellValue().getString();
        boolean foundUnicode = false;
        String lowerCaseStr = s.toLowerCase(Locale.ROOT);

        for (UnicodeMapping entry : unicodeMappings) {
            String key = entry.entityName;
            if (lowerCaseStr.contains(key)) {
                s = s.replaceAll(key, entry.resolvedValue);
                foundUnicode = true;
            }
        }
        if (foundUnicode) {
            cell.setCellValue(cell.getRow().getSheet().getWorkbook().getCreationHelper()
                    .createRichTextString(s));
        }
        return cell;
    }

    static {
        unicodeMappings = new UnicodeMapping[] {
            um("alpha",   "\u03B1" ),
            um("beta",    "\u03B2" ),
            um("gamma",   "\u03B3" ),
            um("delta",   "\u03B4" ),
            um("epsilon", "\u03B5" ),
            um("zeta",    "\u03B6" ),
            um("eta",     "\u03B7" ),
            um("theta",   "\u03B8" ),
            um("iota",    "\u03B9" ),
            um("kappa",   "\u03BA" ),
            um("lambda",  "\u03BB" ),
            um("mu",      "\u03BC" ),
            um("nu",      "\u03BD" ),
            um("xi",      "\u03BE" ),
            um("omicron", "\u03BF" ),
        };
    }

    private static UnicodeMapping um(String entityName, String resolvedValue) {
        return new UnicodeMapping(entityName, resolvedValue);
    }
}
