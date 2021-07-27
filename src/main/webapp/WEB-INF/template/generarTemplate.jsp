<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="accion"/>
<h3><spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments="," htmlEscape="false"/></h3>
<form:form modelAttribute="entidadDTO" name="entidadDTO" class="form-horizontal">	
<div class="panel panel-default">
  <div class="panel-body"><tiles:insertAttribute name="body" /></div>
  <div class="panel-footer">
    <!-- Button (Double) -->
 	<div class="form-group">
	  <div class="col-md-6 col-xs-6">
	     <button type="button" id="buttonCancelar" name="buttonCancelar" class="btn btn-default"><span class="glyphicon glyphicon glyphicon-remove" style="color:red"></span> Cancelar</button>
	  </div>	
	  <div class="col-md-6 col-xs-6">
	    <button type="submit" id="buttonGenerar" name="buttonGenerar" class="btn btn-success pull-right"><span class="glyphicon glyphicon-tasks"></span> Generar</button>
	  </div>
	</div>
  </div>
</div>
</form:form>
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/validator/validator.min.js"></script>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
 	$jQ(function () {
 		$jQ('[data-toggle="tooltip"]').tooltip({trigger: 'focus'});
 		$jQ("#entidadDTO").validator().on('submit', function (e) {
 			if (e.isDefaultPrevented()) {
				// handle the invalid form...
			} else {
				// everything looks good!
				waitingDialog.show('Aguarde', {dialogSize: 'sm'});
				$jQ("#buttonGenerar").attr('disabled', true);
			}
 		});
 		$jQ.fn.validator.Constructor.FOCUS_OFFSET=100;
 		$jQ("#buttonCancelar").on("click", function (e) {
 			window.location.href = $jQ("#applicationContextPath").val() + "/web/GENERAL/HOME?cancel=true";
 	    });
	});
</script>