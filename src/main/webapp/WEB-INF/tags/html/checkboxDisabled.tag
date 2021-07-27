<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%-- <%@ attribute name="readonly" required="false" rtexprvalue="true" %> --%>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">
  <label class="col-xs-12 col-sm-5 col-md-5 control-label" for="${name}"><spring:message code="form.${base}.${name}.label"/></label>
  <div class="col-xs-12 col-sm-7 col-md-7">
  	<spring:message code="form.${base}.${name}.helpBlock" var="varHelpBlock"/>
  	<!-- Se especifica el id del checkbox porque sino spring le agrega un 1 al id y si necesitamos referenciarlo en algun lado no podemos -->
	<form:hidden id="${name}" path="${name}"/>
    <input id="${name}Disabled" type="checkbox" disabled/>
    <small class="text-muted">${varHelpBlock}</small>
    <div class="help-block with-errors"></div><!-- validator.js muestrar el error aca -->
  	<div class="text-danger"><form:errors path="${name}"></form:errors></div>
  </div>
</div>
<!-- No pude hacer andar el cambio automatico si se esta usando bootstrapToggle, igual no creo que se quiera modificar un checkbox readonly con javascript -->
<!-- <script type="text/javascript">/* TODO ESTO ES NECESARIO PARA EVITAR TENER ID DEL HIDDEN Y EL DISABLED REPETIDO, Y PARA QUE CUANDO CAMBIA EL VALOR DEL HIDDEN, SE REFLEJE EL VALOR EN EL DISABLED */
	//copio el valor inicial del hiden en el disabled
	document.getElementById("${name}Disabled").checked = document.getElementById("${name}").value;
	//configuro para que si se modifica por javascript el valor del hidden, este se refleje en el disabled
	Object.defineProperty(document.getElementById("${name}"), "value", {
	    set:  function (t) {
	    	document.getElementById("${name}Disabled").checked = t;
	        return this.defaultValue = t;
	    }
	});
</script> -->