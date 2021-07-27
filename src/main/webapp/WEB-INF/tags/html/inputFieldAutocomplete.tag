<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<%@ attribute name="tipo" required="false" rtexprvalue="true" %>
<%@ attribute name="patron" required="false" rtexprvalue="true" %>
<%@ attribute name="clase" required="false" rtexprvalue="true" %>
<%@ attribute name="labelSize" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="inputSize" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="ayuda" required="false" rtexprvalue="true" type="ar.com.signals.trading.ayuda.suport.AyudaEnum" %>
<%@ attribute name="hidden_id" required="true" rtexprvalue="true" %>
<%@ attribute name="button_nuevo_id" required="false" rtexprvalue="true" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">
  <label class="col-xs-12 col-sm-${empty labelSize?5:labelSize} col-md-${empty labelSize?5:labelSize} col-lg-${empty labelSize?5:labelSize} control-label" for="${name}"><c:if test="${!empty ayuda}"><button type="button" tabindex="-1" class="btn btn-info custom-circle-button" onclick="customComponents_mostrar_ayuda('${ayuda}')"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span></button> </c:if><spring:message code="form.${base}.${name}.label"/><c:choose><c:when test="${obligatorio==true}"><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span></c:when><c:otherwise> <small style="color: grey;">(opcional)</small></c:otherwise></c:choose></label>  
  <div class="col-xs-12 col-sm-${empty inputSize?7:inputSize} col-md-${empty inputSize?7:inputSize} col-lg-${empty inputSize?7:inputSize}">
  	<spring:message code="form.${base}.${name}.placeholder" var="varPlaceholder"/>
  	<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  	<form:hidden path="${hidden_id}"/><!-- debido a que el autocomplete hace referencia al id de esta forma '$jQ(this).prev()' entonces tiene que estar pegada al input -->
  	<form:input path="${name}" placeholder="${varPlaceholder}" class="form-control ${clase}" required="${obligatorio==true?'required':''}" readonly="${readonly}"
  		data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}" type="${tipo}" pattern="${patron}" autocomplete="off" data-button_nuevo_id="${button_nuevo_id}"/><!-- para que el browser no autocomplete! -->
  	<%-- <span class="help-block">
  	<a title="" data-original-title="" href="#" data-toggle="tooltip" data-placement="left" data-title=<spring:message code="form.${base}.${name}.helpBlock"/>>
  		<span class="glyphicon glyphicon-info-sign"></span>
  	</a></span> --%>
  </div>
  <div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  <div class="text-danger"><form:errors path="${name}"></form:errors></div>
</div>