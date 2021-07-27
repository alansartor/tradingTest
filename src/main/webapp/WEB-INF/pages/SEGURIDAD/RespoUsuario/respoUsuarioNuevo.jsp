<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/css/bootstrap-toggle.min.css">
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/jquery-ui/jquery-ui.min.css">
<style>
/* para limitar el tamaño del autocompletar con un scrollbar */
.ui-autocomplete {
    max-height: 400px;
    overflow-y: auto;
    /* prevent horizontal scrollbar */
    overflow-x: hidden;
    /* para que el width del autocompeltar quede bien, se le agrega en el open 20 px al width calculado por el tamaño del scroll */
    max-width: 550px;/*pongo un limite, para que no haya scrol horizontal del body*/
}
</style>
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/js/bootstrap-toggle.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		$jQ("#my-form-buttons-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		$jQ("#my-form-title-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		if($jQ("#accionValue").val() == "EDITAR"){
			$jQ('#activo').bootstrapToggle();
		}else{		
			$jQ('#activo').bootstrapToggle();
			$jQ('#activo').parent().on("click", function(e) {//el toogle agrega un div y esconde el input, entonces miro el clik en el div!
				e.stopImmediatePropagation();//cancela la operacion, hago de esta forma porque no se puede poner readolnly el toogle y si uso el disable no se manda en el post! No usar: $jQ('#activo').bootstrapToggle('disable');
			}).css( "opacity", ".65" );
		}
	    $jQ("#usuario_username").autocomplete({
	        minLength: 2,
	        autoFocus: true,
	        open: function(event, ui) { 
	        	//como le agregamos scroll vertical, incrementamos el width en 20 (tamaño del scroll) para que se muestren en una linea los items
	        	//busco el ui-menu visible! deberia haber uno solo
	        	var autocompleteMenu = $jQ('ul.ui-menu[style*="display: block"]:first');//no se bien como funciona el first, pero llamar una sola vez y hacer referencia a la variable
	        	autocompleteMenu.width(autocompleteMenu.width() + 20);
	        	//si el autocomplete no es visible entonces hacemos scroll para que quede visible
	        	var windowsHeight = $jQ(window).height();//es el alto del viewport, no tiene en cuenta en scrollTop
	        	var windowsScrollTop = $jQ(window).scrollTop();
	        	var inputTop = autocompleteMenu.offset().top - windowsScrollTop - 30;//este es el top con repecto al viewport! (el 30 es del input, asi se ve!)
	        	var autocompleteMenuBottom = inputTop + autocompleteMenu.height() + 30;//inputTop: es la coordenada superior del menu con respecto al viewport, no tiene en cuenta el scrollTop
	        	if(autocompleteMenuBottom > windowsHeight){
	        		var scrollToAdd = autocompleteMenuBottom - windowsHeight;
	        		if(scrollToAdd > inputTop){//si el scroll que voy a hacer es menor que la coordenada superior del input, entonces hago el scroll, caso contrario solo hago scrool hasta que el input quede bien arriba
	        			scrollToAdd = inputTop;
	        		}
	        		$jQ('html, body').animate({
		        		scrollTop: windowsScrollTop + scrollToAdd
	                }, 1200);
	        	}
	        },
	        source: $jQ("#applicationContextPath").val() + "/web/support/REST/autocompletar/usuarioAsignable",
	        select: function( event, ui ) {
	        	$jQ("#usuario_id").val( ui.item.id );
	        	$jQ(event.target).val( ui.item.value )
	        },
	        response: function(event, ui) {
	            // ui.content is the array that's about to be sent to the response callback.
	            if (ui.content.length === 0) {
	            	$jQ("#usuario_id").val("");//cuando no hay coincidencia, blanqueo el id, el valor se blanquea cuando sale del input
	            }
	        },
	        change: function( event, ui ) {
	        	if (!ui.item) {//si no hay seleccion, blanqueo las variables    		
	        		$jQ("#usuario_id").val("");
	            	//$jQ(event.target).val("")//en este caso no se blanquea variable, porque el usuario puede hacer referencia a usuarios que no aparecen en el autocomplete
	            }
	        }
		});		
  	});
</script>
<div class="body">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
	<tiles:importAttribute name="labelAccion"/>
	<tiles:importAttribute name="accion"/>
	
	<input id="accionValue" type="hidden" value="${accion}"/>
	
	<form:hidden path="id"/>
	<div class="form-group form-group-sm">
		<div class=" col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<div class="panel panel-default my-panel">
			  <div class="panel-body my-panel-body">
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<form:hidden path="usuario_id"/>
						<html:inputField name="usuario_username" base="${modulo}.${clase}.${labelAccion}" obligatorio="true" readonly="${accion == 'EDITAR'?true:false}"/>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:selectField name="respo_id" base="${modulo}.${clase}.${labelAccion}" items="${responsabilidadesDTO}" itemLabel="descripcionCompleta" itemValue="id" obligatorio="true"/>
			  		</div>
				</div>
				<c:if test="${empty sinSucursal or sinSucursal == false}">
					<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:selectField name="sucursal_id" base="${modulo}.${clase}.${labelAccion}" items="${sucursales}" itemLabel="codigo" itemValue="id" obligatorio="true"/>
			  		</div>
				</div>
				</c:if>
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