<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-select/css/bootstrap-select.min.css">
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-select/js/bootstrap-select.min.js"></script>
<script type="text/javascript">	
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		$jQ("#sucursales").selectpicker();
		//lo inicializo con los valores seleccionados
	 	var arraySucursales = $jQ('#sucursalesValues').val();
	 	if(arraySucursales && arraySucursales != "[]"){//el bind de spring no funciona con bootstrap-select, entonces lo hago a mano
	 		$jQ("#sucursales").selectpicker('val',  JSON.parse(arraySucursales));//hay que convertirlo a array!
	 		//$jQ('#producciones').selectpicker('val', ['13','32']);
	 	}
	 	$jQ("#eventos").selectpicker();
		//lo inicializo con los valores seleccionados
	 	var arrayEventos = $jQ('#eventosValues').val();
	 	if(arrayEventos && arrayEventos != "[]"){//el bind de spring no funciona con bootstrap-select, entonces lo hago a mano
	 		$jQ("#eventos").selectpicker('val',  JSON.parse(arrayEventos));//hay que convertirlo a array!
	 		//$jQ('#producciones').selectpicker('val', ['13','32']);
	 	}
  	});
</script>
<div class="body">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
    <tiles:importAttribute name="accion"/>
	<tiles:importAttribute name="labelAccion"/>
	<input id="accionValue" type="hidden" value="${accion}"/>
	
	<input id="sucursalesValues" type="hidden" value="${entidadDTO.sucursales}"/>
	<input id="eventosValues" type="hidden" value="${entidadDTO.eventos}"/>
	<form:hidden path="id"/>
  	<!-- Select Multiple -->
	<div class="form-group form-group-sm">
	  <div class="col-lg-6 col-md-8 col-sm-12 col-xs-12">
		  <label class="col-xs-12 col-sm-5 col-md-5 col-lg-5 control-label" for="sucursales"><spring:message code="form.${modulo}.${clase}.${labelAccion}.sucursales.label"/></label>
		  <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">		 	
		  	<form:select path="sucursales" class="form-control" multiple="multiple">
		  	    <c:forEach items="${sucursalesDTO}" var="suc">
			    	<option value="${suc.id}">${suc.codigo}</option>	
			    </c:forEach>
    		</form:select>
		  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
		  	<div class="text-danger"><form:errors path="sucursales"></form:errors></div>
		  </div>
	  </div>
	</div>
	<!-- Select Multiple -->
	<div class="form-group form-group-sm">
	  <div class="col-lg-6 col-md-8 col-sm-12 col-xs-12">
		  <label class="col-xs-12 col-sm-5 col-md-5 col-lg-5 control-label" for="eventos"><spring:message code="form.${modulo}.${clase}.${labelAccion}.eventos.label"/></label>
		  <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">		 	
		  	<form:select path="eventos" class="form-control" multiple="multiple">
		  	    <c:forEach items="${tiposEventosDTO}" var="eve">
			    	<option value="${eve}"><spring:message code="menu.EVENTOS.${eve.claseMenu}.INFORMAR"/></option>	
			    </c:forEach>
    		</form:select>
		  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
		  	<div class="text-danger"><form:errors path="eventos"></form:errors></div>
		  </div>
	  </div>
	</div>	
</div>