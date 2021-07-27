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
	    <p class="list-group-item-text">La Materia Prima Cosechada se calculará automaticamente cuando registre la "Labor Tipo COSECHA". Una vez que registró todos los Movimientos con <i>Origen de la Operacion: Produccion</i> entonces dirijase a <i>Listar Producciones</i>, busque la Produccion y selecciones la opcion <i>Imputar Labor Cosecha</i></p>
	  </a>	  	   
	</div>					
</div>