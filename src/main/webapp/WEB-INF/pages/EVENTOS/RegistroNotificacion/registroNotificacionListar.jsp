<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="ar.com.signals.trading.seguridad.support.Privilegio" %>
<thead>
	<tr>
		<th data-field="evento_id" data-align="right" data-sortable="true">id</th>
		<th data-field="evento_fecha" data-align="right" data-sortable="true">Fecha</th>
		<th data-field="evento" data-align="right" data-sortable="true">Evento</th>
		<th data-field="sujeto_descripcion" data-align="right" data-sortable="true">Titular</th>
		<th data-field="sucursal_codigo" data-align="right" data-sortable="true">Campo</th>
		<th data-field="unidadProductiva_codigo" data-align="right" data-sortable="true">Lote</th>
		<th data-field="observaciones" data-align="right" data-sortable="true">Observaciones</th>
	</tr>
</thead>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/moment.min.js"></script>
<script type="text/javascript">
	var varBuscarUrl = "../RegistroNotificacion/REST/BUSCAR";
	var varColumnDefs = [
		{"targets": [ 0 ],
  			"visible": false,
  			"searchable": false },
  	 	{"targets": [ 1 ],
  	 		     "render": function (data, type, full, meta) {return data?moment(data).format("DD/MM/YYYY"):null;}}
				   ];
	var varAoColumns =  [
        { "data": "evento_id" },
        { "data": "evento_fecha" },
        { "data": "evento" },
        { "data": "sujeto_descripcion" },
        { "data": "sucursal_codigo" },
        { "data": "unidadProductiva_codigo" },
        { "data": "observaciones" }
	];
	var varButtons =  [{ extend: 'copy', text: '<span class="far fa-copy"></span>' }, { extend: 'excel', text: '<span class="far fa-file-excel"></span>' }, { extend: 'pdf', text: '<span class="far fa-file-pdf"></span>' }, { extend: 'print', text: '<span class="fas fa-print"></span>' }];
	//si bien $jQ se declara en listarTemplate, este codigo se ejecuta antes, por lo que lo declaramos aca tambien.
	//en las variables no hay proble ya que el $jQ se utiliza dentro de function que se ejecutan despues de que se carga listarTemplate lo que hace que ya exista la referencia!
	var $jQ = jQuery.noConflict();
</script>