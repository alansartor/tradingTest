<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">  
  <label class="col-xs-12 col-sm-5 col-md-5 control-label" for="${name}"><spring:message code="form.${base}.${name}.label"/><c:choose><c:when test="${obligatorio==true}"><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span></c:when><c:otherwise> <small style="color: grey;">(opcional)</small></c:otherwise></c:choose></label>  
  <div class="col-xs-12 col-sm-3 col-md-3">
  	<div class="input-group date" id="dtp${name}">
  		<spring:message code="form.${base}.${name}.placeholder" var="varPlaceholder"/>
  		<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  		<form:input path="${name}" placeholder="${varPlaceholder}" class="form-control" required="${obligatorio==true?'required':''}" readonly="${readonly}"
  			data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}"/>
	  	<%-- <span class="help-block">
	  	<a title="" data-original-title="" href="#" data-toggle="tooltip" data-placement="left" data-title=<spring:message code="form.${base}.${name}.helpBlock"/>>
	  		<span class="glyphicon glyphicon-info-sign"></span>
	  	</a></span> --%>
		<span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>	
  	</div>
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca --><!-- Si lo pongo dentro del div de input-group, pierdo el foco cuando hago switch entre ventanas -->
  </div>
</div>