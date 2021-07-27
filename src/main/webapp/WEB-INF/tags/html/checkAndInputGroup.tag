<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="nameCheckbox" required="true" rtexprvalue="true" %>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<%@ attribute name="tipo" required="false" rtexprvalue="true" %>
<%@ attribute name="incremento" required="false" rtexprvalue="true" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">  
  <label class="col-xs-12 col-sm-5 col-md-5 control-label" for="${name}"><spring:message code="form.${base}.${name}.label"/><c:choose><c:when test="${obligatorio==true}"><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span></c:when><c:otherwise> <small style="color: grey;">(opcional)</small></c:otherwise></c:choose></label>  
  <div class="col-xs-12 col-sm-7 col-md-7">
  	<div class="input-group">
  		<!-- Se especifica el id del checkbox porque sino spring le agrega un 1 al id y si necesitamos referenciarlo en algun lado no podemos -->
  	    <!-- Se reemplaza readonly por disabled, pero esto requiere un hidden para hacer el bind -->
   		<c:if test="${readonly}"><form:hidden id="${nameCheckbox}" path="${nameCheckbox}"/></c:if>
        <span class="input-group-addon"><form:checkbox id="${nameCheckbox}" path="${nameCheckbox}" disabled="${readonly}" onchange="if (${nameCheckbox}.checked == false) {document.getElementById('${name}').disabled = true;} else {document.getElementById('${name}').disabled = false;}"/></span>
  		<spring:message code="form.${base}.${name}.placeholder" var="varPlaceholder"/>
  		<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  		<!-- Si se deshabilita el input, entonces en el dto va vacio, no importa si tenia algun valor, por defecto lo inicializo deshabilitado -->
  		<form:input path="${name}" placeholder="${varPlaceholder}" class="form-control" required="${obligatorio==true?'required':''}" readonly="${readonly}" disabled="true"
  			data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}" type="${tipo}" step="${incremento}"/>
	<div class="text-danger"><form:errors path="${name}"></form:errors></div>	
  	</div>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca --><!-- Si lo pongo dentro del div de input-group, pierdo el foco cuando hago switch entre ventanas -->
  </div>
</div>