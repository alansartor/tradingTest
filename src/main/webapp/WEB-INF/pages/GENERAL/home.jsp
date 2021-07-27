<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication property="principal.menuFavoritos" var="userModuloList" />
<div class="body">
	<c:if test="${! empty userModuloList}"><h3>Favoritos</h3></c:if>
	<c:forEach items="${userModuloList}" var="modulo">
		<div class="list-group col-md-6" >
			<a href="#" class="list-group-item active"><spring:message code="modulo.${modulo.value.name}"/></a>
			<c:forEach items="${modulo.value.submenu}" var="action">
				<a href="${pageContext.servletContext.contextPath}${action.value.privilegio.link}${action.value.argumentos}" class="list-group-item"><spring:message code="menu.${modulo.value.name}.${action.value.privilegio.clase}.${action.value.privilegio.accion}" arguments="${action.value.privilegio.subClase}," htmlEscape="false"/> ${action.value.nombreExtendido}</a>
			</c:forEach>
		</div>
	</c:forEach>
</div>