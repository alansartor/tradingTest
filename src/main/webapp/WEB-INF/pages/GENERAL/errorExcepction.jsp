<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		$jQ("#buttonEnviarReporte").on("click", function (e) {
			informarDetalleError();
		});
	});
</script>
<div class="body">
	<h1>Error</h1>
	<div class="alert alert-danger" role="alert">${exception.message}</div>		
	<br/>
 	<div class="form-group">
	  <div class="col-xs-12 col-xs-8 col-md-4 col-lg-4">
		  <div class="pull-left">
		  	   <button type="button" id="buttonVerDetalle" name="buttonVerDetalle" class="btn btn-default" onclick="document.getElementById('errorDiv').style.display = '';"><span class="glyphicon glyphicon-list"></span> Ver Detalle Error</button> 
		  	   <button type="button" id="buttonEnviarReporte" name="buttonEnviarReporte" class="btn btn-primary"><span class="glyphicon glyphicon-envelope"></span> Enviar Reporte</button>
		  </div>
	   </div>
	</div>	
	<br/>
	<form id="failForm" action="${pageContext.servletContext.contextPath}/web/GENERAL/REPORTAR_ERROR" method="post" name="failForm">
		<input id="error" name="error" type="hidden" value="${exception.message}"/>
		<input id="stackTrace" name="stackTrace" type="hidden" value="${fn:escapeXml(stackTrace)}"/></input>
	</form>
	<div class="panel panel-default" id="errorDiv" style="display: none; margin-top: 20px;">
	  <div class="panel-body">${fn:escapeXml(stackTrace)}</div>
	</div>	
</div>
<script type="text/javascript">
	function informarDetalleError(){
		waitingDialog.show('Aguarde', {dialogSize: 'sm'});
		document.forms['failForm'].submit();
	}
</script>