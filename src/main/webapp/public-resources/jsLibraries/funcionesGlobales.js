function teclaEnterPresionada(e){
	tecla = (document.all) ? e.keyCode : e.which;
	return tecla==13;
}
function reemplazarPuntoPorComa(e){
	if(document.all){
		if(e.keyCode==46){
			e.keyCode=44;
		}
	}else{
		if(e.which==46){
			e.which=44;
		}
	}
}
function IsJsonString(json){
    var str = json.toString();
    try{
        JSON.parse(str);
    }catch (e){
        return false;
    }
    return true;
}
/**
 * Ver de cambiarle el nombre a todas las funciones, que empiecen con fg_ (funciones globales) para que no interfieran con otras librerias
 * @param number
 * @param decimals
 * @returns
 */
function fg_roundDown(number, decimals){
	var stringValue = number.toString();
	var decimalPart = stringValue.split('.')[1];
	var length = 0;
	if(decimalPart){
		length = decimalPart.length;
	}
	if(length && length > decimals){
		//toFixed hace round up, entonces le agrego le saco una centesima para que redondee para abajo, roundDown(1.55) = roundUp(1.55 - 0.01)
		number = number - (1/Math.pow(10, length));
	}
	return number.toFixed(decimals);
}

/**
 * devuelve true solo si son digitos, sin signo y sin punto decimal
 * @returns
 */
function fg_isInteger(texto){return /^\d+$/.test(texto);}