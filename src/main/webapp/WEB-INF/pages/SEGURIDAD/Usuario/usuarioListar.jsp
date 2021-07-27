<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="ar.com.signals.trading.seguridad.support.Privilegio" %>
<sec:authorize access="hasAuthority('${Privilegio.USUARIO_EDITAR}')" var="hasUSUARIO_EDITAR"></sec:authorize>
<sec:authorize access="hasAuthority('${Privilegio.USUARIO_NUEVO}')" var="hasUSUARIO_NUEVO"></sec:authorize>
<sec:authorize access="hasAuthority('${Privilegio.USUARIO_RESETEAR}')" var="hasUSUARIO_RESETEAR"></sec:authorize>
<input type="hidden" id="hasUSUARIO_RESETEAR" value="${hasUSUARIO_RESETEAR}" />
<input type="hidden" id="hasUSUARIO_EDITAR" value="${hasUSUARIO_EDITAR}" />
<input type="hidden" id="hasUSUARIO_NUEVO" value="${hasUSUARIO_NUEVO}" />
<thead>
	<tr>
		<th data-field="id" data-align="right" data-sortable="true">id</th>
		<th data-field="username" data-align="right" data-sortable="true">Username</th>
		<th data-field="descripcion" data-align="right" data-sortable="true">Apellido y Nombre</th>
		<th data-field="user_email" data-align="right" data-sortable="true">Email</th>
		<th data-field="user_enabled" data-align="center">Habilitado</th>
		<th data-field="accion" data-align="center">Accion</th>
	</tr>
</thead>
<ul id="contextMenu" class="dropdown-menu" role="menu"></ul><!-- The .dropdown-menu class will set display:none so it's initially invisible -->
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-contextmenu.js"></script>
<script type="text/javascript">
	//si bien $jQ se declara en listarTemplate, este codigo se ejecuta antes, por lo que lo declaramos aca tambien.
	//en las variables no hay proble ya que el $jQ se utiliza dentro de function que se ejecutan despues de que se carga listarTemplate lo que hace que ya exista la referencia!
	var $jQ = jQuery.noConflict();
	var varBuscarUrl = "../Usuario/REST/BUSCAR";
	var varSelect = $jQ("#hasUSUARIO_RESETEAR").val() == "true" || $jQ("#hasUSUARIO_EDITAR").val() == "true" || $jQ("#hasUSUARIO_NUEVO").val() == "true";//para habilitar seleccion de rows (sin checkbox, sino que se pintan las filas)
	var varColumnDefs = [ {"targets": [ 0 ], "visible": false, "searchable": false} ];
	var varFnServerParams = function ( aoData ) {
        aoData.push( { "name": "fltUsername", "value": $jQ("#fltUsername").val()});
        aoData.push( { "name": "fltDescripcion", "value": $jQ("#fltDescripcion").val()});
    };
    varStateLoadParams = function (settings, data) {//recupero los parametros del filtro que guardamos
    	if(typeof data.fltDatosGuardadosLocalmente !== 'undefined' && data.fltDatosGuardadosLocalmente){//si hay datos de los filtros guardados, entonces lo aplico
            $jQ("#fltUsername").val(data.fltUsername);
            $jQ("#fltDescripcion").val(data.fltDescripcion);
            $jQ("#inicializar_con_datos").val(true);//si por defecto la pantalla no llama a buscar, entonces lo forzo aca!    
            $jQ("#inicializado_con_save_state").val(true);//para saber que se estan mostrando la pantalla con filtros que estaban guardados!
    	}
    };
    varStateSaveParams = function (settings, data) {//guardo los parametros del filtro en localStorage or sessionStorage (datatble guarda el estado de la tabla, y a mano agregamos tambien los parametros del filtro)
    	//no podemos usar los atributos: time, start, length, order, search, columns
    	data.fltDatosGuardadosLocalmente = true;//este tiene que estar siempre, y se usa para saber si se deben tener en cuenta los datos!
    	
        data.fltUsername = $jQ("#fltUsername").val();
        data.fltDescripcion = $jQ("#fltDescripcion").val();
	}
    var varRowId = "id";
	var varAoColumns = [
		{ "data": "id" },
        { "data": "username" },
        { "data": "descripcion" },
        { "data": "user_email" },
        { "data": "user_enabled" },
        {
            "data": null,
            "bSortable": false,
            "mRender": function (data, type, row, meta) {
            	var acciones = '';
            	if($jQ("#hasUSUARIO_EDITAR").val() == "true"){      		
            		acciones += '<button type="button" class="btn btn-success custom-td-button" onclick="window.location.href=\'../Usuario/EDITAR?idParam='+ data.id +'\'"><span class="glyphicon glyphicon-pencil"></span></button>';
            	}
            	if($jQ("#hasUSUARIO_RESETEAR").val() == "true"){      		
            		acciones += ' <button type="button" class="btn btn-warning custom-td-button" onclick="window.location.href=\'../Usuario/RESETEAR?idParam='+ data.id +'\'" title="Resetear Password"><span class="fas fa-undo"></span></button>';
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
		    		if($jQ("#hasUSUARIO_EDITAR").val() == "true"){
		    			tieneOpcion = true;
		    			$jQ("#contextMenu").append('<li><a tabindex="-1" onclick="window.location.href=\'../Usuario/EDITAR?idParam='+ data.id +'\'"><span class="glyphicon glyphicon-pencil"></span> Editar</a></li>');
		        	}
            		if($jQ("#hasUSUARIO_RESETEAR").val() == "true"){
		    			tieneOpcion = true;
		    			$jQ("#contextMenu").append('<li><a tabindex="-1" onclick="window.location.href=\'../Usuario/RESETEAR?idParam='+ data.id +'\'"><span class="fas fa-undo"></span> Resetear Password</a></li>');
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
	if($jQ("#hasUSUARIO_NUEVO").val() == "true"){
		varButtons.unshift({
			text: '<span class="fas fa-plus"></span>',
		    action: function ( e, dt, node, config ) {
		    	window.location.href="../Usuario/NUEVO";
		    },
		    className: "btn-primary",
		    attr:  {
		    	title: "Nuevo Usuario"
            }
		});
	}
	//variable que se llama en el template luego de finalizado el ready!
	var listarTemplateReadyCallback = function ( table ) {
		if(varSelect){
			//esta linea es para deshabilitar que no pueda seleccionar la fila en la columna de acciones!
			table.on( 'user-select', function ( e, dt, type, cell, originalEvent ) {
				var colIndex = cell.index().column;
		        if ( colIndex === 5) {//columna 'accion', no seleccionar la fila cuando apretan alguno de los botones
		            e.preventDefault();
		        }
		    });
		}
	};
</script>