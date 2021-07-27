<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ(function() {
		$jQ("#password").prop('type', 'password');
		$jQ("#passwordNew").prop('type', 'password');
		$jQ("#passwordNew2").prop('type', 'password');
		
		//$jQ("#username").attr("data-minlength", "4");
		$jQ("#user_email").attr("type", "email");
 		$jQ("#buttonCancelarTelegram").on("click", function (e) {
 			window.location.href = $jQ("#applicationContextPath").val() + "/web/SEGURIDAD/Usuario/RESETEAR/Telegram"; 			
 	    });
  	});
</script>
<div class="body">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
	<tiles:importAttribute name="labelAccion"/>
	<form:hidden path="id"/>
	<div class="form-group form-group-sm row-eq-height">
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 my-wrapper-corrector">
			<div class="panel panel-default my-panel my-panel-corrector">
			  <div class="panel-body my-panel-body">
			    <h4 class="my-panel-title">Informacion Personal</h4>
			    <hr class="my-panel-title-divider" />	
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputDisabled name="username" base="${modulo}.${clase}.${labelAccion}" labelSize="5" inputSize="4"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="descripcion" base="${modulo}.${clase}.${labelAccion}" obligatorio="true"/>
			  		</div>
				</div>			
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="user_email" base="${modulo}.${clase}.${labelAccion}" labelSize="5" inputSize="5"/>
			  		</div>
				</div>
			    <h4 class="my-panel-title">Contraseña</h4>
			    <hr class="my-panel-title-divider" />
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="alert alert-info" role="alert">Si desea modificar la contraseña, complete estos campos:</div>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="password" base="${modulo}.${clase}.${labelAccion}" labelSize="5" inputSize="4"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="passwordNew" base="${modulo}.${clase}.${labelAccion}" labelSize="5" inputSize="4"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="passwordNew2" base="${modulo}.${clase}.${labelAccion}" labelSize="5" inputSize="4"/>
			  		</div>
				</div>
		     </div>
		   </div>
	   </div>
	   <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 my-wrapper-corrector">
			<div class="panel panel-default my-panel my-panel-corrector">
			  <div class="panel-body my-panel-body">
			    <h4 class="my-panel-title">Notificaciones</h4>
			    <hr class="my-panel-title-divider" />
			    <c:choose>        
			         <c:when test = "${!empty entidadDTO.tokenTelegram}">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="jumbotron" style="padding-left: 48px;padding-right: 48px;border-radius: 4px;">
							  <h2><strong>Telegram</strong> <i class="fab fa-telegram-plane"></i></h2>
							  <p>Para poder recibir mensajes del Sistema en su celular tiene que tener instalada la Aplicacion <strong>Telegram</strong> y deberá <strong>INICIAR</strong> una conversacion con <strong>@${telegramBotName}</strong></p>
							  <p>Token de vinculacion: <strong>${entidadDTO.tokenTelegram.token}</strong></p>
							  <small>(*debe usar ese token antes de las <fmt:formatDate pattern="HH:mm" value="${entidadDTO.tokenTelegram.expirationTime}"/>, pasada esa hora debera volver a esta pantalla para buscar un nuevo codigo)</small>
							</div>
						</div>
			         </c:when>    
			         <c:otherwise>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="jumbotron" style="padding-left: 48px;padding-right: 48px;border-radius: 4px;">
							  <h2><strong>Telegram</strong> <i class="fab fa-telegram-plane"></i></h2>
							  <p>Usted ya tiene asociado la Aplicacion <strong>Telegram</strong> en un celular</p>
							  <p>Si desea desvincular el Sistema de la cuenta de Telegram, presione el siguiente boton: <button type="button" id="buttonCancelarTelegram" name="buttonCancelarTelegram" tabIndex="-1" class="btn btn-default btn-cancel"><span class="far fa-bell-slash"></span> Desvincular</button></p>
							  <small>(puede volver a asociar el Sistema con la aplicacion Telgram de su celular cuando lo quiera)</small>
							</div>
						</div>
			         </c:otherwise>
			    </c:choose>			    
			</div>
		  </div>
	   </div>
	</div>
</div>