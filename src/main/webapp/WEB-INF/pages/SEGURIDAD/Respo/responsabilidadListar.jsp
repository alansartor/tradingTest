<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="ar.com.signals.trading.seguridad.support.Privilegio" %>
<sec:authorize access="hasAuthority('${Privilegio.RESPONSABILIDAD_EDITAR}')" var="hasRESPONSABILIDAD_EDITAR"></sec:authorize>
<sec:authorize access="hasAuthority('${Privilegio.RESPONSABILIDAD_NUEVO}')" var="hasRESPONSABILIDAD_NUEVO"></sec:authorize>
<sec:authorize access="hasAuthority('${Privilegio.RESPONSABILIDAD_VER}')" var="hasRESPONSABILIDAD_VER"></sec:authorize>
<input type="hidden" id="hasRESPONSABILIDAD_EDITAR" value="${hasRESPONSABILIDAD_EDITAR}" />
<input type="hidden" id="hasRESPONSABILIDAD_NUEVO" value="${hasRESPONSABILIDAD_NUEVO}" />
<input type="hidden" id="hasRESPONSABILIDAD_VER" value="${hasRESPONSABILIDAD_VER}" />
<thead>
	<tr>
		<th data-field="id" data-align="right" data-sortable="true">id</th>
		<th data-field="codigo" data-align="right" data-sortable="true">Nombre</th>
		<th data-field="descripcion" data-align="right" data-sortable="true">Descripcion</th>
		<th data-field="accion" data-align="center">Accion</th>
	</tr>
</thead>
<script type="text/javascript">
	var varBuscarUrl = "../Respo/REST/BUSCAR";
	var varColumnDefs = [
		{"targets": [ 0 ],
  			"visible": false,
  			"searchable": false }
				   ];
	var varAoColumns =  [
        { "data": "id" },
        { "data": "codigo" },
        { "data": "descripcion" },
        {
            "data": null,
            "mRender": function (data, type, row, meta) {
            	var acciones = '';
            	if($jQ("#hasRESPONSABILIDAD_EDITAR").val() == "true"){
            		acciones += '<button type="button" class="btn btn-success custom-td-button" onclick="window.location.href=\'../Respo/EDITAR?idParam='+ data.id +'\'"><span class="glyphicon glyphicon-pencil"></span></button>';        		
            	}
            	if($jQ("#hasRESPONSABILIDAD_VER").val() == "true"){
            		acciones += ' <button type="button" class="btn btn-info custom-td-button" onclick="window.location.href=\'../Respo/VER?idParam='+ data.id +'\'"><span class="fas fa-binoculars"></span></button>';
            	}
                return acciones;
            }
        }
	];
	var varButtons =  [{ extend: 'copy', text: '<span class="far fa-copy"></span>' }, { extend: 'excel', text: '<span class="far fa-file-excel"></span>' }, { extend: 'pdf', text: '<span class="far fa-file-pdf"></span>' }, { extend: 'print', text: '<span class="fas fa-print"></span>' }];
	//si bien $jQ se declara en listarTemplate, este codigo se ejecuta antes, por lo que lo declaramos aca tambien.
	//en las variables no hay proble ya que el $jQ se utiliza dentro de function que se ejecutan despues de que se carga listarTemplate lo que hace que ya exista la referencia!
	var $jQ = jQuery.noConflict();
	if($jQ("#hasRESPONSABILIDAD_NUEVO").val() == "true"){
		varButtons.unshift({
			text: '<span class="fas fa-plus"></span>',
		    action: function ( e, dt, node, config ) {
		    	window.location.href="../Respo/NUEVO";
		    },
		    className: "btn-primary",
		    attr:  {
		    	title: "Nueva Responsabilidad"
            }
		});
	}
</script>