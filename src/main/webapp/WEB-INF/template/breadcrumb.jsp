<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:if test="${!empty breadcrumbs}">
<div class="container-fluid" style="background-color: #e9e9e9;padding: 0px 0px 0px 0px;"><!-- pongo tod el div del mismo color que class="breadcrumb" -->
<div class="col-lg-11 col-md-11 col-sm-11 col-xs-12" style="padding: 0px 0px 0px 0px;">
	<!-- 01/11/2018 le saque el javascript:window.location.href= al link, no me acuerdo para que lo puse, pero como me afecta para abrir los link en una nueva pestaña, entonces lo saco -->
	<ol class="breadcrumb" style="margin-bottom: 0px;">
		<c:forEach items="${breadcrumbs}" var="bread">
			<c:choose>
				<c:when test="${bread.active}"><li class="active"><spring:message code="menu.${bread.modulo}.${bread.clase}${!empty bread.subClase?'.':''}${bread.subClase}.${bread.accion}" arguments="${bread.idEntidad},," htmlEscape="false"/></li></c:when>
				<c:otherwise><li><a class="linkBreadcrumb" id="bread.${bread.modulo}.${bread.clase}.${bread.accion}" href="${pageContext.servletContext.contextPath}${bread.link}"><spring:message code="menu.${bread.modulo}.${bread.clase}${!empty bread.subClase?'.':''}${bread.subClase}.${bread.accion}" arguments="${bread.idEntidad},," htmlEscape="false"/></a></li></c:otherwise>
			</c:choose>
		</c:forEach>
	</ol>
</div>
	<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1" style="padding: 0px 0px 0px 0px;">
		<div class="btn-group pull-right" role="group" id="divDerechaBread" style="padding: 2px 7px;"><!-- para que se muestre alineado con el class="breadcrumb" -->
		</div>
	</div>
</div>
</c:if>