<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="ar.com.signals.trading.seguridad.support.Privilegio" %>
<sec:authorize access="hasAuthority('${Privilegio.RESPOUSUARIO_EDITAR}')" var="hasRESPOUSUARIO_EDITAR"></sec:authorize>
<sec:authorize access="hasAuthority('${Privilegio.RESPOUSUARIO_NUEVO}')" var="hasRESPOUSUARIO_NUEVO"></sec:authorize>
<input type="hidden" id="hasRESPOUSUARIO_EDITAR" value="${hasRESPOUSUARIO_EDITAR}" />
<input type="hidden" id="hasRESPOUSUARIO_NUEVO" value="${hasRESPOUSUARIO_NUEVO}" />
<thead>
	<tr>
		<th data-field="id" data-align="right" data-sortable="true">id</th>
		<th data-field="usuario_username" data-align="right" data-sortable="true">Username</th>
		<th data-field="usuario_descripcion" data-align="right" data-sortable="true">Apellido y Nombre</th>
		<th data-field="respo_codigo" data-align="right" data-sortable="true">Responsabilidad</th>
		<th data-field="activo" data-align="center">Activo</th>
		<th data-field="accion" data-align="center">Accion</th>
	</tr>
</thead>
<script type="text/javascript">
	//si bien $jQ se declara en listarTemplate, este codigo se ejecuta antes, por lo que lo declaramos aca tambien.
	//en las variables no hay proble ya que el $jQ se utiliza dentro de function que se ejecutan despues de que se carga listarTemplate lo que hace que ya exista la referencia!
	var $jQ = jQuery.noConflict();
	var varBuscarUrl = "../RespoUsuario/REST/BUSCAR";
	var varColumnDefs = [
	       {"targets": [ 0 ],
   			"visible": false,
   			"searchable": false }
	];
	var varAoColumns = [
        { "data": "id" },
        { "data": "usuario_username" },
        { "data": "usuario_descripcion" },
        { "data": "respo_codigo" },
        { "data": "activo" },
        {
            "data": null,
            "bSortable": false,
            "mRender": function (data, type, row, meta) {
            	var acciones = '';
            	if($jQ("#hasRESPOUSUARIO_EDITAR").val() == "true"){      		
            		acciones += '<button type="button" class="btn btn-success custom-td-button" onclick="window.location.href=\'../RespoUsuario/EDITAR?idParam='+ data.id +'\'"><span class="glyphicon glyphicon-pencil"></span></button>';
            	}
                return acciones;
            }
        }
    ];
	var varButtons =  [{ extend: 'copy', text: '<span class="far fa-copy"></span>' }, { extend: 'excel', text: '<span class="far fa-file-excel"></span>' }, { extend: 'pdf', text: '<span class="far fa-file-pdf"></span>' }, { extend: 'print', text: '<span class="fas fa-print"></span>' }];
	if($jQ("#hasRESPOUSUARIO_NUEVO").val() == "true"){
		varButtons.unshift({
			text: '<span class="fas fa-plus"></span>',
		    action: function ( e, dt, node, config ) {
		    	window.location.href="../RespoUsuario/NUEVO";
		    },
		    className: "btn-primary",
		    attr:  {
		    	title: "Nueva Asignacion Responsabilidad"
            }
		});
	}
</script>