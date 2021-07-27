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
	<div class="list-group">
	  <a href="#" class="list-group-item">
	    <h4 class="list-group-item-heading">CARTA DE PORTE PARA EL TRANSPORTE AUTOMOTOR DE GRANOS</h4>
	    <p class="list-group-item-text">Se debe utilizar en los movimientos que se hagan con Carta de Porte y que se trasnportaron en transporte automotor. Se debe ingresar tambien en Nro de Ctg</p>
	  </a>
	  <a href="#" class="list-group-item">
	    <h4 class="list-group-item-heading">COMPROBANTE INTERNO</h4>
	    <p class="list-group-item-text">Se usa generalmente para Movimientos de Entrada, provenientes de la Produccion. Si no posee una numeracion propia, el sistema le asignara un valor, solo debe presionar el boton <span class="fas fa-cogs"></span></p>
	  </a>	  	   
	</div>					
</div>