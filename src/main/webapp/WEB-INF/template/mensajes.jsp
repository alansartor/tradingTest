<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Mensajes de success, info, warning, danger -->
<c:if test="${!empty flashMensajes}">
	<c:forEach var="flashMensaje" items="${flashMensajes}">
		<div class="alert alert-${flashMensaje.tipo} alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>${flashMensaje.mensaje}</div>					
	</c:forEach>
</c:if>
<c:if test="${!empty flashNotificaciones}">
	<c:forEach var="flashNotificacion" items="${flashNotificaciones}">
		<div style="display: none;" class="divNotificacionConstructor" title="${flashNotificacion.tipo}" data-delay="${flashNotificacion.delay}" data-placement-align="${flashNotificacion.placementAlign}">${flashNotificacion.mensaje}</div>					
	</c:forEach>
</c:if>