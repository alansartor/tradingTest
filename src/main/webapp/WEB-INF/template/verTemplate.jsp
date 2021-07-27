<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="ar.com.signals.trading.general.support.Accion" %>
<style></style>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<form:form modelAttribute="entidadDTO" name="entidadDTO" class="form-horizontal">	
<div class="panel panel-default my-no-border-formpanel">
  <div class="panel-body my-formpanel-body">
	<div class="form-group my-formheader"><div id="my-form-title-div-id" class="col-md-12 col-xs-12"><div class="col-md-10"><div class="pull-left"><h3 class="my-formheader-title">${Accion.getIconForMenuAction(accion)}<spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments="${entidadDTO}," htmlEscape="false"/><small id="tituloSmallText"></small></h3></div></div>
	<div class="col-md-2"><div id="divDerechaTitulo" class="pull-right"><h3 class="my-formheader-title">${Accion.getIconClaseForForm(clase)}</h3></div></div></div></div>
  	<tiles:insertAttribute name="body" />
  	<tiles:insertAttribute name="bodyExtended" />
  </div>
  <div class="form-group" id="panelFooterWraper.${modulo}.${clase}.${accion}" style="display: none;margin-right: 0px;margin-left: 0px;"><!-- Se agrega este div pero vacio, entonces en caso de querer agregar algun boton se puede hacer con jquery! Se debe poner visible tambien, porque por defeto se oculta!-->
	  <div id="my-form-buttons-div-id" class="col-md-12 col-xs-12">
	  	<!-- <hr /> -->
	  	<div class="pull-right" id="panelFooter.${modulo}.${clase}.${accion}"></div>
	  </div>
  </div>
</div>
</form:form>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
 	$jQ(function () {
 		//$jQ('[data-toggle="tooltip"]').tooltip({trigger: 'focus'});esta en el maintemplate
	});
</script>