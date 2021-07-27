<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="nameLabel" required="true" rtexprvalue="true" %>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="name2" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="tipo" required="false" rtexprvalue="true" %>
<%@ attribute name="patron" required="false" rtexprvalue="true" %>
<div class="form-group">
  <label class="col-md-3 control-label" for="${nameLabel}"><spring:message code="form.${base}.${nameLabel}.label"/></label>
  <div class="col-md-3">
  	<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  	<form:hidden path="${name}"/>
  	<form:input path="${name}" class="form-control" disabled="true" data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}"/>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  </div>
  <div class="col-md-6">
  	<spring:message code="form.${base}.${name2}.placeholder" var="varPlaceholder2"/>
  	<spring:message code="form.${base}.${name2}.helpBlock" var="varHelpBlock2"/>
  	<form:hidden path="${name2}"/>
  	<form:input path="${name2}" class="form-control" disabled="true" data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock2}"/>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name2}"></form:errors></div>
  </div>
</div>