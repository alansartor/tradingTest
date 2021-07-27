<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<script src="${pageContext.servletContext.contextPath}/public/jquery-ui/jquery-ui.min.js"></script>
<div class="form-group form-group-sm">
	<div class="col-lg-6 col-md-8 col-sm-12 col-xs-12">
		<label class="col-xs-12 col-sm-5 col-md-5 col-lg-5 control-label" for="sujeto_descripcion">Titular</label>  
		<div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
			<input type="hidden" id="fltSujetoId" value="${sujeto_id}"/>
			<input type="search" id="sujeto_descripcion" value="${sujeto_descripcion}" class="form-control input-md" ${empty sujeto_id?'':'readonly'}/>
  		</div>
	</div>
</div>
<c:if test="${!empty sujeto_id}">
	<div class="form-group form-group-sm">
		<div class="col-lg-6 col-md-8 col-sm-12 col-xs-12">	
			<label class="col-xs-12 col-sm-5 col-md-5 col-lg-5 control-label" for="fltSujetoDescripcion">Campo</label>  
			<div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
				<select id="fltSucursalId" class="form-control selectpicker" onkeypress="if(teclaEnterPresionada(event)){$jQ('#buttonBuscar').trigger('click');}">
			   		<option value="">-Todos-</option>
					<c:forEach items="${sucursalesDTO}" var="sucu">
						<option value="${sucu.id}">${sucu.codigo}</option>
					</c:forEach>
				</select>
	  		</div>
		</div>
	</div>
</c:if>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () { 		
 		$jQ("#sujeto_descripcion").autocomplete({
	        minLength: 3,
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
	        source: $jQ("#applicationContextPath").val() + "/web/support/REST/autocompletar/sujeto",
	        select: function( event, ui ) {
	        	$jQ("#fltSujetoId").val( ui.item.id );
	        	$jQ(event.target).val( ui.item.value );
	        },
	        response: function(event, ui) {
	            // ui.content is the array that's about to be sent to the response callback.
	            if (ui.content.length === 0) {
	            	$jQ("#fltSujetoId").val("");//cuando no hay coincidencia, blanqueo el id, el valor se blanquea cuando sale del input
	            	//$jQ(event.target).val("")	            	
	            }
	        },
	        change: function( event, ui ) {
	        	if (!ui.item) {//si no hay seleccion, blanqueo las variables    		
	            	$jQ("#fltSujetoId").val("");
	            	$jQ(event.target).val("");
	            }
	        }
	      });
  	});	
</script>