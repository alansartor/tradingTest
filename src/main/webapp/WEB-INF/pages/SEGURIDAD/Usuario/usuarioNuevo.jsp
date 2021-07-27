<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/css/bootstrap-toggle.min.css">
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/js/bootstrap-toggle.min.js"></script>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ(function() {
		$jQ("#my-form-buttons-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		$jQ("#my-form-title-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		$jQ("#username").attr("data-minlength", "4");
		$jQ("#email").attr("type", "email");
		if($jQ("#accionValue").val() == "EDITAR"){
			$jQ('#user_enabled').bootstrapToggle();
		}else{
			$jQ('#username').focus();
			$jQ('#user_enabled').bootstrapToggle();
			$jQ('#user_enabled').parent().on("click", function(e) {//el toogle agrega un div y esconde el input, entonces miro el clik en el div!
				e.stopImmediatePropagation();//cancela la operacion, hago de esta forma porque no se puede poner readolnly el toogle y si uso el disable no se manda en el post! No usar: $jQ('#activo').bootstrapToggle('disable');
			}).css( "opacity", ".65" );
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
						<html:inputField name="username" base="${modulo}.${clase}.${labelAccion}" readonly="${accion == 'EDITAR'?true:false}" obligatorio="true" labelSize="5" inputSize="4"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="descripcion" base="${modulo}.${clase}.${labelAccion}" obligatorio="true"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="form-group">
						    <label class="col-md-5 control-label" for="password">Password</label>
						    <div class="col-md-7">
						      <p class="form-control-static">1234</p>
						      <form:hidden path="password"/>
						    </div>
					  	</div>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="user_email" base="${modulo}.${clase}.${labelAccion}"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:checkboxField name="user_enabled" base="${modulo}.${clase}.${labelAccion}"/>
			  		</div>
				</div>
			  </div>
			</div>
		</div>	
	</div>	
</div>