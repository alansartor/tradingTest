<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">  
  <div class="col-md-12">
  	<div class="input-group">
  		<span class="input-group-addon" id="basic-addon1"><spring:message code="form.${base}.${name}.label"/></span>
  		<spring:message code="form.${base}.${name}.placeholder" var="varPlaceholder"/>
  		<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
		<!-- con el readonly y el tabindex obtenemos la apariencia del disabled sin los inconvenientes de id duplicados por tener que tener un form:hidden para no perder el bind -->
  		<form:input path="${name}" placeholder="${varPlaceholder}" class="form-control" readonly="true" tabindex="-1"
  			data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}"/>
  	<%-- <span class="help-block">
  	<a title="" data-original-title="" href="#" data-toggle="tooltip" data-placement="left" data-title=<spring:message code="form.${base}.${name}.helpBlock"/>>
  		<span class="glyphicon glyphicon-info-sign"></span>
  	</a></span> --%>	
  	</div>
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca --><!-- Si lo pongo dentro del div de input-group, pierdo el foco cuando hago switch entre ventanas -->
  </div>
</div>