<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/jquery-plugins/bonsai/css/jquery.bonsai.css">
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/bonsai/js/jquery.bonsai.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/bonsai/js/jquery.qubit.js"></script>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		$jQ("#my-form-buttons-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		$jQ("#my-form-title-div-id").removeClass().addClass("col-md-6 col-xs-12");//acomodo los botones para que queden mejor
		$jQ("#checkboxes").bonsai({
		      expandAll: true,
		      checkboxes: true, // depends on jquery.qubit plugin
		      handleDuplicateCheckboxes: false // optional, antes estaba en true, pero creo que era eso lo que hacia que falle y se enloquesca el plugin lo que hacia que entre en un bucle indefinido cuando destildaba un par de checkbox
		    });
  	});
</script>
<div class="body">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
    <tiles:importAttribute name="accion"/>
	<tiles:importAttribute name="labelAccion"/>
	
	<form:hidden path="id"/>
	<div class="form-group form-group-sm">
		<div class=" col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<div class="panel panel-default my-panel">
			  <div class="panel-body my-panel-body">
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="codigo" base="${modulo}.${clase}.${labelAccion}" obligatorio="true"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="descripcion" base="${modulo}.${clase}.${labelAccion}" obligatorio="true"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">	
						<c:choose>
							<c:when test="${accion == 'EDITAR'}">
								<html:inputDisabled name="tipo" base="${modulo}.${clase}.${labelAccion}" labelSize="5" inputSize="4"/>
							</c:when>
							<c:otherwise>
								<html:selectPrimitiveField name="tipo" base="${modulo}.${clase}.${labelAccion}" items="${tipos}" obligatorio="true" labelSize="5" inputSize="4"/>
							</c:otherwise>
						</c:choose>
			  		</div>
				</div>
				<%-- <form:checkboxes items="${privilegiosDTOs}" path="privilegios"/> --%>
				<ol id='checkboxes'>
					<li class='expanded'><input type='checkbox' value='root' /> All
			          <ol>
				      	<c:forEach items="${privilegiosDTOs}" var="modulo">
					      	<li>
				              <input type='checkbox' value='${modulo.value.name}'/> ${modulo.value.iconSpan}<spring:message code="modulo.${modulo.value.name}"/>
				              <ol>
					              <c:forEach items="${modulo.value.submenu}" var="clase">
						            <li>
						            	<input type='checkbox' value='${clase.value.name}' /> ${clase.value.iconSpan}<spring:message code="menu.${modulo.value.name}.${clase.value.name}" htmlEscape="false"/><span class="label label-info"><spring:message code="menu.${modulo.value.name}.${clase.value.name}.help" text=""/></span>
						            	<ol>
							            	<c:forEach items="${clase.value.submenu}" var="action">
							            		<!-- las clase, en vez de usar la variable clase, uso la clase de la accion, ya que hay algunos privilegios (eventos) que se muestran todos juntos bajo una misma clase, pero los textos estan registradoas cada cual con su clase -->
							            		<li><input type="checkbox" id="privilegios${action.value.privilegio.ordinal}.checked" name="privilegios[${action.value.privilegio.ordinal}].checked" ${entidadDTO.privilegios[action.value.privilegio.ordinal].checked?'checked':''}/> ${action.value.iconSpan}<spring:message code="menu.${modulo.value.name}.${action.value.privilegio.claseMenu}${!empty action.value.privilegio.subClase?'.':''}${action.value.privilegio.subClase}.${action.value.privilegio.accion}" arguments="," htmlEscape="false"/>
							            		<span class="label label-info"><spring:message code="menu.${modulo.value.name}.${action.value.privilegio.claseMenu}${!empty action.value.privilegio.subClase?'.':''}${action.value.privilegio.subClase}.${action.value.privilegio.accion}.help" text=""/></span></li>
							            		<input type="hidden" id="privilegios${action.value.privilegio.ordinal}.privilegio" name="privilegios[${action.value.privilegio.ordinal}].privilegio" value="${action.value.privilegio}"/>
											</c:forEach>
						                </ol>
						            </li>
						          </c:forEach>
				              </ol>
				            </li>
				      	</c:forEach>
			      		</ol>
			        </li>
				</ol>
			  </div>
			</div>
		</div>	
	</div>	
</div>