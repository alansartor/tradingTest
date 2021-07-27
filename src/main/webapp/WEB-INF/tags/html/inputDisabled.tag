<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="base" required="true" rtexprvalue="true" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true" %>
<%@ attribute name="obligatorio" required="false" rtexprvalue="true" %>
<%@ attribute name="tipo" required="false" rtexprvalue="true" %>
<%@ attribute name="patron" required="false" rtexprvalue="true" %>
<%@ attribute name="labelSize" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="inputSize" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="ayuda" required="false" rtexprvalue="true" type="ar.com.signals.trading.ayuda.suport.AyudaEnum" %>
<div class="form-group <spring:hasBindErrors name="entidadDTO"><c:set var="nameAtribute" value="${name}" /><c:if test="${errors.hasFieldErrors(nameAtribute)}">has-error</c:if></spring:hasBindErrors>">
  <label class="col-xs-12 col-sm-${empty labelSize?5:labelSize} col-md-${empty labelSize?5:labelSize} col-lg-${empty labelSize?5:labelSize} control-label" for="${name}"><c:if test="${!empty ayuda}"><button type="button" tabindex="-1" class="btn btn-info custom-circle-button" onclick="customComponents_mostrar_ayuda('${ayuda}')"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span></button> </c:if><spring:message code="form.${base}.${name}.label"/></label>  
  <div class="col-xs-12 col-sm-${empty inputSize?7:inputSize} col-md-${empty inputSize?7:inputSize} col-lg-${empty inputSize?7:inputSize}">
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
	//luego de mucho trabajo encontre esta forma que funciona para hacer todo. es decir para iniciar con el mismo valor, y para que si se cambia por javascrit, cambien los dos!
	document.getElementById("${name}Disabled").defaultValue = document.getElementById("${name}").defaultValue;//copio el valor inicial del hiden en el disabled
	Object.defineProperty(document.getElementById("${name}"), "value", {
		get: function() {return this.defaultValue;},
	    set:  function (t) {
	    	document.getElementById("${name}Disabled").defaultValue = t;//para que quede perfecto lo modifico los dos!
	    	document.getElementById("${name}Disabled").value = t;//configuro para que si se modifica por javascript el valor del hidden, este se refleje en el disabled
	        this.defaultValue = t;
	    }
	});	
	//esta otra forma tambien funciona, en vez de guardar el valor en this.defaultValue, la guardo en una variable dentro de la funcion!
	/* 	function createGetSetValue( inputElement,  inputElementDisabled){//lo tengo que meter en una funcion por el scope de la varieble var value (sin no lo hago en una funcion, entonces el scope es global)
		var value = inputElement.value;
		inputElementDisabled.defaultValue = value;//defaultValue copio el valor inicial del hiden en el disabled	
		Object.defineProperty(inputElement, "value", {
			get: function() { return value;},
		    set:  function (t) {
		    	inputElementDisabled.value = t;
		    	value = t;
		    }
		});
	}
	createGetSetValue(document.getElementById("${name}"), document.getElementById("${name}Disabled")); */
</script>