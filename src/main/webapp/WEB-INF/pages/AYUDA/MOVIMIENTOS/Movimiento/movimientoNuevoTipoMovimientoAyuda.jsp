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
	    <h4 class="list-group-item-heading">Entrada</h4>
	    <p class="list-group-item-text">Se debe utilizar cuando lo que se esta cosechando se va a acopiar en el campo o cuando se esta trayendo alguna mercaderia de otro lugar, ya sea un tercero, acopio alquilado u otro campo propio</p>
	  </a>
	  <a href="#" class="list-group-item">
	    <h4 class="list-group-item-heading">Salida</h4>
	    <p class="list-group-item-text">Utilizar esta opcion cuando los que se esta cosechando se manda directamente a un Acopio o Exportador externo, o cualquier movimiento de mercaderia en la que el destino sea fuera del campo</p>
	  </a>  
	</div>					
</div>