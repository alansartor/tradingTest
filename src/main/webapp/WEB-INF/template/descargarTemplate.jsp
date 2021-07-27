<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="ar.com.signals.trading.general.support.Accion" %>
<style>
.btn-cancel{
	margin-right: 15px;
	border: none;
	background-color: #f5f5f5
}
</style>
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/validator/validator.min.js"></script>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
 	//NEW 09/11/2018 Paso este codigo arriba, para que se cargue antes que la jsp child, entonces dentro de la jsp child puedo cambiar el comportamiento del entidadDTOValidator en el submit pisando el entidadDTOValidator.on('submit'
 	//NEW Si piso el comportamiento del submit, ya sea haciendo un nuevo entidadDTOValidator.on('submit' o haciendo un entidadDTOValidator.off('submit').on('submit', entonces el validator no funciona bien, el e.isDefaultPrevented() deja de andar y siempre se hace el submit
 	//entonces para evitar eso, creo una variable que se debe llenar con una funcion en la implementacion del template para agregar funcionalidad extra!
 	var entidadDTOValidator;
 	var onSubmitExtraValidationFunction;
 	var onSubmitInvalidFormValidationFunction;
 	$jQ(function () {
 		//$jQ('[data-toggle="tooltip"]').tooltip({trigger: 'focus'});esta en el maintemplate
 		entidadDTOValidator = $jQ("#entidadDTO").validator();//31/07/2018 para poder referenciarlo, se usa para cuando agregamos fields dinamicamente y queremos hacer un update al validator para que los valide
 		entidadDTOValidator.on('submit', function (e) {
 			if (e.isDefaultPrevented()) {
				// handle the invalid form...
 				$jQ(".has-error:first :input").focus();//para que el foco vaya al primer error en los casos que no se hace submit
				// handle the invalid form...
 				if(typeof onSubmitInvalidFormValidationFunction !== 'undefined'){
 					onSubmitInvalidFormValidationFunction();
 				}
			} else {
				//Para agregar validacion extra en la implementacion del template!
				if(typeof onSubmitExtraValidationFunction !== 'undefined'){
					if(onSubmitExtraValidationFunction()){
						// everything looks good!
						waitingDialog.show('Aguarde', {dialogSize: 'sm'});
						$jQ("#buttonSubmit").attr('disabled', true);
					}else{
						e.preventDefault();//para anular el comportamiento habitual del submit
					}
				}else{
					// everything looks good!
					waitingDialog.show('Aguarde', {dialogSize: 'sm'});
					$jQ("#buttonSubmit").attr('disabled', true);
				}
			}
 		});
 		$jQ.fn.validator.Constructor.FOCUS_OFFSET=100;
 		$jQ("#buttonCancelar").on("click", function (e) {
 			if($jQ("#popUpFrame").val()){
 				window.parent.postMessage('message', '*');//esto le da la señal al parent para que cierre el Dialog
 			}else{
 	 			window.location.href = $jQ("#applicationContextPath").val() + "/web/GENERAL/HOME?cancel=true";
 			}
 	    });
	});
</script>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<tiles:importAttribute name="popUpFrame" ignore="true"/>

<input type="hidden" id="popUpFrame" value="${popUpFrame}"/>
<!-- Se usa para hacer foco en el primer filed con error, aca se registra en un hidden el primer field con error, y luego en el ready se hace el foco -->
<spring:hasBindErrors name="entidadDTO">
	<c:if test="${!empty errors && !empty errors.allErrors}">
		<input id="firtsSpringBindErrorFiled" type="hidden" value="${errors.allErrors[0].field}"/>
	</c:if>
</spring:hasBindErrors>
<form:form modelAttribute="entidadDTO" name="entidadDTO" class="form-horizontal" target="${popUpFrame}"><!-- Cuando se abre un NuevoTemplate como PopUp, entonces la respuesta se debe mandar de nuevo al iframe -->
<div class="panel panel-default my-no-border-formpanel">
  <div class="panel-body my-formpanel-body">
  	<div class="form-group my-formheader"><div id="my-form-title-div-id" class="col-md-12 col-xs-12"><div class="col-md-10"><div class="pull-left"><h3 class="my-formheader-title">${Accion.getIconForMenuAction(accion)}<spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments="," htmlEscape="false"/><small id="tituloSmallText"></small></h3></div></div>
  	<div class="col-md-2"><div id="divDerechaTitulo" class="pull-right"><h3 class="my-formheader-title">${Accion.getIconClaseForForm(clase)}</h3></div></div></div></div>
  	<tiles:insertAttribute name="body" />
    <!-- Button (Double) -->
 	<div class="form-group" style="margin-right: 0px;margin-left: 0px;">	
	  <div id="my-form-buttons-div-id" class="col-md-12 col-xs-12">
	  	<!-- <hr /> -->
	  	<div class="pull-right"><!-- meto el boton dentro de un div, ya que hay casos en que agrgeo botones por codigo y para que queden bien tiene que estar asi -->
	  		<button type="button" id="buttonCancelar" name="buttonCancelar" tabIndex="-1" class="btn btn-default btn-cancel"><span class="glyphicon glyphicon glyphicon-remove"></span> Cancelar</button>	  	
	  		<button type="submit" id="buttonSubmit" name="buttonSubmit" class="btn btn-success pull-right"><span class="glyphicon glyphicon-download-alt"></span> Descargar</button>
	  	</div>
	  </div>
	</div>
  </div>
</div>
</form:form>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		//OJO: ESTE READY ES SOLO PARA ACCIONES QUE SE DEBEN EJECUTAR DESPUES DEL READY DEL BODY!
		//Este foco se ejecuta despues del body, ya que por defecto quiza haya un foco en algun field, y aca se cambia el foco a algun field con error
		//si tiene error que viene del BindingResult de Spring, entonces pongo el foco ahi!
		var firsErrorFieldId = $jQ('#firtsSpringBindErrorFiled').val();
		if(firsErrorFieldId){
			$jQ('#'+firsErrorFieldId).focus();
		}
	});
</script>