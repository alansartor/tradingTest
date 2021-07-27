package ar.com.signals.trading.support;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.stereotype.Controller;

/**
 * Esta clase modifica el binder de todos los Controller para
 * que los bind de los String devuelva null en vez de empty
 * @author pepe@hotmail.com
 *
 */
@ControllerAdvice(annotations = Controller.class)//Target all Controllers annotated with @Controller
public class ControllerGlobalSetup {
    @InitBinder
    public void initBinder (WebDataBinder binder )
    {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
        
        //para que @NumberFormat muestre los BigDecimal con un . como separador decimal
        //Tuve que poner este patern aca porque sino me tomaba por defecto solo 3 decimales, ya que se ignoraba el @NumberFormat de los dtos!
        DecimalFormat df = new DecimalFormat("0.######");//IMPORTANTE: si aparece alguna variable que se muestra en pantalla que requiere mas presicion, entonces agrandar la presicion aca!
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        //dfs.setGroupingSeparator(',');
        df.setGroupingUsed(false);
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, df, true));
    }
}
