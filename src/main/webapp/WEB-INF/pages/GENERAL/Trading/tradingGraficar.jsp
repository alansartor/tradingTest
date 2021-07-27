<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
    border-bottom: 1px solid #ddd;
}
#filtroId:after {
    font-family:'Glyphicons Halflings';
    content:"\e114";
    float: left;
    color: grey;
}
#filtroId.collapsed:after {
    content:"\e080";
}
#buttonRefrescar {
    float: right;
}
</style>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/moment.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/chart-beta/luxon.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/chart-beta/chart.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/chart-beta/chartjs-adapter-luxon.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/chart-beta/chartjs-chart-financial.js"></script>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>

<input type="hidden" id="inicializar_con_datos" value="${inicializar_con_datos}" />
<!-- <div class="page-header"> -->
  <h3><span class="far fa-chart-bar" aria-hidden="true"></span> <spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments="," htmlEscape="false"/></h3>
<!-- </div> -->
<div class="panel panel-default">
  <!-- <div class="panel-footer clearfix"> -->
  <div class="panel-heading">
    <a id="filtroId" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Filtro</a>
    <a id="buttonRefrescar" ><span class="glyphicon glyphicon-search" aria-hidden="true"></span></a>
  </div>
  <div class="panel-body collapse in" id="collapseOne">
	  <form role="form" data-toggle="validator" class="form-horizontal" id="filter"><!--  method="GET" action="#" --><!-- el form se necesita para validaciones en pantalla -->
		<div class="form-group form-group-sm">
			<div class="col-lg-6 col-md-8 col-sm-12 col-xs-12">	
				<label class="col-xs-12 col-sm-5 col-md-5 col-lg-5 control-label" for="fltTrueFXDivisas">Divisa</label>  
				<div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
					<select id="fltTrueFXDivisas" class="form-control selectpicker" onkeypress="if(teclaEnterPresionada(event)){$jQ('#buttonBuscar').trigger('click');}">
				   		<option value="">-Todos-</option>
						<c:forEach items="${trueFXDivisas}" var="tic">
							<option value="${tic}" ${tic == fltTrueFXDivisasValue ? 'selected' : ''}>${tic}</option>
						</c:forEach>
					</select>
		  		</div>
			</div>
		</div>		
	    <div class="form-group">
		  <div class="col-md-12">
		    <button type="submit" id="buttonBuscar" name="buttonBuscar" class="btn btn-info pull-right"><span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar</button>
		  </div>
	  	</div>
  </form>
  </div>  
  <div class="panel-body">
	  <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="padding: 15px;">
	  	<canvas id="myChart" style="height:500px !important;"></canvas>
	  </div>
  </div>
</div>
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/validator/validator.min.js"></script>
<script type="text/javascript">
	var varChartUrl = "../Trading/REST/BUSCAR";
	var varChartType = "candlestick";
	var varChartOptions ={
			responsive: true,
			maintainAspectRatio: false
	};
	var varChartTitle = {
      display: false,
      text: "Divisas",
    };
	var varFnServerParams = function ( aoData ) {	
        aoData.push( { "name": "fltTrueFXDivisas", "value": $jQ("#fltTrueFXDivisas").val()});
    };
    
 	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		var ctx = document.getElementById('myChart').getContext('2d');
		var myChart = new Chart(ctx, {
		  type: varChartType,
		  //data: {},no inicializ los datos para no confundir al chart, para que tome el formato de datos del primer lote de datos que se le asignen! ya que los datos pueden venir en dos formatos para los datos tipo line: number[] or Point[]
		  options: varChartOptions//esto no se puede cambiar despues, y falla si no esta inicializado, entonces tiene que estar de entrada
		});		
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
		            	 myChart.config.data=data.data;
		            	 myChart.config.options = {
		            	        scales: {
		            	            xAxes: [{
		            	                type: 'time',
		            	                distribution: 'series'
		            	            }]
		            	        }
		            	    };
		            	 myChart.update();
		            	 if(data.mensajes){
		            		 data.mensajes.forEach(function (item, index) {
		            				$jQ.notify({
		            					message: item.mensaje,			
		            					icon: 'glyphicon glyphicon-info-sign'
		            				}, {
		            					newest_on_top: true,
		            					type: 'info',
		            					allow_dismiss: true,
		            					placement: {
		            						from: 'top',
		            						align: 'right'
		            					},
		            					animate: {
		            						enter: 'animated fadeInDown',
		            						exit: 'animated fadeOutUp'
		            					},
		            					delay: 60000,
		            					icon_type: 'class'
		            				});
		            		 });
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
 			//para disparar el buscar (si hay alguna validacion y no se cumple, entonces no se dispara el buscar)
 			setInterval(function() {$jQ('#filter').trigger('submit');}, 1000);
 		}
	});
</script>