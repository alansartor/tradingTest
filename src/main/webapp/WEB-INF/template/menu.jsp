<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<style>
.navbar{
	/* Por defecto el navbar deja un margen abajo, con esto lo saco */
	margin-bottom: 0px;
}
.navbar-brand{
	padding-top: 10px;
}
.navbar-brand small {
  display:block;
  font-size:12px;
  margin-top: -5px;
  text-align: center;
  letter-spacing: 2px;
}
div.my-custom-popover{
	max-width: 450px !important;
    width: 450px !important;
}
div.my-custom-popover-content{
	max-height: calc(90vh - 100px);
    overflow-y: auto;
    padding-left: 2px;
	padding-right: 2px;
}

</style>
<script type="text/javascript"> 	
	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {
		//por ahora armo una sola vez el popover de notificaciones, ver si en algun momento armar una logica para que se actualice cada tanto tiempo, por si el usuario queda mucho tiempo en una pantalla
		//llamo a un rest para ver notificaciones, diferenciando las nuevas de las marcadas como vistas!
		$jQ.getJSON($jQ("#applicationContextPath").val() + "/web/support/REST/registroNotificacion/ultimos/", function( data ) {
			var content = '<ul class="list-group">';
			var countNoVistos = 0;
			var noVistosIds = [];
			if(data){
				$jQ.each(data, function(index, notificacion) {
					if(notificacion.visto == false){
						++countNoVistos;
						noVistosIds.push(notificacion.registroEvento.id); 
					}
					content += '<li class="list-group-item" style="border: none;border-top: 1px solid #ddd;">'+notificacion.registroEvento.observaciones+'</li>';
				});
				if(countNoVistos > 0){
					$jQ("#notificationsPopoverBadge").html(countNoVistos);
				}
			}else{
				content += 'No se encontraron nuevas notificaciones';
			}
			content += '</ul>';
			$jQ('#notificationsPopover').popover({
				//container: 'body', //si modificamos el tamaño del navegador, el popover queda suelto
				viewport: { selector: 'body', padding: 5 },
	            placement : 'auto',
	            trigger : 'click',
	            html: true,
	            title: 'Notificaciones<a href="'+$jQ("#applicationContextPath").val()+'/web/EVENTOS/RegistroNotificacion/LISTAR" style="float: right;"><span class="glyphicon glyphicon-list"></span>Listar Todas</a>',
	            template: '<div class="popover my-custom-popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content my-custom-popover-content"></div></div>',//pongo custom class para poder modificar el width!
	            content: content
	        }).on('hide.bs.popover', function () {
	        	//marco mensajes no vistos como vistos
	        	if(noVistosIds.length > 0){
	    			$jQ.ajax({
		                "dataType": 'json',
		                "type": "PUT",
		                "url": $jQ("#applicationContextPath").val() + "/web/support/REST/registroNotificacion/marcarVistos/ids/" + noVistosIds,
		             	"contentType": 'application/json',
		                "success": function (request, status, error) {		                	
		                	if(request.statusCode == 'OK'){
		                		$jQ("#notificationsPopoverBadge").html("");
	                       	}else{
	                       		console.log(request.observaciones);	      
	                       	}
	                    },
	                    "error": function (request, status, error) {
	                   	    console.log(request.responseJSON.observaciones);
	                    }
			        });	
	        	}
	        })
		}); 
	});
</script>
<sec:authentication property="principal" var="user" />
<form action="${pageContext.servletContext.contextPath}/logout" method="post" hidden="true" name="logoutForm">
	<sec:csrfInput />
    <input type="submit" value="Logout" />
</form>
<div class="menu">
<nav class="navbar navbar-inverse navbar-default navbar-static-top">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <img src="${pageContext.servletContext.contextPath}/public/custom/images/${user.menuImagen}.png" style="height: 50px;background-color: #9d9d9d;"/><!-- mismo color de fondo que las letras del menu -->     
    </div>  
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <c:forEach items="${user.menu}" var="modulo">
      	<ul class="nav navbar-nav navbar-left"><!-- de esta forma se distribuyen bien los items cuando son muchos y no entran en una linea! (como esta antes se comportaba todo como uno solo) -->
	      	<li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-submenu role="button" aria-expanded="false">${modulo.value.iconSpan}<spring:message code="modulo.${modulo.value.name}"/><span class="caret"></span></a>
	          <ul class="dropdown-menu" role="menu">
	          <c:forEach items="${modulo.value.submenu}" var="clase">
	            <li class="dropdown-submenu">
	            	<a data-toggle="dropdown" data-submenu tabindex="0">${clase.value.iconSpan}<spring:message code="menu.${modulo.value.name}.${clase.value.name}" htmlEscape="false"/></a>
	            	<ul class="dropdown-menu">
	            		<c:forEach items="${clase.value.submenu}" var="action">
	            			<li><a href="${pageContext.servletContext.contextPath}${action.value.privilegio.link}" class="menu-waitfor">${action.value.iconSpan}<spring:message code="menu.${modulo.value.name}.${clase.value.name}${!empty action.value.privilegio.subClase?'.':''}${action.value.privilegio.subClase}.${action.value.privilegio.accion}" arguments="," htmlEscape="false"/></a></li>
	            		</c:forEach>
	            	</ul>
	            </li>
	          </c:forEach>
	          </ul>
	        </li>
	    </ul>
      </c:forEach>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#" id="notificationsPopover"><span class="glyphicon glyphicon-bell" aria-hidden="true"></span><span id="notificationsPopoverBadge" class="badge"></span></a></li>
        <li><a href="#" onclick="customComponents_mostrar_ayuda('${ayuda}')"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span></a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle glyphicon glyphicon-cog" data-toggle="dropdown" data-submenu role="button" aria-expanded="false" id="dropdownMenuConfiguracionUsuarioButton"></a>
          <ul class="dropdown-menu" role="menu">
            <li class="dropdown-header"><p>${user.descripcion}</p></li>
            <li class="dropdown-header"><p>${user.responsabilidad.codigo}</p></li>
            <li class="divider"></li>
            <li><a href="${pageContext.servletContext.contextPath}/web/SEGURIDAD/Usuario/EDITAR_2"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Configuracion de Usuario</a></li>           
            <li><a href="${pageContext.servletContext.contextPath}/web/EVENTOS/SuscripcionConfig/CONFIGURAR"><span class="glyphicon glyphicon-bell" aria-hidden="true"></span> Configuracion de Alertas</a></li>
            <li><a href="${pageContext.servletContext.contextPath}/web/SEGURIDAD/SELECCIONAR"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Cambiar Responsabilidad</a></li>	 
            <li class="divider"></li>
            <li><a href="javascript: document.logoutForm.submit();"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Salir</a></li><!-- spring security 4 cambio j_spring_security_logout -->
          </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
</div>