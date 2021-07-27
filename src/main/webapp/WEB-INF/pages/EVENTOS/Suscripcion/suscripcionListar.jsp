<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="ar.com.signals.trading.seguridad.support.Privilegio" %>
<sec:authorize access="hasAuthority('${Privilegio.SUSCRIPCION_NUEVO}')" var="hasSUSCRIPCION_NUEVO"></sec:authorize>
<sec:authorize access="hasAuthority('${Privilegio.SUSCRIPCION_ELIMINAR}')" var="hasSUSCRIPCION_ELIMINAR"></sec:authorize>
<input type="hidden" id="hasSUSCRIPCION_NUEVO" value="${hasSUSCRIPCION_NUEVO}" />
<input type="hidden" id="hasSUSCRIPCION_ELIMINAR" value="${hasSUSCRIPCION_ELIMINAR}" />
<thead>
	<tr>
		<th data-field="id" data-align="right">Id</th>
		<th data-field="sucursal_codigo" data-align="right">Establecimiento</th>
		<th data-field="sucursal_codigo" data-align="right">Metodo Notificacion Primario</th>
		<th data-field="eventoDescripcion" data-align="right">Evento</th>
		<th data-field="accion" data-align="center">Accion</th>
	</tr>
</thead>
<ul id="contextMenu" class="dropdown-menu" role="menu"></ul><!-- The .dropdown-menu class will set display:none so it's initially invisible -->
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-contextmenu.js"></script>
<script type="text/javascript">
	var varBuscarUrl = "../Suscripcion/REST/BUSCAR";
	var varSelect = $jQ("#hasSUSCRIPCION_ELIMINAR").val() == "true";//para habilitar seleccion de rows (sin checkbox, sino que se pintan las filas)
	var varColumnDefs = [
	       {"targets": [ 0 ],
      			"visible": false,
      			"searchable": false }
					   ];
	var varFnServerParams = function ( aoData ) {
		aoData.push( { "name": "fltSucursalId", "value": $jQ("#fltSucursalId").val()});      
    };
    varStateLoadParams = function (settings, data) {//recupero los parametros del filtro que guardamos
    	if(typeof data.fltDatosGuardadosLocalmente !== 'undefined' && data.fltDatosGuardadosLocalmente){//si hay datos de los filtros guardados, entonces lo aplico
            $jQ("#fltSucursalId").val(data.fltSucursalId);
            $jQ("#inicializar_con_datos").val(true);//si por defecto la pantalla no llama a buscar, entonces lo forzo aca!
    	}
    };
    varStateSaveParams = function (settings, data) {//guardo los parametros del filtro en localStorage or sessionStorage (datatble guarda el estado de la tabla, y a mano agregamos tambien los parametros del filtro)
    	//no podemos usar los atributos: time, start, length, order, search, columns
    	data.fltDatosGuardadosLocalmente = true;//este tiene que estar siempre, y se usa para saber si se deben tener en cuenta los datos!
    	
        data.fltSucursalId = $jQ("#fltSucursalId").val();
	}
    var varRowId = "id";
	var varAoColumns = [
        { "data": "id" },
        { "data": "sucursal_codigo", className: "text-nowrap"},
        { "data": "metodoIconSpan", className: "text-nowrap"},
        { "data": "eventoDescripcion", className: "text-nowrap" }, 
        {
            "data": null,
            "mRender": function (data, type, row, meta) {
            	var acciones = '';

           		if($jQ("#hasSUSCRIPCION_ELIMINAR").val() == "true"){
               		acciones += ' <button type="button" class="btn btn-danger custom-td-button" onclick="eliminarEntidadDeTabla(\'Suscripcion evento ' + data.eventoDescripcion + ' del Campo ' + data.sucursal_codigo + '\','+data.id+', \'../Suscripcion/ELIMINAR?idParam='+data.id+'\', \'Suscripcion\')"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>';
               	}
            	
            	return acciones;         
            }
        }
    ];
	varDrawCallback = function(settings) {
		//cada vez que traigo datos nuevos a la tabla, entonces atache el evento del contextmenu
		//primero habiamos puesto esto en afterFnCallback, pero cuando se cambia de pagina se necesita volver a llamar, entonces se hace en el Draw
		$jQ("#tablaEntidades td").contextMenu({
		    menuSelector: "#contextMenu",
	    	menuRender: function (invokedOn) {
	    		var tieneOpcion = false;
	    		//el menu depende de las filas seleccionadas, si es una sola, entonces menu completo, si son varias nada por ahora (se podria hacer un eliminar pero es medio peligroso)
	    		//el menu no se corresponde con la fila donde se hizo el click derecho, sino que dependen de la fila seleccionada!
		    	var table = $jQ('#tablaEntidades').DataTable();
	    		var dataArray = table.rows( { selected: true } ).data().toArray();
	    		//NEW si selecciono varios registros, pero no hizo click derecho en ninguno de esos, entonce deselecciono y selecciono el nuevo!
	    		if(dataArray.length > 1){
	    			var clickSobreRowSelected = false;
	    			var clickedRowIdx = table.cell(invokedOn).index().row;
	    			table.rows( { selected: true } ).every( function ( rowIdx, tableLoop, rowLoop ) {//usar every o each de dataTables, no se usa jquery para esto!
	    			    var index = this.index();
	    			    if(index == clickedRowIdx){
	    					clickSobreRowSelected = true;
	    				}
	    			});
	    		}
	    		
		    	if(dataArray.length > 1 && clickSobreRowSelected){
		    		$jQ("#contextMenu").empty();	    			
		    	}else{
		    		if(dataArray.length > 0){
		    			table.rows().deselect();//si el usuario selecciono una celda (click izquierdo), pero luego hizo click derecho en otra, entonces tengo que deseleccionar esa fila y seleccionar la otra
		    			//la forma rapida es siempre deseccionar todo, y seleccionar la row donde se desplego el menu!
		    		}
	    			//no hay nada selccionado, entonces selecciono la fila donde se hizo el clik derecho:
		    		var row = table.rows(table.cell(invokedOn).index().row);
		    		row.select();//la selecciono!
		    		var data = row.data()[0];
		    		//mostrar menu para un solo registro
		    		$jQ("#contextMenu").empty();	            		
		        	if($jQ("#hasPRODUCTOCATEGORIA_ELIMINAR").val() == "true"){
		        		tieneOpcion = true;
		        		$jQ("#contextMenu").append('<li><a tabindex="-1" onclick="eliminarEntidadDeTabla(\'Suscripcion evento ' + data.eventoDescripcion + ' del Campo ' + data.sucursal_codigo + '\','+data.id+', \'../Suscripcion/ELIMINAR?idParam='+data.id+'\', \'Suscripcion\')"><span class="glyphicon glyphicon-trash"></span> Eliminar</a></li>');
		        	}		    		
		    	}
				//devuelvo si hay alguna opcion o esta vacio el menu, esto es para que no se muestre
				return tieneOpcion;
	    	},
		    menuSelected: function (invokedOn, selectedMenu) {
		    	//aca no hago nada, ya que las opciones del menu disparan lo que se tiene que hacer!
		        //"You selected the menu item '" + selectedMenu.text() + "' on the value '" + invokedOn.text() + "'";
		    }
		});
	}
	//IMPORTANTE: para que el menu se muestre en la posicion correcta, debe depender del body, no puede estar dentro de un form, entonces lo muevod e lugar!!
	$jQ('#contextMenu').appendTo("body");
	var varButtons =  [{ extend: 'copy', text: '<span class="far fa-copy"></span>' }, { extend: 'excel', text: '<span class="far fa-file-excel"></span>' }, { extend: 'pdf', text: '<span class="far fa-file-pdf"></span>' }, { extend: 'print', text: '<span class="fas fa-print"></span>' }];
	//si bien $jQ se declara en listarTemplate, este codigo se ejecuta antes, por lo que lo declaramos aca tambien.
	//en las variables no hay proble ya que el $jQ se utiliza dentro de function que se ejecutan despues de que se carga listarTemplate lo que hace que ya exista la referencia!
	var $jQ = jQuery.noConflict();	
	if($jQ("#hasSUSCRIPCION_NUEVO").val() == "true"){
		varButtons.unshift({
			text: '<span class="fas fa-plus"></span>',
		    action: function ( e, dt, node, config ) {
		    	window.location.href="../Suscripcion/NUEVO";
		    },
		    className: "btn-primary",
		    attr:  {
		    	title: "Nueva Suscripcion"
            }
		});
	}
	//variable que se llama en el template luego de finalizado el ready!
	var listarTemplateReadyCallback = function ( table ) {
		if(varSelect){
			//esta linea es para deshabilitar que no pueda seleccionar la fila en la columna de acciones!
			table.on( 'user-select', function ( e, dt, type, cell, originalEvent ) {
				var colIndex = cell.index().column;
		        if ( colIndex === 3) {//columna 'accion', no seleccionar la fila cuando apretan alguno de los botones
		            e.preventDefault();
		        }
		    });
		}
	};
</script>