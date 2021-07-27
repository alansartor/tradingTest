<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%><!-- los custom tag estan en ml.jar, en teoria para hacerlos visibles habria que armar un archivo TLD, pero lo probe sin eso y sigue funcionando -->
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/moment.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<div class="bodyExtended">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
    <tiles:importAttribute name="accion"/>
	<tiles:importAttribute name="labelAccion"/>
	<div class="form-group form-group-sm">
		<div class=" col-lg-6 col-md-6 col-sm-12 col-xs-12">
			<div class="panel panel-default my-panel">
			  <div class="panel-body my-panel-body">
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="creation_date" base="${modulo}.${clase}.${labelAccion}" readonly="true" labelSize="5" inputSize="3"/>
			  		</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:inputField name="last_update_date" base="${modulo}.${clase}.${labelAccion}" readonly="true" labelSize="5" inputSize="3"/>
			  		</div>
				</div>
			  </div>
			</div>
		</div>	
	</div>	
</div>