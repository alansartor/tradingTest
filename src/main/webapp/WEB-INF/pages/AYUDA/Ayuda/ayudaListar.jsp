<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/public/jquery-plugins/bonsai/css/jquery.bonsai.css">
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/bonsai/js/jquery.bonsai.js"></script>
<script src="${pageContext.servletContext.contextPath}/public/jquery-plugins/bonsai/js/jquery.qubit.js"></script>
<script type="text/javascript">
	var $jQ = jQuery.noConflict();
	var currentAyudaenum;
	$jQ(function() {
		var ayudaiInicialValor = $jQ("#ayudaInicial").val();
		currentAyudaenum = "";
		$jQ("#checkboxes").bonsai({
			expandAll: true,
			//addExpandAll: true, // add a link to expand all items
		    //checkboxes: true, // depends on jquery.qubit plugin
		    //handleDuplicateCheckboxes: false // optional
		});	
		//agrego boton previous and next
		$jQ('#panelFooter\\.AYUDA\\.Ayuda\\.LISTAR').append('<button type="button" id="buttonAyudaBackward" class="btn btn-default" name="buttonAyudaBackward"><span class="glyphicon glyphicon-step-backward"></span> Anterior</button>'+
				'<button type="button" id="buttonAyudaForward" class="btn btn-primary pull-right" name="buttonAyudaForward"><span class="glyphicon glyphicon-step-forward"></span> Siguiente</button>');
		
		$jQ('#buttonAyudaBackward').on('click', function() {//cuando modifican la semilla, se deben popular de nuevo los select de los productos y subproductos
			if(currentAyudaenum != ""){
				var count = $jQ("#"+currentAyudaenum).data('linkcount');
				var previusLink = $jQ("#checkboxes").find("a")[ count - 1 ];
				if(previusLink){
					mostrarAndPosicionarPanelAyuda($jQ(previusLink).data('ayudaid'));
				}
			}	
		});
		$jQ('#buttonAyudaForward').on('click', function() {//cuando modifican la semilla, se deben popular de nuevo los select de los productos y subproductos
			if(currentAyudaenum != ""){
				var count = $jQ("#"+currentAyudaenum).data('linkcount');
				var nextLink = $jQ("#checkboxes").find("a")[ count + 1 ];
				if(nextLink){
					mostrarAndPosicionarPanelAyuda($jQ(nextLink).data('ayudaid'));
				}
			}else{
				var nextLink = $jQ("#checkboxes").find("a")[ 0 ];//el primer link que aparezca
				mostrarAndPosicionarPanelAyuda($jQ(nextLink).data('ayudaid'));
			}
		});
		if(ayudaiInicialValor){
			mostrarAndPosicionarPanelAyuda(ayudaiInicialValor);
		}
  	});
	function mostrarPanelAyuda(ayudaEnum){
		currentAyudaenum = ayudaEnum;
		$jQ("#ayudaContenidoDiv").load($jQ("#applicationContextPath").val() + '/web/AYUDA/Ayuda/VER?ayuda='+ayudaEnum+'&popUpMode=true');
		$jQ("#selectedAyudaIndicador").remove();//borro la flecha de donde estaba, y la agrego a la nueva opcion seleccionada
		$jQ("#"+ayudaEnum).append('<span id="selectedAyudaIndicador" class="glyphicon glyphicon glyphicon-chevron-left"></span>');
	}
	function mostrarAndPosicionarPanelAyuda(ayudaEnum){
		if(ayudaEnum){
			var myBonsai = $jQ("#checkboxes").data("bonsai");
			mostrarPanelAyuda(ayudaEnum);
			myBonsai.expandTo(myBonsai.listItem(ayudaEnum));//$jQ("#"+ayudaiInicialValor).data('modulo'))
		}
	}
</script>
<div class="body">
    <tiles:importAttribute name="modulo"/>
    <tiles:importAttribute name="clase"/>
    <tiles:importAttribute name="accion"/>
    
    <input type="hidden" id="ayudaInicial" value="${ayuda}"/>
    <c:set var="count" value="-1" scope="page" />
	<div class="row">
		<div class="col-lg-5 col-md-4 col-sm-3 col-xs-2" style="border-right: 1px solid #dee2e6 !important;">	 
          <ol id="checkboxes">
	      	<c:forEach items="${ayudasDTOs}" var="modulo">
	      		<c:choose>
					<c:when test="${!empty modulo.value.ayuda}">
						<c:set var="count" value="${count + 1}" scope="page"/>
				      	<li data-linkCount="${count}"><a href="#" id="${modulo.value.ayuda}" data-ayudaid="${modulo.value.ayuda}" onclick="mostrarPanelAyuda('${modulo.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="modulo.${modulo.value.name}" htmlEscape="false"/></a>	
			              <ol>
				              <c:forEach items="${modulo.value.submenu}" var="clase">
					            <c:choose>
									<c:when test="${!empty clase.value.ayuda}">
										<c:set var="count" value="${count + 1}" scope="page"/>
							            <li id="${clase.value.ayuda}" data-linkCount="${count}"><a href="#" data-ayudaid="${clase.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${clase.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="menu.${modulo.value.name}.${clase.value.name}" htmlEscape="false"/></a>					            	
							            	<ol>
								            	<c:forEach items="${clase.value.submenu}" var="action">
								            		<c:choose>
														<c:when test="${!empty action.value.ayuda}">
															<c:set var="count" value="${count + 1}" scope="page"/>
										            		<li id="${action.value.ayuda}" data-linkCount="${count}"><a href="#" data-ayudaid="${action.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${action.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/></a>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>	  			
														</c:when>
														<c:otherwise>
										            		<li>
												            	<spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>										
														</c:otherwise>
													</c:choose>
									            </c:forEach>			
							                </ol>
							            </li>							
						            </c:when>
									<c:otherwise>
							            <li><spring:message code="menu.${modulo.value.name}.${clase.value.name}" htmlEscape="false"/>
							            	<ol>
								            	<c:forEach items="${clase.value.submenu}" var="action">
								            		<c:choose>
														<c:when test="${!empty action.value.ayuda}">
															<c:set var="count" value="${count + 1}" scope="page"/>
										            		<li id="${action.value.ayuda}" data-linkCount="${count}"><a href="#" data-ayudaid="${action.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${action.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/></a>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>	  			
														</c:when>
														<c:otherwise>
										            		<li><spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>										
														</c:otherwise>
													</c:choose>
									            </c:forEach>			
							                </ol>
							            </li>							
						            </c:otherwise>
								</c:choose>
					          </c:forEach>
			              </ol>
			            </li>						
				    </c:when>
					<c:otherwise>
				      	<li><spring:message code="modulo.${modulo.value.name}"/>
			              <ol>
				              <c:forEach items="${modulo.value.submenu}" var="clase">
					            <c:choose>
									<c:when test="${!empty clase.value.ayuda}">
										<c:set var="count" value="${count + 1}" scope="page"/>
							            <li><a href="#" id="${clase.value.ayuda}" data-ayudaid="${clase.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${clase.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="menu.${modulo.value.name}.${clase.value.name}" htmlEscape="false"/></a>					            	
							            	<ol>
								            	<c:forEach items="${clase.value.submenu}" var="action">
								            		<c:choose>
														<c:when test="${!empty action.value.ayuda}">
															<c:set var="count" value="${count + 1}" scope="page"/>
										            		<li data-linkCount="${count}"><a href="#" id="${action.value.ayuda}" data-ayudaid="${action.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${action.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/></a>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>	  			
														</c:when>
														<c:otherwise>
										            		<li>
												            	<spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>										
														</c:otherwise>
													</c:choose>
									            </c:forEach>			
							                </ol>
							            </li>							
						            </c:when>
									<c:otherwise>
							            <li><spring:message code="menu.${modulo.value.name}.${clase.value.name}" htmlEscape="false"/>
							            	<ol>
								            	<c:forEach items="${clase.value.submenu}" var="action">
								            		<c:choose>
														<c:when test="${!empty action.value.ayuda}">
															<c:set var="count" value="${count + 1}" scope="page"/>
										            		<li id="${action.value.ayuda}" data-linkCount="${count}"><a href="#" data-ayudaid="${action.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${action.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/></a>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>	  			
														</c:when>
														<c:otherwise>
										            		<li><spring:message code="menu.${modulo.value.name}.${clase.value.name}.${action.value.name}" arguments="," htmlEscape="false"/>
												            	<ol>
													            	<c:forEach items="${action.value.submenu}" var="field">
													            		<c:set var="count" value="${count + 1}" scope="page"/>
													            		<li id="${field.value.ayuda}" data-modulo="${modulo.value.name}" data-linkCount="${count}"><a href="#" data-ayudaid="${field.value.ayuda}" data-count="${count}" onclick="mostrarPanelAyuda('${field.value.ayuda}');"><span class="glyphicon glyphicon-question-sign"></span><spring:message code="ayuda.AYUDA.${field.value.ayuda}.VER" arguments="," htmlEscape="false"/></a></li>
																	</c:forEach>
																</ol>
											                </li>										
														</c:otherwise>
													</c:choose>
									            </c:forEach>			
							                </ol>
							            </li>							
						            </c:otherwise>
								</c:choose>
					          </c:forEach>
			              </ol>
			            </li>   		
	      			</c:otherwise>
				</c:choose>	      		
	      	</c:forEach>
      	  </ol>		
		</div>
		<div id="ayudaContenidoDiv" class="col-lg-7 col-md-8 col-sm-9 col-xs-10"></div>
	</div>
</div>