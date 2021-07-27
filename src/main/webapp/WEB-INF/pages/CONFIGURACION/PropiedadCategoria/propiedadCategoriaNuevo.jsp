<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%><!-- los custom tag estan en ml.jar, en teoria para hacerlos visibles habria que armar un archivo TLD, pero lo probe sin eso y sigue funcionando -->
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/css/bootstrap-toggle.min.css">
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/js/bootstrap-toggle.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/moment.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		$jQ("#my-form-buttons-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		$jQ("#my-form-title-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		if($jQ("#accionValue").val() == "VER"){
			$jQ('#activo').bootstrapToggle();
			$jQ('#activo').parent().on("click", function(e) {//el toogle agrega un div y esconde el input, entonces miro el clik en el div!
				e.stopImmediatePropagation();//cancela la operacion, hago de esta forma porque no se puede poner readolnly el toogle y si uso el disable no se manda en el post! No usar: $jQ('#activo').bootstrapToggle('disable');
			}).css( "opacity", ".65" );
		}else{
			$jQ('#activo').bootstrapToggle();
		}
  	});
</script>
<div class="body">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
    <tiles:importAttribute name="accion"/>
	<tiles:importAttribute name="labelAccion"/>
	<input id="accionValue" type="hidden" value="${accion}"/>
	
	<form:hidden path="id"/>
	<div class="form-group form-group-sm">
		<div class=" col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<div class="panel panel-default my-panel">
			  <div class="panel-body my-panel-body">
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:selectEnumField name="codigo" base="${modulo}.${clase}.${labelAccion}" readonly="${accion == 'EDITAR' or accion == 'VER'?true:false}" obligatorio="true"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="valor" base="${modulo}.${clase}.${labelAccion}" readonly="${accion == 'VER'?true:false}" obligatorio="false" labelSize="5" inputSize="4"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:checkboxField name="activo" base="${modulo}.${clase}.${labelAccion}"/>
					</div>
				</div>	
			  </div>
			</div>
		</div>	
	</div>	
</div>