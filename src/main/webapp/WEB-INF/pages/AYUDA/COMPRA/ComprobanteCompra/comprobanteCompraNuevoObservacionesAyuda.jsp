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
		<p>Ingrese un texto que le sea de utilidad a la hora de seleccionar los comprobantes en las Labores, ejemplo: la especie y opcionalmente el lote donde se uso o se va a usar<br/>
		Tambien puede ingresar una observacion distinta a cada linea del comprobante<p/>
	</div>						
</div>