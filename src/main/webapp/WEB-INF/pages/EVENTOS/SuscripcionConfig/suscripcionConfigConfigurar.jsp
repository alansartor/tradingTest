<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/css/bootstrap-toggle.min.css">
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-toggle/js/bootstrap-toggle.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/moment.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript">	
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		$jQ('#eventoTradingAlert').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#dtphoraInicio').datetimepicker({format: 'HH:mm'});
		$jQ('#dtphoraFin').datetimepicker({format: 'HH:mm'});
		$jQ('#eur_usd').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#usd_jpy').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#gbp_usd').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#eur_gbp').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#usd_chf').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#eur_jpy').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#eur_chf').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#usd_cad').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#aud_usd').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		
		
		$jQ('#gbp_jpy').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#aud_cad').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#aud_chf').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#aud_jpy').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#aud_nzd').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#cad_chf').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#cad_jpy').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#chf_jpy').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#eur_aud').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#eur_cad').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#eur_nok').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#eur_nzd').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#gbp_cad').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#gbp_chf').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#nzd_jpy').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#nzd_usd').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#usd_nok').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		$jQ('#usd_sek').bootstrapToggle({
		      on: 'SI',
		      off: 'NO'
		});
		
  	});
</script>
<div class="body">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
    <tiles:importAttribute name="accion"/>
	<tiles:importAttribute name="labelAccion"/>
	
	<div class="form-group form-group-sm row-eq-height">
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 my-wrapper-corrector">
			<div class="panel panel-default my-panel my-panel-corrector">
			  <div class="panel-body my-panel-body">
			    <h4 class="my-panel-title">Configuracion General</h4>
			    <hr class="my-panel-title-divider" />
			    
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:checkboxField name="eventoTradingAlert" base="${modulo}.${clase}.${labelAccion}"/>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:selectComplexEnumField name="patron" base="${modulo}.${clase}.${labelAccion}" itemLabel="descripcion" obligatorio="true"/>
					</div>
				</div>				
			  </div>
		    </div>
	    </div>
		<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12 my-wrapper-corrector">
			<div class="panel panel-default my-panel my-panel-corrector">
			  <div class="panel-body my-panel-body">
			    <h4 class="my-panel-title">Rango Horario</h4>
			    <hr class="my-panel-title-divider" />
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:horaGroup name="horaInicio" base="${modulo}.${clase}.${labelAccion}" obligatorio="true"/>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<html:horaGroup name="horaFin" base="${modulo}.${clase}.${labelAccion}" obligatorio="true"/>
					</div>
				</div>
			  </div>
		    </div>
	    </div>    	
 	</div>
 	<div class="form-group form-group-sm">
		<div class=" col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="panel panel-default my-panel">
				  <div class="panel-body my-panel-body">
				    <h4 class="my-panel-title">Pares de Divisas</h4>
				    <hr class="my-panel-title-divider" />
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_usd" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="usd_jpy" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="gbp_usd" base="${modulo}.${clase}.${labelAccion}"/>
							</div>														
						</div>
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_gbp" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="usd_chf" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_jpy" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
						</div>
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_chf" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="usd_cad" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="aud_usd" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
						</div>
						
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="gbp_jpy" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="aud_cad" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="aud_chf" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
						</div>
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="aud_jpy" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="aud_nzd" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="cad_chf" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
						</div>
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="cad_jpy" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="chf_jpy" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_aud" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
						</div>
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_cad" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_nok" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="eur_nzd" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
						</div>
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="gbp_cad" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="gbp_chf" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="nzd_jpy" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
						</div>
						<div class="form-group form-group-sm">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="nzd_usd" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="usd_nok" base="${modulo}.${clase}.${labelAccion}"/>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<html:checkboxField name="usd_sek" base="${modulo}.${clase}.${labelAccion}"/>
							</div>							
						</div>																									    
				  </div>
				</div>				
		</div>	
	</div>				    	
</div>