<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
    <tiles:importAttribute name="subClase" ignore="true"/>
    <tiles:importAttribute name="accion"/>
    <title><spring:message code="menu.${modulo}.${clase}${!empty subClase?'.':''}${subClase}.${accion}" arguments=",," htmlEscape="false"/></title>
    <link rel="shortcut icon" href="${pageContext.servletContext.contextPath}/public/custom/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/bootstrap/3.3.7/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-dialog.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-submenu/dist/css/bootstrap-submenu.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/font-awesome/5.14.0-web/css/fontawesome.min.css"><!-- hay que incluir el estylo de iconos que vamos a usar, la verison free solo podemos usar los solid (fas) y algunos regulares (far) -->
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/font-awesome/5.14.0-web/css/all.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/animate.min.css">
	<!-- Poner debajo de la hoja de estilo de bootstrap, para que tenga mayor prioridad -->
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/public/custom/css/customGlobalStyle.css?v=1.12"/>
    <style type="text/css">
		body {
	    	/* The fixed navbar will overlay your other content, unless you add padding to the top of the <body> */
	        /* padding-top:50px esto se usaba cuando el navbar era navbar-fixed-top*/
	        background-color: #f0f0f0;
		}
		#contenedorDiv{
			padding: 5px 5px 5px 5px;
		}
    </style>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <!-- Va arriba por si las sub jsp usan otros js plugins que tienen como dependencia jquery -->
    <script src="${pageContext.servletContext.contextPath}/public/jquery/jquery-2.1.1.min.js"></script>
    <!-- Lo pongo arriba, porque hay algunos plugins que se utilizan en algunas paginas que requieren que bootstrap.js este cargado antes (ej: bootstrap-select.js) -->
    <script src="${pageContext.servletContext.contextPath}/public/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<input type="hidden" id="applicationContextPath" value="${pageContext.servletContext.contextPath}" /> 
    <div class="page">
        <tiles:insertAttribute name="menu" ignore="true"/>
        <tiles:insertAttribute name="breadcrumb" ignore="true"/> 
        <div class="content" id=contenedorDiv>
        	<tiles:insertAttribute name="mensajes" />
        	<tiles:insertAttribute name="genericBody" />           
        </div>
    </div>
</body>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-submenu/dist/js/bootstrap-submenu.min.js" defer></script>
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-dialog.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-waitingfor.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/bootstrap-plugins/bootstrap-notify.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/funcionesGlobales.js?v=1"></script>
<script src="${pageContext.servletContext.contextPath}/public/jsLibraries/customComponents.js?v=3.1"></script>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
	$jQ().ready(function () {		
		$jQ('[data-submenu]').submenupicker();//$jQ('.dropdown-submenu > a').submenupicker();
 		$jQ('.menu-waitfor').on('click', function(){
 			waitingDialog.show('Aguarde', {dialogSize: 'sm'});
		});
 		$jQ('[data-toggle="tooltip"]').tooltip({trigger: 'hover'});//NEW 22/07/2020 cambie de focus a hover, asi puedo mostrar mejor en dropdowns y buttons, sino se mostraba cuando ahcian click!
 		/* Muestra notificaciones, son mensajes que aparecen por unos segundos */
		$jQ( ".divNotificacionConstructor" ).each(function( index ) {
			var tipoNotificacion = this.getAttribute("title");
			var customDelay = $jQ(this).data('delay');//por defecto viene un 7000
			var customPlacementAlign = $jQ(this).data('placement-align');
			$jQ.notify({
				message: $jQ(this).html(),			
				icon: 'glyphicon glyphicon-' + ((tipoNotificacion=='success') ? 'ok-sign' : ((tipoNotificacion=='warning') ? 'warning-sign' : ((tipoNotificacion=='danger') ? 'ban-circle' : 'info-sign')))
			}, {
				newest_on_top: true,
				type: tipoNotificacion,
				allow_dismiss: true,
				placement: {
					from: 'top',
					align: customPlacementAlign
				},
				animate: {
					enter: 'animated fadeInDown',
					exit: 'animated fadeOutUp'
				},
				delay: customDelay,
				icon_type: 'class'
			});
		});		
	});
</script>
</html>