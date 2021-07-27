<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="body">
 	<tiles:importAttribute name="title"/>
	<h1>${title}</h1>
	<form:form  modelAttribute="entidadDTO" name="entidadDTO">
	    <table>
	    	<tr>
				<td>Seleccionar una opcion:</td>
				<td>
				    <form:select path="sucursalId" items="${entidades}" itemValue="id"/>
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="Siguiente"/></td>
			</tr>
	    </table>
	</form:form>
</div>