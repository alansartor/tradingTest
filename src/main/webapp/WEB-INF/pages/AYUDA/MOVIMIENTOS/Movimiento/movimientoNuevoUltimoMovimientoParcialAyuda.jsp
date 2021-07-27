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
	    <p class="list-group-item-text">Se usa en los movimientos con Origen Produccion, para los casos en que el ultimo camion que carga mercaderia de la produccion tiene que completar la carga con mercaderia de otra produccion. Solo estos casos se debe marcar el movimiento como "Mercaderia de dos Producciones", esto habilitara que luego al registrar la Labor Cosecha, se pueda imputar el movimiento en dos Labores!</p>
	  </a>	  	   
	</div>					
</div>