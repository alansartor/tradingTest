<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="form-group form-group-sm">
	<div class="col-lg-6 col-md-8 col-sm-12 col-xs-12">	
		<label class="col-xs-12 col-sm-5 col-md-5 col-lg-5 control-label" for="fltSujetoDescripcion">Campo</label>  
		<div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
			<select id="fltSucursalId" class="form-control selectpicker" onkeypress="if(teclaEnterPresionada(event)){$jQ('#buttonBuscar').trigger('click');}">
		   		<option value="">-Todos-</option>
				<c:forEach items="${sucursalesDTO}" var="sucu">
					<option value="${sucu.id}" ${sucu.id == sucursalSelectedId ? 'selected' : ''}>${sucu.codigo}</option>
				</c:forEach>
			</select>
  		</div>
	</div>
</div>
