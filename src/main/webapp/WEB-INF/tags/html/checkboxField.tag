<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="ayuda" required="false" rtexprvalue="true" type="ar.com.signals.trading.ayuda.suport.AyudaEnum" %>
<%-- <%@ attribute name="readonly" required="false" rtexprvalue="true" %> --%>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">
  <label class="col-xs-12 col-sm-5 col-md-5 control-label" for="${name}"><c:if test="${!empty ayuda}"><button type="button" tabindex="-1" class="btn btn-info custom-circle-button" onclick="customComponents_mostrar_ayuda('${ayuda}')"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span></button> </c:if><spring:message code="form.${base}.${name}.label"/></label>
  <div class="col-xs-12 col-sm-7 col-md-7">
  	<!-- Se especifica el id del checkbox porque sino spring le agrega un 1 al id y si necesitamos referenciarlo en algun lado no podemos -->
   	<!-- Se reemplaza readonly por disabled, pero esto requiere un hidden para hacer el bind -->
   	<%-- <c:if test="${readonly}"><form:hidden id="${name}" path="${name}"/></c:if> --%>
	<!-- No usar disable! Ya que sino se duplica el id y no se puede usar bootstrapToggle, entonces se usa el disable en el contructor del bootstrapToggle! -->
	<!-- Solucion negrada temporal al checkbox readonly: cuando es readonly directamente no mostrarlo -->
    <form:checkbox id="${name}" path="${name}"/>
    <small class="text-muted"><spring:message code="form.${base}.${name}.helpBlock"/></small>
    <div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  </div>
</div>