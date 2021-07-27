/**
 * Requiere las dependencias:
 * jquery.min.js
 * jquery-ui.min.js	
 * 
 * args{
 * 		success: es la funcion que se llama luego de guardar el sujeto
 * }
 * 
 * @param args
 * @returns
 */
function nuevoSujetoPopUp(args){
	//abrir dialogo alta cliente
	BootstrapDialog.show({
        type: BootstrapDialog.TYPE_PRIMARY,
        title: '<span class="fa fa-plus-square" aria-hidden="true"></span> ' + ((typeof args.titulo !== 'undefined')?args.titulo:'Nuevo Cliente/Proveedor'),
        message: $jQ('<div class="container-fluid"><div class="form-group form-group-sm"><label class="col-md-4 control-label" for="dialogClienteCuit">Cuit:</label><input id="dialogClienteCuit" class="form-control col-md-4"/></div><div class="form-group form-group-sm"><label class="col-md-4 control-label" for="dialogClienteDescripcion">Razon Social:</label><input id="dialogClienteDescripcion" readonly="true" class="form-control col-md-4"/></div></div>'),     
        onshown: function(dialogRef){
        	var posibleCuit = args.initialPotentialCuit;
        	if(typeof posibleCuit !== 'undefined'){
        		posibleCuit = posibleCuit.replace(new RegExp('-', 'g'), '');
        		if(!isNaN(posibleCuit)){
        			 $jQ("#dialogClienteCuit").val(posibleCuit);
        			 if(posibleCuit.length == 11){//si esta el cuit completo, entonces disparo el guardar
        				 var buttonDialogConfirm = dialogRef.getButton('btnDialogNuevoSujetoConfirm');
        				 buttonDialogConfirm.trigger('click');
        			 }
        		}
        	}
        },
        buttons: [{
            label: 'Cancelar',
            id: 'btnDialogNuevoSujetoCancel',
            action: function(dialog) {
            	dialog.close();
            }
        },{
            label: 'Guardar Cliente/Proveedor',
            id: 'btnDialogNuevoSujetoConfirm',
            action: function(dialog) {
            	var buttonNuevo = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
            	buttonNuevo.disable();
            	var buttonDialogCancelar = dialog.getButton('btnDialogNuevoSujetoCancel');
            	buttonDialogCancelar.disable();
            	dialog.setClosable(false);
            	buttonNuevo.spin();//se muestra un asterisco que gira para indicar que se esta haciendo algo
            	//validacion de cuit
            	var cuit = $jQ("#dialogClienteCuit").val();
            	if(cuit = "" || (cuit.length != 11 && cuit.length != 13)){
            		buttonNuevo.stopSpin();
            		alert("Ingrese el Cuit completo sin espacios");
            		buttonNuevo.enable();
            		buttonDialogCancelar.enable();
                  	dialog.setClosable(true);
            	}else{
            		if ($jQ("#dialogClienteDescripcion").prop('readonly')) {
            			//pruebo a guardar y que el sistema busque la razon social en afip
            			$jQ.ajax({
	   		                "dataType": 'json',
	   		                "type": "POST",
	   		                "url": $jQ("#applicationContextPath").val() + "/web/support/REST/nuevo/sujeto",
	   		             	"data": JSON.stringify({"cuit": $jQ("#dialogClienteCuit").val()}),//aca no pude usar las variables cuit y razonSocial, porque se traduce en un false
	   		             	"contentType": 'application/json',
	   		                "success": function (request, status, error) {
	                           	//viene un json con respuesto por bien o por mal
	                           	if(request.statusCode == 'OK'){
	                           		alert(request.observaciones);
	                               	dialog.close();
	                               	args.success(request.entidadDTO);
	                           	}else{
	                           		buttonNuevo.stopSpin();
	                           		if(request.statusCode == 'ERROR_SERVIDOR'){
	                           			//el sistema no pudo conectarse con afip, o afip no devolvio los datos correctamente, entonces habilito a que ingresen la razon social
		                           		$jQ("#dialogClienteDescripcion").prop('readonly',false);
		                           		alert(request.observaciones);
		                        		buttonNuevo.enable();
		                        		buttonDialogCancelar.enable();
		                              	dialog.setClosable(true);
	                           		}else{
	                           			//el usuario ingreso algun dato mal, se informa el error
		                           		alert(request.observaciones);
		                        		buttonNuevo.enable();
		                        		buttonDialogCancelar.enable();
		                              	dialog.setClosable(true);
	                           		}
	                           	}
                            },
                            "error": function (request, status, error) {
                            	//error inesperado
                            	buttonNuevo.stopSpin();
                           	    alert(request.responseJSON.observaciones);
                        		buttonNuevo.enable();
                        		buttonDialogCancelar.enable();
                              	dialog.setClosable(true);
                            }
	   		            });
            		}else{
            			//el sistema no pudo buscar la razon social en afip, que la ingrese a mano
            			var razonSocial = $jQ("#dialogClienteDescripcion").val();
            			if(razonSocial = "" || razonSocial.length < 3){
            				buttonNuevo.stopSpin();
            				alert("Ingrese la Razon Social del Cliente/Proveedor");
	                		buttonNuevo.enable();
	                		buttonDialogCancelar.enable();
                          	dialog.setClosable(true);
            			}else{
                			$jQ.ajax({
    	   		                "dataType": 'json',
    	   		                "type": "POST",
    	   		                "url": $jQ("#applicationContextPath").val() + "/web/support/REST/nuevo/sujeto",
    	   		             	"data": JSON.stringify({"cuit": $jQ("#dialogClienteCuit").val(), "descripcion": $jQ("#dialogClienteDescripcion").val()}),//aca no pude usar las variables cuit y razonSocial, porque se traduce en un false
    	   		             	"contentType": 'application/json',
    	   		                "success": function (request, status, error) {
    	                           	//viene un json con respuesto por bien o por mal
    	                           	if(request.statusCode == 'OK'){
    	                           		alert(request.observaciones);
    	                               	dialog.close();
    	                               	args.success(request.entidadDTO);
    	                           	}else{
    	                           		//el usuario ingreso algun dato mal, se informa el error
    	                           		buttonNuevo.enable();
    		                           	alert(request.observaciones);
    		                           	buttonNuevo.enable();
    			                		buttonDialogCancelar.enable();
    		                          	dialog.setClosable(true);
    	                           	}
                                },
                                "error": function (request, status, error) {
                                	//error inesperado
                            	    buttonNuevo.enable();
                               	    alert(request.responseJSON.observaciones);
                               	    buttonNuevo.enable();
                               	 	buttonDialogCancelar.enable();
                               		dialog.setClosable(true);
                                }
    	   		            });
            			}
            		}
            	}  	
            }
        }]
    });
}

/**
 * Requiere las dependencias:
 * jquery.min.js
 * jquery-ui.min.js	
 * 
 * args{
 * 		selectTipoOptions: son las opciones para popular el select
 * 		success: es la funcion que se llama luego de guardar el productoGenerico
 * }
 * 
 * @param args
 * @returns
 */
function nuevoProductoGenericoPopUp(args){
	//abrir dialogo alta productoGenerico
	BootstrapDialog.show({
        type: BootstrapDialog.TYPE_PRIMARY,
        title: '<span class="fa fa-plus-square" aria-hidden="true"></span> Nuevo Principio Activo/Especie',
        message:  $jQ('<div class="container-fluid"><div class="form-group form-group-sm"><label class="col-md-4 control-label" for="dialogProductoGenericoDescripcion">Descripcion:</label><input id="dialogProductoGenericoDescripcion" maxlength="60" class="form-control col-md-4"/></div><div class="form-group form-group-sm"><label class="col-md-4 control-label" for="dialogProductoGenericoTipo">Tipo:</label><select id="dialogProductoGenericoTipo" class="form-control col-md-4">' + args.selectTipoOptions + '</select></div></div>'),
        btnCancelLabel: 'Cancelar',
        btnOKLabel: 'Guardar Principio Activo/Especie',
        buttons: [{
            label: 'Cancelar',
            action: function(dialog) {
            	dialog.close();
            }
        },{
            label: 'Guardar Principio Activo/Especie',
            action: function(dialog) {
            	var buttonNuevo = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
            	//validacion de datos vacios
            	var descripcion = $jQ("#dialogProductoGenericoDescripcion").val();
            	var tipo = $jQ("#dialogProductoGenericoTipo").val();
            	if(descripcion == "" || tipo == ""){
            		alert("Debe ingresar la Descripcion y el Tipo");
            		buttonNuevo.enable();
            	}else{
        			$jQ.ajax({
   		                "dataType": 'json',
   		                "type": "POST",
   		                "url": $jQ("#applicationContextPath").val() + "/web/support/REST/nuevo/productoGenerico",
   		             	"data": JSON.stringify({"descripcion": $jQ("#dialogProductoGenericoDescripcion").val(), "tipo":$jQ("#dialogProductoGenericoTipo").val()}),
   		             	"contentType": 'application/json',
   		                "success": function (request, status, error) {
                           	//viene un json con respuesto por bien o por mal
                           	if(request.statusCode == 'OK'){
                           		alert(request.observaciones);
                               	dialog.close();
                               	args.success(request.entidadDTO);
                           	}else{
                           		//el usuario ingreso algun dato mal, se informa el error
                           		buttonNuevo.enable();
	                           	alert(request.observaciones);
                           	}
                        },
                        "error": function (request, status, error) {
                        	//error inesperado
                    	    buttonNuevo.enable();
                       	    alert(request.responseJSON.observaciones);
                        }
   		            });
            	}  	
            }
        }]
    });
}

/**
 * Requiere las dependencias:
 * bootstrap-notify.min.js (esta en el mainTemplate)	
 * 
 * Esta variable se crea cada vez que una jsp carga el script
 * Se usa para guardar advertencias que solo se quieren mostrar una vez por pantalla, entonces cada vez que se llama al metodo
 * customComponents_advertir_la_primera_vez, la funcion primero verificara el map, si no contiene el id de la advertencia que
 * se quiere mostrar, entonces muestra una notify por unos segundos
 */
var customComponents_advertenciasMap = new Map();
function customComponents_advertir_la_primera_vez(idAdvertencia, msj, tipoNotificacion){
	if(typeof customComponents_advertenciasMap.get(idAdvertencia) === "undefined"){
		customComponents_advertenciasMap.set(idAdvertencia, "Ya se advirtio una vez, la proxima no se volvera a advertir");
		customComponents_mostrar_notificacion(msj, tipoNotificacion);
	}
}

/**
 * Para abrir una notificacion por algunos segundos
 * @param msj
 * @param tipoNotificacion {success, warning, danger, info}
 * @returns
 */
function customComponents_mostrar_notificacion(msj, tipoNotificacion, delay){
	if(typeof tipoNotificacion === "undefined" || tipoNotificacion == ""){
		tipoNotificacion = "info";
	}
	if(typeof delay === "undefined" || delay == ""){
		delay = 7000;
	}
	$jQ.notify({
		message: msj,			
		icon: 'glyphicon glyphicon-' + ((tipoNotificacion=='success') ? 'ok-sign' : ((tipoNotificacion=='warning') ? 'warning-sign' : ((tipoNotificacion=='danger') ? 'ban-circle' : 'info-sign')))
	}, {
		newest_on_top: true,
		type: tipoNotificacion,
		allow_dismiss: true,
		placement: {
			from: 'top',
			align: 'center'
		},
		animate: {
			enter: 'animated fadeInDown',
			exit: 'animated fadeOutUp'
		},
		delay: delay,
		icon_type: 'class'
	});
}

/**
 * Requiere las dependencias:
 * jquery.min.js
 * jquery-ui.min.js	
 * bootstrap-dialog.min.js (esta en el mainTemplate)
 * bootstrap-notify.min.js (esta en el mainTemplate)
 *  
 * Se debe llamar desde una pantalla armada con listarTemplate ya que hace referencia a tabla: #tablaEntidades
 * @param idEliminar
 * @param rowIndex
 * @param codigo
 * @returns
 */
function eliminarEntidadDeTabla(msg, rowId, partialUrlEliminar, clase){
	BootstrapDialog.show({
        type: BootstrapDialog.TYPE_DANGER,
        title: 'Eliminar ' + clase,
        message: '¿Esta seguro que desea Eliminar ' + msg + '?',     
        buttons: [{
            label: 'Cancelar',
            id: 'btnDialogEliminarEntidadCancel',
            action: function(dialog) {
            	dialog.close();
            }
        },{
            label: 'Eliminar ' + clase,
            id: 'btnDialogEliminarEntidadConfirm',
            action: function(dialog) {
            	var buttonEliminar = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
            	buttonEliminar.disable();
            	var buttonDialogCancelar = dialog.getButton('btnDialogEliminarEntidadCancel');
            	buttonDialogCancelar.disable();
            	dialog.setClosable(false);
            	buttonEliminar.spin();//se muestra un asterisco que gira para indicar que se esta haciendo algo
            	//Llame al rest para eliminar la entidad
        		$jQ.ajax({
                    "dataType": 'json',
                    "type": "DELETE",
                    "url": partialUrlEliminar,
                 	"contentType": 'application/json',
                    "success": function (request, status, error) {
                       	//viene un json con respuesto por bien o por mal
                       	if(request.statusCode == 'OK'){
                       		//elimino la fila
                       		var table = $jQ('#tablaEntidades').DataTable();
                        	//NEW Si se eliminan mas de una fila sin refrescar la pantalla, entonces el rowIndex queda mal, por lo que empezamos a usar el id para encontrar la fila!
                       		//para ello hay que ir a todas las pantallas donde se pueden eliminar registros y definir la variable 'var varRowId = "id";' con la variable que DataTable debe usar como id de la fila!
                       		var row = table.row('#'+rowId);
                        	//var row = table.row(rowIndex);
                        	dialog.close();//primero cierro y luego redibujo, para que se note que desaparece el registro
                        	row.remove().draw();                      	
                        	//mostrar notificacion de exito
        					$jQ.notify({
        						message: request.observaciones,			
        						icon: 'glyphicon glyphicon-ok-sign'
        					}, {
        						newest_on_top: true,
        						type: 'success',
        						allow_dismiss: true,
        						placement: {
        							from: 'top',
        							align: 'center'
        						},
        						animate: {
        							enter: 'animated fadeInDown',
        							exit: 'animated fadeOutUp'
        						},
        						delay: 10000,
        						icon_type: 'class'
        					});
                       	}else{
                       		buttonEliminar.stopSpin();
                           	alert(request.observaciones);
                           	buttonEliminar.enable();
                    		buttonDialogCancelar.enable();
                          	dialog.setClosable(true);
                       	}
                    },
                    "error": function (request, status, error) {
                    	//error inesperado
                    	buttonEliminar.stopSpin();
                   	    alert(request.responseJSON.observaciones);
                		buttonEliminar.enable();
                		buttonDialogCancelar.enable();
                      	dialog.setClosable(true);
                    }
                });
            }
        }]
    });
}
function customComponents_mostrar_ayuda(ayudaEnum){
	BootstrapDialog.show({
        type: BootstrapDialog.TYPE_DEFAULT,
        //size: BootstrapDialog.SIZE_WIDE,
        cssClass: 'custom-modal-dialog-lg',
        title: '<div id="ayudaDialogTitleDiv"><span class="glyphicon glyphicon-question-sign text-primary" aria-hidden="true"></span></div>',
        message : $jQ('<div class="embed-responsive custom-embed-dialog-lg"><iframe id="ayudaPopUpFrame" name="ayudaPopUpFrame" class="embed-responsive-item"></iframe></div>'),//scrolling="no" style="overflow:hidden;" quice sacar el scroll al iframe y hacer el scroll en los div de la pagina, pero la pagina no respeta el heigth del iframe
		onshow: function(dialogRef){//antes que se muestre el contenido del dialog
			//cuando cancelan en el ifram, se manda un mensaje, entonces se cierra el dialog
			var receiveMessage = function(event) {
				dialogRef.close();
			}
			window.addEventListener("message", receiveMessage, true);
		},
		onshown: function(dialogRef){//luego que se crea el contenido del dialog (message)
			//una ves que se crea el iframe, ahi le atcho el load event y le asigno el src para que se dispare!
			//si al iframe ya lo defino de entrada con el src, entonces no alcanzo a capturar el evento load del iframe
			$jQ('#ayudaPopUpFrame').load(function() {
				//Cuando cargo el iframe general, es decir el navegador de ayuda, entonces muevo el titulo del iframe al boostrapDialog
				//tener cuidado de no llevar el titulo de la ayuda actual
				var titulo = $jQ(this).contents().find('#ayudaTemplateTitleText').text();//el titulo esta dentro del iframe
				$jQ('#ayudaDialogTitleDiv').append(titulo);//pongo el titulo en el dialog
				$jQ(this).contents().find('#ayudaTemplateTitleDiv').hide();//pensaba que desde aca no podia modificar atributos de elementos del iframe (ya que el iframe usa otro contexto) pero si me dejo (quiza porque es del mismo dominio!)
		    });
			$jQ('#ayudaPopUpFrame').attr('src', $jQ("#applicationContextPath").val() + '/web/AYUDA/Ayuda/LISTAR?ayuda='+ayudaEnum+'&popUpMode=true');
        }
    });
}