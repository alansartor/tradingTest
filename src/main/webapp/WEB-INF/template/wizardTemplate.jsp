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
 		if (document.getElementById('buttonCancelar')) {
 	 		$jQ("#buttonCancelar").on("click", function (e) {
 	 			window.location.href = $jQ("#applicationContextPath").val() + "/web/GENERAL/HOME?cancel=true";
 	 	    });
 		}
 		//El input donde viene el _cid (si se guardo el dto en el SessionAttributes del controller) no tiene id, entonces lo busco por name y le pongo id
 		//ya que si lo manejo por name y no existe, entonce la referencia 
 		var inputCid = $jQ('[name="_cid"]').first();
 		if(inputCid.length !== 0){
 			$jQ(inputCid).attr("id", "_cid");
 		}else{
 			$jQ("#entidadDTO").before('<input type="hidden" id="_cid"/>');
 		}
 		//A los linkBreadcrumb le agrego el atributo _cid para que Spring resuleba el SessionAttributes
 		$jQ(".linkBreadcrumb").each(function( index ) {
			var link = this.getAttribute("href");
			if (link.indexOf('?') > -1){
				this.setAttribute("href", link.substring(0, link.length) + "&_cid="+document.getElementById('_cid').value);//+ "'" NEW 07/04/2019 saco esta comilla que hace que el cid sea invalido, no se porque estaba!
			}else{
				this.setAttribute("href", link.substring(0, link.length) + "?_cid="+document.getElementById('_cid').value);//+ "'"
			}
		});
	});
</script>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<tiles:importAttribute name="back"/>
<tiles:importAttribute name="showButtonGuardar" ignore="true"/>
<tiles:importAttribute name="notShowNextButton" ignore="true"/>

<input type="hidden" id="showButtonGuardar" value="${showButtonGuardar}"/>
<input type="hidden" id="notShowNextButton" value="${notShowNextButton}"/>

<spring:hasBindErrors name="entidadDTO">
	<c:if test="${!empty errors && !empty errors.allErrors}">
		<input id="firtsSpringBindErrorFiled" type="hidden" value="${errors.allErrors[0].field}"/>
	</c:if>
</spring:hasBindErrors>
<form:form modelAttribute="entidadDTO" name="entidadDTO" class="form-horizontal" action="${accion}">
<div class="panel panel-default my-no-border-formpanel">
  <div class="panel-body my-formpanel-body">
  	<div class="form-group my-formheader"><div id="my-form-title-div-id" class="col-md-12 col-xs-12"><div class="col-md-10"><div class="pull-left"><h3 class="my-formheader-title">${Accion.getIconForMenuAction(accion)}<spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments=",,${empty entidadDTO.id?'Nuevo ':'Edicion '}" htmlEscape="false"/><small id="tituloSmallText"></small></h3></div></div>
  	<div class="col-md-2"><div id="divDerechaTitulo" class="pull-right"><h3 class="my-formheader-title">${Accion.getIconClaseForForm(clase)}</h3></div></div></div></div>
  	<tiles:insertAttribute name="body" />
    <!-- Button (Double) -->
	<div class="form-group" style="margin-right: 0px;margin-left: 0px;">
	  <div id="my-form-buttons-div-id" class="col-md-12 col-xs-12">
	  	<!-- <hr /> -->
	  	<div class="pull-right"><!-- meto el boton dentro de un div, ya que hay casos en que agrgeo botones por codigo y para que queden bien tiene que estar asi -->
	  		<c:choose>
				<c:when test="${!empty back}">
				  <button type="button" id="buttonAtras" name="buttonAtras" class="btn btn-default btn-cancel" tabIndex="-1" onclick="window.location.href='${back}?_cid='+document.getElementById('_cid').value"><span class="glyphicon glyphicon-step-backward"></span> Atras</button>
				</c:when>
				<c:otherwise>
				  <button type="button" id="buttonCancelar" name="buttonCancelar" class="btn btn-default btn-cancel" tabIndex="-1"><span class="glyphicon glyphicon glyphicon-remove"></span> Cancelar</button>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${showButtonGuardar == 'true'}">
	 				<button type="submit" id="buttonSubmit" name="buttonGuardar" class="btn btn-success pull-right"><span class="glyphicon glyphicon-floppy-disk"></span> Guardar</button>
				</c:when>
				<c:when test="${notShowNextButton == 'true'}"></c:when>			
				<c:otherwise>
					<button type="submit" id="buttonSubmit" name="buttonSiguiente" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-step-forward"></span> Siguiente</button>
				</c:otherwise>
			</c:choose>
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