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
	    <h4 class="list-group-item-heading">Canje Cosecha</h4>
	    <p class="list-group-item-text">Para este caso no se tendra en cuenta el IVA en esta etapa</p>
	  </a>
	  <a href="#" class="list-group-item">
	    <h4 class="list-group-item-heading">Canje Disponible</h4>
	    <p class="list-group-item-text"></p>
	  </a>
	  <a href="#" class="list-group-item">
	    <h4 class="list-group-item-heading">Cheque</h4>
	    <p class="list-group-item-text">Todo cheque a mas de 30 dias</p>
	  </a>
	  <a href="#" class="list-group-item">
	    <h4 class="list-group-item-heading">Contado</h4>
	    <p class="list-group-item-text">Pago de Contado, o cheques a 30 dias o menos</p>
	  </a>	  	   
	</div>					
</div>