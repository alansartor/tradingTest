<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="disabled" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<%@ attribute name="tipo" required="false" rtexprvalue="true" %>
<%@ attribute name="incremento" required="false" rtexprvalue="true" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">  
  <div class="col-md-12">
  	<div class="input-group">
  		<span class="input-group-addon" id="basic-addon1"><spring:message code="form.${base}.${name}.label"/></span>
  		<spring:message code="form.${base}.${name}.placeholder" var="varPlaceholder"/>
  		<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  		<c:if test="${disabled}"><form:hidden id="${name}" path="${name}"/></c:if>
  		<form:input path="${name}" placeholder="${varPlaceholder}" class="form-control" required="${obligatorio==true?'required':''}" readonly="${readonly}" disabled="${disabled}"
  			data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}" type="${tipo}" step="${incremento}" autocomplete="off"/><!-- para que el browser no autocomplete! -->
  	<%-- <span class="help-block">
  	<a title="" data-original-title="" href="#" data-toggle="tooltip" data-placement="left" data-title=<spring:message code="form.${base}.${name}.helpBlock"/>>
  		<span class="glyphicon glyphicon-info-sign"></span>
  	</a></span> --%>	
  	</div>
  </div>
  <div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  <div class="text-danger"><form:errors path="${name}" htmlEscape="false"></form:errors></div> 
</div>