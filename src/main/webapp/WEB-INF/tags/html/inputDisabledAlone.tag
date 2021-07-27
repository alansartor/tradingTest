<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<%@ attribute name="tipo" required="false" rtexprvalue="true" %>
<%@ attribute name="patron" required="false" rtexprvalue="true" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">
  <div class="col-md-12">
  	<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  	<form:hidden path="${name}"/>
  	<input id="${name}Disabled" class="form-control" disabled
  		data-toggle="tooltip" data-placement="top" data-title="${varHelpBlock}"/>
  	<%-- <span class="help-block">
  	<a title="" data-original-title="" href="#" data-toggle="tooltip" data-placement="left" data-title=<spring:message code="form.${base}.${name}.helpBlock"/>>
  		<span class="glyphicon glyphicon-info-sign"></span>
  	</a></span> --%>
  	<div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  </div>
</div>
<script type="text/javascript">/* TODO ESTO ES NECESARIO PARA EVITAR TENER ID DEL HIDDEN Y EL DISABLED REPETIDO, Y PARA QUE CUANDO CAMBIA EL VALOR DEL HIDDEN, SE REFLEJE EL VALOR EN EL DISABLED */
	//copio el valor inicial del hiden en el disabled
	document.getElementById("${name}Disabled").defaultValue = document.getElementById("${name}").value;
	//configuro para que si se modifica por javascript el valor del hidden, este se refleje en el disabled
	Object.defineProperty(document.getElementById("${name}"), "value", {
	    set:  function (t) {
	    	document.getElementById("${name}Disabled").value = t;
	        return this.defaultValue = t;
	    }
	});
</script>