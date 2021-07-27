<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="ar.com.signals.trading.general.support.Accion" %>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/dataTables/datatables.min.css"/>
<style>
/* body { font-size: 140%; } */
div.DTTT { margin-bottom: 0.5em; float: right; }
div.dataTables_wrapper { clear: both; }
.panel-heading {
    padding: 5px 12px;
}
#collapseOne {
    padding: 5px 5px 2px 5px;
}
/* @media (min-width: 768px) {/* Solo aplico a esto desde sm y mas grande para que el boton buscar quede abajo!*/
	.cols-bottom {
		display: flex;
	    align-items: end;
	}
} */
/* Para no mostrar en azul (primary color) el boton activo del pagination */
.pagination > .active > a, .pagination > .active > a:focus, .pagination > .active > a:hover {
	background-color: #8e8e99;
	border-color: #8e8e99;
}
</style>
<!-- Para que funcione el plugin para date format and order de dataTable esto tiene que cargarse primero -->
<!-- en la pagina de datatable se puede descargar un solo archivo que contiene todos los js juntos https://datatables.net/download/index -->
<script src="${pageContext.servletContext.contextPath}/public/dataTables/datatables.min.js"></script><!-- Error de mapa de fuente: Error: request failed with status 404: en teoria sale cuando abrimos dev tools del navegador, lo podemos ignorar deshabilitando 'habilitrar mapas de fuentea' del navegador (opciones de depuracion) -->
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<input type="hidden" id="inicializar_con_datos" value="${inicializar_con_datos}" />
<input type="hidden" id="inicializar_con_datos_notificacion" value="${inicializar_con_datos_notificacion}" />
<input type="hidden" id="ignorar_session_state" value="${ignorar_session_state}" />
<input type="hidden" id="inicializado_con_save_state" value="false" />
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
				  <div class="panel-body my-panel-body table-responsive"><!-- Para que agregue un scroll si la tabla es muy largo horizontalmente -->
					<table id="tablaEntidades" class="table table-striped table-condensed" width="100%" cellspacing="0">
						<tiles:insertAttribute name="tabla" />
					</table>		  
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
 	var sEcho = 0;
	$jQ().ready(function () {
		var table = $jQ("#tablaEntidades").DataTable({
			//"dom": '<"toolbar">lfrtip',
			//"sDom": 'T<"clear">lfrtip',
	        "processing": false,//para no mostrar el cartelito de procesando, ya que se reemplaza por un mensaje modal
	        "bServerSide": false,
	        "bFilter": true,
	        "bInfo": false,
			"select": typeof varSelect !== 'undefined'?varSelect:false,
	        "sAjaxSource": String(varBuscarUrl),
	        "iDisplayLength": (typeof variDisplayLength !== 'undefined' && variDisplayLength)?variDisplayLength:50,
	    	"footerCallback": typeof varfooterCallback !== 'undefined'?varfooterCallback:null,
	        "sAjaxDataProp": "aaData",
	        "fnServerParams": varFnServerParams,
	        "rowId": typeof varRowId !== 'undefined'?varRowId:null,//para que data table identifique cada row con esta propiedad, entonces despues podemos hacer referencia al row con el id de la entidad, mas que nada en eliminar!
	        "columnDefs": varColumnDefs,
	        "aoColumns": varAoColumns,
	        "aaSorting": [],//para que respete el orden natural de los datos, sino ordena por defecto por la primer columna	       
	        "oLanguage": {
	        	"sSearch": "Encontrar",
	        	"sLengthMenu": "Mostrar _MENU_ registros",
	        	"oPaginate": {
	                "sPrevious": "Anterior",
	                "sNext": "Siguiente"
	            }
	        },
	        "fnServerData": function ( sSource, aaData, fnCallback ) {
	        	if(sEcho>0){
		        	$jQ.ajax({
		                "dataType": 'json',
		                "type": "GET",
		                "url": sSource,
		                "data": aaData,
		                "success": function(data){
		                	if(typeof beforeFnCallback !== 'undefined'){
		                		beforeFnCallback(data);
		                	}
		                	fnCallback(data);
		                	if(typeof afterFnCallback !== 'undefined'){
		                		afterFnCallback(data);
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
                        	//alert(request.responseText);
                        	fnCallback({"iTotalRecords":0,"aaData":[]});
                        }
		            }).done($jQ("#buttonBuscar").attr('disabled', false));
	        	}else{
	        		fnCallback({"iTotalRecords":0,"aaData":[]});
	        	}
	        	++sEcho;
	        },
	        "buttons": varButtons,
	        "createdRow": typeof varCreatedRow !== 'undefined'?varCreatedRow:null,
	    	//Se llama cuando se termina de dibujar los registros (o cuando se modifica a manu una celda tambien, tener cuidado de no duplicar cosas)
	    	"drawCallback": typeof varDrawCallback !== 'undefined'?varDrawCallback:null,
	    	"rowGroup": typeof varRowGroup !== 'undefined'?varRowGroup:null,
   	        "stateSave": true,//esto guarda el estado de la tabla, entonces si refrescamos o volvemos para atras recumeramos info (los parametros del filtro hay que manejarlos a mano!)
   	     	//issue stateSave: si quiero ir a una pantalla con x filtros (ejemplo link stock insumos: ir a comprobantes pendiente de imputar donde figura el insumo) pero antes estuve en esa pantalla, entonces me filtra con lo guardado y no con lo que necesito, entonces hay que anular el uso de la sessionState, esto se hace en el controller incluyendo parametro 'ignorar_session_state'
	    	"stateDuration": -1,//duracion en segundos de los datos de la tabla y sus filtros. El -1 se usa para indicar que la info se guarda en sessionStorage, lo que hace que solo se usae dentro de una pestaña, mejor
	    	"stateLoadParams": typeof varStateLoadParams !== 'undefined' && $jQ("#ignorar_session_state").val() != 'true'?varStateLoadParams:null,//ignorar_session_state se usa para forzar que se deben usar los filtros que vienen del controller y no los guardados en la session!
   	     	"stateSaveParams": typeof varStateSaveParams !== 'undefined'?varStateSaveParams:null
		}).on('processing.dt', function ( e, settings, processing ) {
			//$jQ('#processingIndicator').css( 'display', processing ? 'block' : 'none' );
			if(processing){
				waitingDialog.show('Buscando Datos', {dialogSize: 'sm'});
			}else{
				waitingDialog.hide();
			}
	    });//para cambiar el cartelito de procesando, por un mensaje modal!
	    
		table.buttons().container().appendTo( '#tablaEntidades_wrapper .col-sm-6:eq(0)');
 		$jQ("#filter").validator().on('submit', function (e) {//se usa para validaciones en la pantalla
 			if (e.isDefaultPrevented()) {
				// handle the invalid form...
			} else {
				// everything looks good!
				//NEW 13/11/2019 Para evitar doble disparos de este evento, pongo una variable con la ultima vez que se ejecuto, y no dejo ejecutar hasta que no pasen unos milisegundos
				var validator = $jQ(e.target);
				if(!validator.data('lockedAt') || (+new Date() - validator.data('lockedAt') > 500)) {//si es la primera vez, o la fecha actual menos la ultima vez que se ejecuto correctamente supera los 500 milisegundos, entonces ejecuto
					validator.data('lockedAt', +new Date());
					$jQ("#buttonBuscar").attr('disabled', true);
					//borro el input search, sino queda con valor de la busqueda anterior y la tabla muestra datos filtrados, lo que
					//puede confundir al usuario. hacer el keyup para que datatable tome el cambio!
					var mySearch = $jQ("#tablaEntidades_filter input[type='search']");
					if(mySearch){
						mySearch.val("").keyup();
					}
					table.ajax.reload(function ( datos ) {
						if (table.data().count()){
							var open = $jQ('#filtroId').attr('aria-expanded');
							if(open == 'true'){
								$jQ('#filtroId').trigger('click');//Si hay registros entonces se cierra el panel del filtro
							}					
						}
					});
			    }
				return false;
			}
 		});
 		
 		$jQ('#collapseOne').on('show.bs.collapse', function () {
 			$jQ('#filtroHelpTextId').html('Presione para ocultar las opciones');
 		});
		$jQ('#collapseOne').on('hide.bs.collapse', function () {
			$jQ('#filtroHelpTextId').html('Presione para ver mas opciones');
 		});
 		
		$jQ('#buttonRefrescar').on('click', function(){
			$jQ('#filter').trigger('submit');
		}).hide();
		//muevo tablaEntidades_length junto con tablaEntidades_filter
		$jQ("#tablaEntidades_filter").wrap("<div id='tablaEntidadesLengthAndFilterDiv' class='form-group form-group-sm pull-right'><div>");
		$jQ("#tablaEntidades_length").insertBefore("#tablaEntidades_filter");
		//hay que sacar el paddin del input 'encontrar' porque sino se desplaza para la derecha y queda fuera del panel
		$jQ("#tablaEntidades_filter input[type=search]").each(function() {
			$jQ(this).css( "padding-right", "0px" ).css( "padding-left", "0px" );
        });
		$jQ("#tablaEntidades_length").addClass("col-sm-6");
		$jQ("#tablaEntidades_filter").addClass("col-sm-6");
		
		
		if (document.getElementById("shownOnDivFiltroBuscar")) {
			$jQ("#shownOnDivFiltroBuscar").prependTo("#divFiltroBuscar");
		}
		
		//si por alguna razon necesito modificar algo del datatable en el implentacion del template, entonces es necesario hacerlo despues de que se inicialice el datatable
		//(ejemplo: agregar botones con funcionalidad custom!), entonces para esto usamos una funcion, entonces si en la implementacion se la declara, se llama al final del ready!
		if(typeof listarTemplateReadyCallback === 'function'){
			listarTemplateReadyCallback(table);
		}
 		var inicializar_con_datos = $jQ("#inicializar_con_datos").val();
 		if(inicializar_con_datos == 'true'){
 			$jQ('#filter').trigger('submit');//para disparar el buscar (si hay alguna validacion y no se cumple, entonces no se dispara el buscar)
 		}else{//si no se inicializa con datos, entonces dejo abierto el filtro
 			var open = $jQ('#filtroId').attr('aria-expanded');
			if(open == 'false'){
				$jQ('#filtroId').trigger('click');
			}
 		}
 		
 		var inicializado_con_save_state = $jQ("#inicializado_con_save_state").val();
 		if(inicializado_con_save_state == 'true'){
			$jQ.notify({
				message: 'Se muestran los registros utilizando los filtros usados anteriormente<br>Si lo desea puede cambiar los filtros, para ver todas las opciones haga click donde dice <span class="glyphicon glyphicon-filter"></span> <b>Filtro</b> y luego presione el boton <span class="glyphicon glyphicon-search" aria-hidden="true"></span> <b>Buscar</b>',			
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
				delay: 10000,
				icon_type: 'class'
			});
 		}else{
 			if(inicializar_con_datos == 'true'){
 				var inicializar_con_datos_notificacion = $jQ("#inicializar_con_datos_notificacion").val();
 				if(inicializar_con_datos_notificacion){
 	 				$jQ.notify({
 	 					message: inicializar_con_datos_notificacion + '<br>Si lo desea puede cambiar los filtros, para ver todas las opciones haga click donde dice <span class="glyphicon glyphicon-filter"></span> <b>Filtro</b> y luego presione el boton <span class="glyphicon glyphicon-search" aria-hidden="true"></span> <b>Buscar</b>',			
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
 	 					delay: 15000,
 	 					icon_type: 'class'
 	 				});
 				}
 	 		}else{
 				$jQ.notify({
 					message: 'Ingrese algun filtro y haga click el boton <span class="glyphicon glyphicon-search" aria-hidden="true"></span> <b>Buscar</b> para ver registros',			
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
 					delay: 10000,
 					icon_type: 'class'
 				});
 	 		}
 		}
	});
</script>