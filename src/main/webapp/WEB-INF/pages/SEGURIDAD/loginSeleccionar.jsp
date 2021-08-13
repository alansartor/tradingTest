<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<style>
body {
  padding-top: 40px;
  background: url("${pageContext.servletContext.contextPath}/public/custom/images/loginBackground.jpg") no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.form-group {
    margin-bottom: 15px;
}
@media only screen and (max-width: 480px) {
	.center-block{
		width:95%;
	}
}
@media only screen and (min-width: 480px) and (max-width: 768px) {
	.center-block{
		width:460px;
	}	
}
@media only screen and (min-width: 768px) and (max-width: 992px) {
	.center-block{
		width:500px;
	}
}
@media only screen and (min-width: 992px) {
	.center-block{
		width:500px;
	}
}
</style>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
 	var onSubmit = false;
 	$jQ().ready(function () {
 		$jQ(".list-group-item").click(function( event ) {
 			if(onSubmit){
 				return false;
 			}
 			onSubmit = true;
 			$jQ('.list-group-item').addClass('disabled');//para deshabilitar las opciones
 			
 			//this is (or event.currentTarget, see below) always refers to the DOM element the listener was attached to
 			$jQ(this).find("h4").prepend('<span class="fas fa-spinner custom-icon-spin" aria-hidden="true"></span>');
 			
 			var variableId = $jQ('#variableId').val();
 			var id = $jQ(this).data('id');
 			$jQ('#'+variableId).val(id);
 			$jQ('#entidadDTO').submit();
 		});
	});
</script>
<div class="body">
 	<tiles:importAttribute name="modulo"/>
	<tiles:importAttribute name="clase"/>
	<tiles:importAttribute name="accion"/>
 	<tiles:importAttribute name="variable"/>
	<div class="center-block">
		<form:form  modelAttribute="entidadDTO" name="entidadDTO" class="form-horizontal">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<div class="panel panel-default">
		  <div class="panel-heading"><span class="glyphicon glyphicon-hand-down"></span> <spring:message code="menu.${modulo}.${clase}.${accion}" arguments="${subClase}," htmlEscape="false"/></div>
		  <div class="panel-body" style="padding: 0px;">
			 <div class="list-group">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="padding: 0px;">
					<input type="hidden" id="variableId" value="${variable}"/>
					<form:hidden path="${variable}"/>
					<form:hidden path="user_target_url"/>
					<c:forEach items="${entidades}" var="enti">		
						  <a href="#" class="list-group-item col-lg-12 col-md-12 col-sm-12 col-xs-12" data-id="${enti.id}" style="padding-left: 10px;padding-right: 10px;border:none;border-top: 1px solid #ddd;">
						    <h4 class="list-group-item-heading">${enti.descripcion1}</h4>
						    <p class="list-group-item-text">${enti.descripcion2}</p>
						  </a>
					</c:forEach>
				</div>
			 </div>				  
		  </div>
		</div>
		</form:form>	
	</div>	   	
</div>