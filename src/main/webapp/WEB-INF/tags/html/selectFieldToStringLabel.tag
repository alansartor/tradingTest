<!-- Custom Tag para Spring form:select
	Los items se deben pasar como java.util.List
Nota 1: no se puede usar PlaceHolder ni tooltip
Nota 2: si se quiere pasar las opciones del select mediante <jsp:doBody/> no se puede porque tienen tags de spring, solo se puede pasar texto basico o tags normales creo -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List" %>
<%@ attribute name="itemValue" required="true" rtexprvalue="true" %>
<%@ attribute name="labelSize" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="inputSize" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">
  <label class="col-xs-12 col-sm-${empty labelSize?5:labelSize} col-md-${empty labelSize?5:labelSize} col-lg-${empty labelSize?5:labelSize} control-label" for="${name}"><spring:message code="form.${base}.${name}.label"/><c:choose><c:when test="${obligatorio==true}"><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span></c:when><c:otherwise> <small style="color: grey;">(opcional)</small></c:otherwise></c:choose></label>  
  <div class="col-xs-12 col-sm-${empty inputSize?7:inputSize} col-md-${empty inputSize?7:inputSize} col-lg-${empty inputSize?7:inputSize}">
   	<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
   	<!-- Se reemplaza readonly por disabled, pero esto requiere un hidden para hacer el bind -->
   	<c:if test="${readonly}"><form:hidden path="${name}"/></c:if>
  	<form:select path="${name}" data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}" class="form-control" required="${obligatorio==true?'required':''}" disabled="${readonly}" onkeypress="if(teclaEnterPresionada(event)){$jQ('#entidadDTO').trigger('submit');}">
		<form:option value="" label = "Seleccionar"></form:option> 
		<form:options items="${items}" itemValue="${itemValue}"/>
	</form:select>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  </div>
</div> 