<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style></style>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<div class="container-fluid" id="ayudaTemplateTitleDiv"><div class="row"><div class="col-md-10"><div class="pull-left"><h3 id="ayudaTemplateTitleText">
<spring:message code="ayuda.${modulo}.${clase}.${accion}" arguments="${subClase}," htmlEscape="false"/></h3></div></div><div class="col-md-2"><div id="divDerechaTitulo" class="pull-right"></div></div></div></div>
<div class="form-group form-group-sm">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<a href="#" class="list-group-item">
	    	<h4 class="list-group-item-heading">Compra</h4>
	    	<p class="list-group-item-text">Es la suma de los importes en Pesos de todos los Comprobantes de Compra. Se usa la Fecha de Vencimiento para ubicarlo en el mes que corresponda. Los comprobantes en Dolar se pasan a Pesos con la cotizacion que figura en el comprobante. Se usa el Importe Total del comprobante, es decir que incluye IVA y Percepciones<p/>
		</a>
	  	<a href="#" class="list-group-item">
	    	<h4 class="list-group-item-heading">Venta</h4>
	    	<p class="list-group-item-text">Es la suma de los importes en Pesos de todos los Comprobantes de Venta. Se usa la Fecha de Vencimiento para ubicarlo en el mes que corresponda. Los comprobantes en Dolar se pasan a Pesos con la cotizacion que figura en el comprobante. Se usa el Importe Total del comprobante, es decir que incluye IVA, Deducciones y Retenciones<p/>
		</a>
	  	<a href="#" class="list-group-item">
	    	<h4 class="list-group-item-heading">Saldo</h4>
	    	<p class="list-group-item-text">El el Saldo del Mes, que se calcula sumandole las Ventas al Saldo del mes anterior y restandole las Ventas. Para el calculo del Saldo del primer mes, se usa el Saldo Inicial del Ejercicio<p/>
		</a>		
	</div>						
</div>