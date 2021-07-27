 /**
 *
 * jquery.binarytransport.js
 * 
 * //NEW 07/02/2019 CUSTOM, se agregaron lineas al archivo original, para poder extraer datos json cuando ocurre una excepcion desde //cambiar todos por observaciones
 *
 * @description. jQuery ajax transport for making binary data type requests.
 * @version 1.0
 * @author Henry Algus <henryalgus@gmail.com>
 *
 */

(function($, undefined) {
    "use strict";

    // use this transport for "binary" data type
    $.ajaxTransport("+binary", function(options, originalOptions, jqXHR) {
        // check for conditions and support for blob / arraybuffer response type
        if (window.FormData && ((options.dataType && (options.dataType == 'binary')) || (options.data && ((window.ArrayBuffer && options.data instanceof ArrayBuffer) || (window.Blob && options.data instanceof Blob))))) {
            return {
                // create new XMLHttpRequest
                send: function(headers, callback) {
                    // setup all variables
                    var xhr = new XMLHttpRequest(),
                        url = options.url,
                        type = options.type,
                        async = options.async || true,
                        // blob or arraybuffer. Default is blob
                        dataType = options.responseType || "blob",
                        data = options.data || null,
                        username = options.username || null,
                        password = options.password || null;

                    xhr.addEventListener('load', function() {
                    	//este metodo se llama siempre que haya una respuesta del servidor, ya sea correcto o con error
                        var data = {};
                        if(xhr.status == 200) {
                        	data[options.dataType] = xhr.response;//aca hay que devolver si o si binary, no blob, sino jquery.ajax arroja error: no conversion from blob to binary
                        }else{
                            data[xhr.responseType] = xhr.response;//aca se devuleve json
                        }
                        // make callback and send data
                        callback(xhr.status, xhr.statusText, data, xhr.getAllResponseHeaders());
                    });
                    xhr.addEventListener('error', function() {
                    	//07/02/2019 este metodo solo se llama cuando hay un error de red, de conexion con el servidor
                        var data = {};
                        data[options.dataType] = xhr.response;
                        // make callback and send data
                        callback(xhr.status, xhr.statusText, data, xhr.getAllResponseHeaders());
                    });
                    //NEW 07/02/2019 CUSTOM, para poder extraer datos json cuando ocurre una excepcion desde //cambiar todos por observaciones
                    xhr.onreadystatechange = function() {
                        if(xhr.readyState == 4) {
                            //if(xhr.status == 200) {
                                //console.log(typeof xhr.response); // should be a blob
                            //} else if(xhr.responseJSON != "") {
                                //console.log(xhr.responseJSON);
                                //console.log(xhr.response);
                            //}
                        } else if(xhr.readyState == 2) {
                            if(xhr.status == 200) {
                            	xhr.responseType = "blob";
                            } else {
                            	xhr.responseType = "json";
                            }
                        }
                    };
                    xhr.open(type, url, async, username, password);

                    // setup custom headers
                    for (var i in headers) {
                        xhr.setRequestHeader(i, headers[i]);
                    }

                    xhr.responseType = dataType;
                    xhr.send(data);
                },
                abort: function() {}
            };
        }
    });
})(window.jQuery);