<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
 	var $jQ = jQuery.noConflict();
 	$jQ(function () {
 		$jQ("#form").on('submit', function (e) {
 			if (e.isDefaultPrevented()) {
				// handle the invalid form...
			} else {
				// everything looks good!
				//waitingDialog.show('Aguarde', {dialogSize: 'sm'});saco que queda feo y reemplazo por un asterisco girando y deshabilitando los inputs
				$jQ("#buttonLogin").attr('disabled', true).prepend('<span class="fas fa-spinner custom-icon-spin" aria-hidden="true"></span>');
				$jQ("#username").attr('readonly', true);//si pongo disabled no se mandan en el submit
				$jQ("#password").attr('readonly', true);//si pongo disabled no se mandan en el submit
			}
 		});
	});
</script>
<div class="center-block" style="width:300px;">
	<div class="panel panel-default">
	  <div class="panel-heading"><span class="glyphicon glyphicon-lock"></span> Login</div>
	  <div class="panel-body">
		<form class="form-signin" id="form" action="<c:url value='/login'/>" method="POST"><!-- spring 4 cambio el el action, antes era /j_spring_security_check -->
			<input type="hidden" id="user_target_url" name="user_target_url" value="${user_target_url}"/>
		    <c:if test="${'fail' eq param.auth}">
		        <div><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/></div>
		    </c:if>
		    <c:if test="${not empty param.out}">
		        <div>Usted ha cerrado sesion correctamente</div>
		    </c:if>
		    <c:if test="${not empty param.time}">
		        <div>La sesion ha expirado por inactividad</div>
		    </c:if>					
			<div class="form-group">
	        	<div class="input-group">
	                <span class="input-group-addon"><i class="fas fa-user"></i></span>
	                <input type="text" class="form-control" id="username" name="username" placeholder="Usuario" required autofocus>				
	            </div>
	        </div>					
			<div class="form-group">
	            <div class="input-group">
	                <span class="input-group-addon"><i class="fas fa-lock"></i></span>
	                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>				
	            </div>
	        </div>					
			<div class="form-group">
	            <button type="submit" id="buttonLogin" class="btn btn-primary login-btn btn-block">Ingresar</button>
	        </div>
		</form>
	  </div>
	</div>		
</div>
<style>
body {
  padding-top: 40px;	
  background: url("${pageContext.servletContext.contextPath}/public/custom/images/loginBackground.jpg") no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.form-group {
    margin-bottom: 15px;
}
</style>