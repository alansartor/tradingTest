<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="ar.com.signals.trading.general.support.Accion" %>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/jsLibraries/chart/Chart.min.css"/>
<style>
/* body { font-size: 140%; } */
div.DTTT { margin-bottom: 0.5em; float: right; }
div.dataTables_wrapper { clear: both; }
.panel-heading {
    padding: 5px 7px;
}
#collapseOne {
    padding: 5px 5px 2px 5px;
}
</style>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/moment.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/chart/Chart.min.js"></script><!-- Si o si tiene que estar antes moment.js para que funcionen correctamentes los ejes de tiempo -->
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<input type="hidden" id="inicializar_con_datos" value="${inicializar_con_datos}" />
<div class="panel panel-default my-no-border-formpanel">
  <div class="panel-body my-formpanel-body">
  	<div class="form-group my-formheader"><div id="my-form-title-div-id" class="col-md-12 col-xs-12"><div class="col-md-10"><div class="pull-left"><h3 class="my-formheader-title">${Accion.getIconForMenuAction(accion)}<spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments="," htmlEscape="false"/><small id="tituloSmallText"></small></h3></div></div>
  	<div class="col-md-2"><div id="divDerechaTitulo" class="pull-right"><h3 class="my-formheader-title">${Accion.getIconClaseForForm(clase)}</h3></div></div></div></div>
	<div class="body">
		<div class="form-group form-group-sm">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<form role="form" data-toggle="validator" class="form-horizontal" id="filter"><!--  method="GET" action="#" --><!-- el form se necesita para validaciones en pantalla -->		    
					<div class="panel panel-default my-panel">
					  <div class="panel-heading" style="background-color: #fff;">
					    <div class="row form-group" style="margin-bottom: 0px;">
					    	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6"><div class="pull-left"><h4 style="margin-top: 7px;margin-bottom: 7px;"><a id="filtroId" data-toggle="collapse" href="#collapseOne" aria-expanded="false" aria-controls="collapseOne"><span class="glyphicon glyphicon-filter"></span> Filtro <small id="filtroHelpTextId">Presione para ver mas opciones</small></a></h4></div></div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6"><div class="form-group form-group-sm pull-right"><div id="divFiltroBuscar" class="col-sm-6" style="margin-top: 2px;"></div><div class=" col-sm-6"><button type="submit" id="buttonBuscar" name="buttonBuscar" class="btn btn-info pull-right" title="Buscar datos en el Servidor"><span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar</button></div></div></div>
						</div>
					  </div>
					  <div class="panel-body collapse" id="collapseOne">
					    <div class="form-group cols-bottom">
						  <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						    <tiles:insertAttribute name="filtro" />
						  </div>
					  	</div>
					  </div>
					</div>
				</form>
			</div>	
		</div>	
		<div class="form-group form-group-sm">
			<div class=" col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="panel panel-default my-panel">
				  <div id="chart_container" class="panel-body my-panel-body"><!-- Para que agregue un scroll si la tabla es muy largo horizontalmente -->
					<canvas id="myChart"></canvas>
	  				<tiles:insertAttribute name="chart" />		  
				  </div>
				</div>
			</div>	
		</div>	
	</div>
  </div>
</div>
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/validator/validator.min.js"></script>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
 	var chartData = {
 		datasets:[]
 	};
	$jQ().ready(function () {
		var ctx = document.getElementById('myChart').getContext('2d');
		var myChart = new Chart(ctx, {
		  type: varChartType,
		  data: chartData,
		  //data: {},no inicializ los datos para no confundir al chart, para que tome el formato de datos del primer lote de datos que se le asignen! ya que los datos pueden venir en dos formatos para los datos tipo line: number[] or Point[]
		  options: varChartOptions//esto no se puede cambiar despues, y falla si no esta inicializado, entonces tiene que estar de entrada
		});
		//seteo el alto del chart (no hay forma de que se extire automaticamente hasta ocupar toda la parte vertical de la pantalla, hay que hacerlo a mano)
		$jQ("#chart_container").css('height', ($jQ(window).height() - $jQ("#chart_container").offset().top - 30) + 'px');//el 30 son los margenes de los paneles		
		$jQ("#filter").validator().on('submit', function (e) {//se usa para validaciones en la pantalla
 			if (e.isDefaultPrevented()) {
				// handle the invalid form...
			} else {
				// everything looks good!
				$jQ("#buttonBuscar").attr('disabled', true);
				
				//buscar los datos que se deben mandar como filtro
				var aoData = [];
				varFnServerParams(aoData);
				//llamar ajax para popular el chart
			    $jQ.ajax({
		             "dataType": 'json',
		             "type": "GET",
		             "url": varChartUrl,
		             "data": aoData,//estos tiene que ser un array o un string que se concatena al url, entonces se debe armar antes de llamar al metodo, y se debe actualizar con los nuevos filtro antes de cada llamada
		             "success": function (data, textStatus, jqXHR){
		            	 //myChart.config.type=data.type; el typo no cambia, entonces ya lo inicializo de entrada
		            	 //myChart.config.data=data.data;
		            	 chartData.datasets=data.data.datasets;
		            	 myChart.update();
		            	 var open = $jQ('#filtroId').attr('aria-expanded');
						 if(open == 'true'){
						 	$jQ('#filtroId').trigger('click');//Si hay registros entonces se cierra el panel del filtro
						 }
		             },
		             "error": function (request, status, error) {
		             	//alert(status);
		             	//alert(error);
		             	//BootstrapDialog.alert(request.responseText);
		             	BootstrapDialog.show({
		                     type: BootstrapDialog.TYPE_WARNING,
		                     title: 'Advertencia',
		                     message: request.responseJSON.observaciones,
		                     buttons: [{
		                         label: 'OK',
		                        	action: function(dialogRef){    
		                                dialogRef.close();
		                            }
		                     }]
		               });
		               myChart.config.data = [];//en teoria pone en blanco los datos
		               myChart.update();
		             }
		        }).done($jQ("#buttonBuscar").attr('disabled', false));
				return false;
			}
 		});
		$jQ('#buttonRefrescar').on('click', function(){
			$jQ('#filter').trigger('submit');
		});
 		var inicializar_con_datos = $jQ("#inicializar_con_datos").val();
 		if(inicializar_con_datos == 'true'){
 			$jQ('#filter').trigger('submit');//para disparar el buscar (si hay alguna validacion y no se cumple, entonces no se dispara el buscar)
 		}
	});
</script>