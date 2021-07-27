package ar.com.signals.trading.util.support;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static <E> void setValue(Cell cell, Field field, Method method, E registro) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(field.getType().isAssignableFrom(String.class)){
			cell.setCellValue((String) method.invoke(registro));
		}else if(field.getType().isAssignableFrom(Boolean.class) || field.getType().isAssignableFrom(boolean.class)){
			cell.setCellValue((Boolean) method.invoke(registro));
		}else if(field.getType().isAssignableFrom(Date.class)){
			Date value = (Date) method.invoke(registro);
			if(value!=null){
				cell.setCellValue(value);
			}
		}else if(field.getType().isAssignableFrom(Timestamp.class)){
			Timestamp value = (Timestamp) method.invoke(registro);
			if(value!=null){
				cell.setCellValue((Date) value);
			}
		}else if(field.getType().isAssignableFrom(Double.class) || field.getType().isAssignableFrom(double.class)){
			Double value = (Double) method.invoke(registro);
			if(value!=null){
				cell.setCellValue(value);
			}
		}else if(field.getType().isAssignableFrom(Long.class) || field.getType().isAssignableFrom(long.class)){
			Long value = (Long) method.invoke(registro);
			if(value!=null){
				cell.setCellValue(value.doubleValue());
			}
		}else if(field.getType().isAssignableFrom(Integer.class) || field.getType().isAssignableFrom(int.class)){
			Integer value = (Integer) method.invoke(registro);
			if(value!=null){
				cell.setCellValue(value.doubleValue());
			}	
		}else{
			Object value = (Object) method.invoke(registro);
			if(value!=null){
				cell.setCellValue(value.toString());
			}
		}
	}
	
	public static Cell setValue(Cell cell, Object value, Map<String, Object> styleProperties) {
		setValue(cell, value);
		MyCellUtil.setCellStyleProperties((XSSFCell) cell, styleProperties);
		return cell;
	}

	public static Cell setValue(Cell cell, Object value) {
		if(value == null){
			return cell;
		}
		if(value.getClass().isAssignableFrom(String.class)){
			cell.setCellValue((String) value);
		}else if(value.getClass().isAssignableFrom(Boolean.class) || value.getClass().isAssignableFrom(boolean.class)){
			cell.setCellValue((Boolean) value);
		}else if(value.getClass().isAssignableFrom(Date.class)){
			cell.setCellValue((Date) value);
		}else if(value.getClass().isAssignableFrom(Long.class) || value.getClass().isAssignableFrom(long.class)){
			cell.setCellValue(((Long) value).doubleValue());
		}else if(value.getClass().isAssignableFrom(Integer.class) || value.getClass().isAssignableFrom(int.class)){
			cell.setCellValue(((Integer) value).doubleValue());
		}else if(value.getClass().isAssignableFrom(BigDecimal.class)){
			cell.setCellValue(((BigDecimal) value).doubleValue());
		}else{
			cell.setCellValue(String.valueOf(value));
		}
		return cell;
	}
	
	public static XSSFCellStyle getStyle(XSSFWorkbook workbook, Map<MyStyle, XSSFCellStyle> mapStyles, MyStyle myStyle) {
		XSSFCellStyle cellStyle = mapStyles.get(myStyle);
		if(cellStyle!=null) {
			return cellStyle;
		}
		cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		cellStyle.setFont(font);
		switch (myStyle) {
		case BOLD:
			font.setBold(true);
			return cellStyle;
		default:
			return cellStyle;
		}
	}
}
