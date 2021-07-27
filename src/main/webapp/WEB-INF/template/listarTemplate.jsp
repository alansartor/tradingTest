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
</style>
<!-- en la pagina de datatable se puede descargar un solo archivo que contiene todos los js juntos https://datatables.net/download/index -->
<script src="${pageContext.servletContext.contextPath}/public/dataTables/datatables.min.js"></script>
<tiles:importAttribute name="modulo"/>
<tiles:importAttribute name="clase"/>
<tiles:importAttribute name="subClase" ignore="true"/>
<tiles:importAttribute name="accion"/>
<div class="panel panel-default my-no-border-formpanel">
  <div class="panel-body my-formpanel-body">
  	<div class="form-group my-formheader"><div id="my-form-title-div-id" class="col-md-12 col-xs-12"><div class="col-md-10"><div class="pull-left"><h3 class="my-formheader-title">${Accion.getIconForMenuAction(accion)}<spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments="," htmlEscape="false"/><small id="tituloSmallText"></small></h3></div></div>
  	<div class="col-md-2"><div id="divDerechaTitulo" class="pull-right"><h3 class="my-formheader-title">${Accion.getIconClaseForForm(clase)}</h3></div></div></div></div>
	<div class="body">
		<div class="form-group form-group-sm">
			<div class=" col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="panel panel-default my-panel">
				  <div class="panel-body my-panel-body table-responsive"><!-- Para que agregue un scroll si la tabla es muy largo horizontalmente -->
					<table id="tablaEntidades" class="table table-striped table-condensed" width="100%" cellspacing="0">
						<tiles:insertAttribute name="body" />
					</table>		  
				  </div>
				</div>
			</div>	
		</div>	
	</div>
  </div>
</div>
<script type="text/javascript">
	var traerDatos = false; 
 	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		var table = $jQ("#tablaEntidades").DataTable({
			//"dom": '<"toolbar">lfrtip',
			//"sDom": 'T<"clear">lfrtip',
	        "bProcessing": true,
	        "bServerSide": false,
	        "bFilter": true,
	        "bInfo": false,
			"select": typeof varSelect !== 'undefined'?varSelect:false,
	        "sAjaxSource": varBuscarUrl,
	        "iDisplayLength": (typeof variDisplayLength !== 'undefined' && variDisplayLength)?variDisplayLength:50,
	        "footerCallback": typeof varfooterCallback !== 'undefined'?varfooterCallback:null,
	        "sAjaxDataProp": "aaData",
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
	        	if(traerDatos ){
		        	$jQ.ajax( {
		                "dataType": 'json',
		                "type": "GET",
		                "url": sSource,
		                "data": aaData,
		                "success": fnCallback,
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
		            });
	        	}else{
	        		fnCallback({"iTotalRecords":0,"aaData":[]});
	        	}
	        },
	        "buttons": varButtons,
	        "createdRow": typeof varCreatedRow !== 'undefined'?varCreatedRow:null,
	        //Se llama cuando se termina de dibujar los registros (o cuando se modifica a manu una celda tambien, tener cuidado de no duplicar cosas)
	    	"drawCallback": typeof varDrawCallback !== 'undefined'?varDrawCallback:null
 		});
		table.buttons().container().appendTo( '#tablaEntidades_wrapper .col-sm-6:eq(0)');
		//muevo tablaEntidades_length junto con tablaEntidades_filter
		$jQ("#tablaEntidades_filter").wrap("<div id='tablaEntidadesLengthAndFilterDiv' class='form-group form-group-sm pull-right' style='margin-right: 0px;'><div>");//el margen es para arreglar que no se desplace para la derecha por un margen -5
		$jQ("#tablaEntidades_length").insertBefore("#tablaEntidades_filter");
		//hay que sacar el paddin del input 'encontrar' porque sino se desplaza para la derecha y queda fuera del panel
		$jQ("#tablaEntidades_filter input[type=search]").each(function() {
			$jQ(this).css( "padding-right", "0px" ).css( "padding-left", "0px" );
        });
		$jQ("#tablaEntidades_length").addClass("col-sm-6");
		$jQ("#tablaEntidades_filter").addClass("col-sm-6");
		
		//NEW 31/07/2019 si por alguna razon necesito modificar algo del datatable en el implentacion del template, entonces es necesario hacerlo despues de que se inicialice el datatable
		//(ejemplo: agregar botones con funcionalidad custom!), entonces para esto se usamos una funcion, entonces si en la implementacion se la declara, se llama al final del ready!
		if(typeof listarTemplateReadyCallback === 'function'){
			listarTemplateReadyCallback(table);
		}
		
		//Si no se demora la busqueda de los datos, entonces no se muestran los botones
		traerDatos = true; 
		table.ajax.reload();
	});
</script>
