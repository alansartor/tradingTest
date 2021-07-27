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
	    	<h4 class="list-group-item-heading">Registros en Verde <div style="display: inline-block;border:1px solid #000;width:60px;height:18px;background-color: #A9F2A9;"></div></h4>
	    	<p class="list-group-item-text">Todas sus lineas fueron imputadas a 'Labores/Gastos Globales' por el 100%<p/>
		</a>
	  	<a href="#" class="list-group-item">
	    	<h4 class="list-group-item-heading">Registros en Amarillo <div style="display: inline-block;border:1px solid #000;width:60px;height:18px;background-color: #EEF19A;"></div></h4>
	    	<p class="list-group-item-text">Parcialmente Imputado, quiere decir que hay al menos una linea del comprobante que no fue imputada o que esta parcialemnte imputada<p/>
		</a>
	  	<a href="#" class="list-group-item">
	    	<h4 class="list-group-item-heading">Registros en Marron <div style="display: inline-block;border:1px solid #000;width:60px;height:18px;background-color: #CC9900;"></div></h4>
	    	<p class="list-group-item-text">Comprobantes que no se pueden imputar. Estos pueden ser:<p/>
			<ul>
			  <li>Remitos que tienen una Factura asociada (las imputaciones pasan a la Factura)</li>
			  <li>Facturas Anuladas por una Nota de Credito</li>
			  <li>Facturas con condicion de pago 'Canje Cosecha' que fueron Refacturdas (las imputaciones pasan a la nueva factura)</li>
			</ul> 
	    </a>		
	  	<a href="#" class="list-group-item">
	    	<h4 class="list-group-item-heading">Registros en Blanco (y Gris) <div style="display: inline-block;border:1px solid #000;width:60px;height:18px;background-color: #FFF;"></div></h4>
	    	<p class="list-group-item-text">Comprobantes que estan pendiente de Imputación<p/>
	    </a>
	</div>						
</div>