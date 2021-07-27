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
	    <h4 class="list-group-item-heading">Movimientos con CARTA DE PORTE</h4>
	    <p class="list-group-item-text">Se deben registrar el Neto en Destino. En caso de no saber este valor todavia, repetir el neto de procedencia y despues se modificara al momento de registrar el Certificado de Deposito</p>
	  </a>
	  <a href="#" class="list-group-item">
	    <h4 class="list-group-item-heading">Otros Movimientos</h4>
	    <p class="list-group-item-text">Si se tiene el valor del Neto en Destino, ingresar ese dato, caso contrario repetir el neto de procedencia</p>
	  </a>	  	   
	</div>					
</div>