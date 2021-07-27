<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="name2" required="true" rtexprvalue="true" %>
<%@ attribute name="name3" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<%@ attribute name="tipo" required="false" rtexprvalue="true" %>
<%@ attribute name="patron" required="false" rtexprvalue="true" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">
  <label class="col-md-3 control-label" for="${name}"><spring:message code="form.${base}.${name}.label"/></label>
  <div class="col-md-2">
  	<spring:message code="form.${base}.${name}.placeholder" var="varPlaceholder"/>
  	<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  	<form:hidden path="${name}"/>
  	<form:input path="${name}" class="form-control" disabled="true" data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}"/>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  </div>
  <div class="col-md-3">
  	<spring:message code="form.${base}.${name2}.placeholder" var="varPlaceholder2"/>
  	<spring:message code="form.${base}.${name2}.helpBlock" var="varHelpBlock2"/>
  	<form:hidden path="${name2}"/>
  	<form:input path="${name2}" class="form-control" disabled="true" data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock2}"/>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name2}"></form:errors></div>
  </div>
  <div class="col-md-4">
  	<spring:message code="form.${base}.${name3}.placeholder" var="varPlaceholder3"/>
  	<spring:message code="form.${base}.${name3}.helpBlock" var="varHelpBlock3"/>
  	<form:hidden path="${name3}"/>
  	<form:input path="${name3}" class="form-control" disabled="true" data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock3}"/>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name3}"></form:errors></div>
  </div>
</div>