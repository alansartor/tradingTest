<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style>
/*TODO ESTO ES PARA HACER FULL HEIGTH LA PAGINA, ES DECIR PONGO LOS CONTENEDORES AL 100%, Y EL panel-body to take the remaining height */
html, body {
  height: 100%;
  margin: 0;
}
.panel-default, .page, .content {
  height: 100%;
}
.panel {
  display: flex;/*Esto es relativamente nuevo, entonces puede llegar a fallar en navegadores muy antiguos, no abusar!*/
  flex-flow: column;
  margin-bottom: 0px;/*panel tiene un margen de 20px que provoca que aparezcan el scroll vertical, entonces lo pongo en cero*/
}
.panel-body {
	flex-grow : 1;/* take the remaining height (el que lo contine debe tener display: flex;, y todos la pagina tiene que tener height: 100%;)*/
}
</style>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<div class="container-fluid" id="ayudaTemplateTitleDiv"><div class="row"><div class="col-md-10"><div class="pull-left"><h3 id="ayudaTemplateTitleText">
<spring:message code="ayuda.${modulo}.${clase}.${accion}" arguments="${subClase}," htmlEscape="false"/></h3></div></div><div class="col-md-2"><div id="divDerechaTitulo" class="pull-right"></div></div></div></div>
<div class="panel panel-default">
  <div class="panel-body">
  	<tiles:insertAttribute name="body" />
  	<tiles:insertAttribute name="bodyExtended" />
  </div>
  <div class="panel-footer"><!-- Se agrega este div pero vacio, entonces en caso de querer agregar algun boton se puede hacer con jquery! -->
 	<div class="form-group" id="panelFooter.${modulo}.${clase}.${accion}"></div>
  </div>
</div>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
 	$jQ(function () {
 		//$jQ('[data-toggle="tooltip"]').tooltip({trigger: 'focus'});esta en el maintemplate
	});
</script>