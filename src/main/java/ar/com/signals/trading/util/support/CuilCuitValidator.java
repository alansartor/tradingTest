package ar.com.signals.trading.util.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

public class CuilCuitValidator {
	/**
	 * Verifica si el prefijo del cuit corresponde a un cuit internacional
	 * @param cuit
	 * @return
	 */
	public static boolean esCuitCuilInternacional(String cuit){
		if(StringUtils.isNotEmpty(cuit) && cuit.length() > 2){
			if(cuit.substring(0, 2).compareTo("50") == 0 || 
			   cuit.substring(0, 2).compareTo("55") == 0){
				return true;
			}
		}
		return false;
	}

	public static boolean validateCuilCuit(String numeroIdentif, Errors errors, String field) {
		return CuilCuitValidator.validateCuilCuit(numeroIdentif, errors, field, false);
	}
	
	/**
	 *  Verifica que el cuit sea valido, si esta todo ok devuelve true, si algo esta mal devuelve false
	 *  y si errors es distinto de null tambien lo agrega a los errores
	 *	Numeros de prueba: 23-32223035-4 30-68151999-4
	 */
	public static boolean validateCuilCuit(String numeroIdentif, Errors errors, String field, boolean soportaGuiones) {
		try {
			CuilCuitValidator.validateCuilCuit(numeroIdentif, soportaGuiones);
			return true;
		} catch (Exception e) {
			if(errors != null){
				errors.rejectValue(field, "001", e.getMessage());
			}
			return false;
		}	
	}
	public static String cuilCuitError(String numeroIdentif, boolean soportaGuiones) {
		try {
			CuilCuitValidator.validateCuilCuit(numeroIdentif, soportaGuiones);			
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}
	public static void validateCuilCuit(String numeroIdentif, boolean soportaGuiones) throws Exception {
		if (StringUtils.isNotEmpty(numeroIdentif)) {
			int mod, digVerif;
			int suma = 0;
			int longitud = numeroIdentif.length();
			String patron = "5432765432";

			if(longitud < 11){
				throw new Exception("La longitud de Cuil/Cuit es incorrecta");
			}else if(longitud > 11){
				if(!soportaGuiones){
					throw new Exception("La longitud de Cuil/Cuit debe ser de 11 caracteres.");
				}else{
					if(longitud != 13){
						throw new Exception("La longitud de Cuil/Cuit es incorrecta");
					}else if(numeroIdentif.charAt(2) != '-' || numeroIdentif.charAt(11) != '-'){
						throw new Exception("El formato del Cuil/Cuit debe ser 'nn-nnnnnnnnn-n'");
					}else{
						numeroIdentif = numeroIdentif.replace("-", "");//Le saco los guiones para verificarlo con el algoritmo
					}
				}
			}

			//CuilCuitValidator.esCuitCuilInternacional se agrego para que acepte cuit internacional (cada pais tiene un cuit fijo)
			if (numeroIdentif.substring(0, 2).compareTo("20") == 0
					|| numeroIdentif.substring(0, 2).compareTo("23") == 0
					|| numeroIdentif.substring(0, 2).compareTo("24") == 0
					|| numeroIdentif.substring(0, 2).compareTo("27") == 0
					|| numeroIdentif.substring(0, 2).compareTo("30") == 0
					|| numeroIdentif.substring(0, 2).compareTo("33") == 0
					|| numeroIdentif.substring(0, 2).compareTo("34") == 0
					|| CuilCuitValidator.esCuitCuilInternacional(numeroIdentif)) {

				for (int i = 0; i <= 9; i++) {
					suma += Integer.parseInt(patron.substring(i, i + 1)) * Integer.parseInt(numeroIdentif.substring(i, i + 1));
				}

				mod = suma % 11;
				digVerif = 11 - mod;

				if (digVerif == 11) {
					digVerif = 0;
				} else if (digVerif == 10) {
					digVerif = 9;
				}

				if (digVerif != Integer.valueOf(numeroIdentif.substring(10, 11))) {
					throw new Exception("Fallo la verificacion: El Cuil/Cuit ingresado es invalido.");
				}
			}else{
				throw new Exception("Los 2 primeros digitos de Cuil/Cuit son erroneos.");
			}
		}else{
			throw new Exception("La longitud de Cuil/Cuit debe ser de 11 caracteres.");
		}
	}

	/**
	 * Algoritmo para verificacion de Rut de Uruguay
	 * No se usa mas, de ahora en mas a los choferes uruguayos
	 * se le solicitara la cedula de identidad que trae digito verificador 
	 * @param numeroIdentif
	 * @param errors
	 * @param field
	 */
	public static void validateRucRut(String numeroIdentif, Errors errors, String field){
		if (StringUtils.isNotEmpty(numeroIdentif)) {
			int mod, digVerif;
			int suma = 0;

			String patron = "43298765432";

			//Completo hasta 12 digitos
			while (numeroIdentif.length() < 12) {
				numeroIdentif = "0" + numeroIdentif;
			}

				// 2 Primeras Posiciones entre 01 y 21 
				try{
					String prefijo = numeroIdentif.substring(0, 2);
					int pref = new Integer(prefijo);
					if(pref < 1 || pref > 21)
						errors.rejectValue(field, "001"," Fallo la verificacion: El "+ field.toUpperCase()+ " ingresado es invalido. Primeros dos digitos debe estar entre 01 y 21");

	
					for (int i = 0; i <= 10; i++) {
						suma += Integer.parseInt(patron.substring(i, i + 1))
								* Integer.parseInt(numeroIdentif.substring(i,i + 1));
					}

					mod = suma % 11;
					digVerif = 11 - mod;

					if (digVerif == 11) {
						digVerif = 0;
					} else {
						//Si el resto es 10 es un ruc invalido
						if (digVerif == 10) 
							digVerif = -1;
					}

					if (digVerif != Integer.valueOf(numeroIdentif.substring(11,	12))) {
						errors.rejectValue(field, "001"," Fallo la verificacion: El "+ field.toUpperCase() + " ingresado es invalido.");
					}
				}catch (NumberFormatException e) {
					errors.rejectValue(field, "001"," Fallo la verificacion: El "+ field.toUpperCase()+ " ingresado es invalido. Los caracteres ingresados deben ser todos numericos");
				}
		}
	}
	
	/**
	 * En paraguay, la cedula de identidad consta de 7 digitos o pueden ser 6, y si a ese nro
	 * le agregamos el digito verificador modulo 11 entonces obtenemos el
	 * Registro Unico de Contribuyentes (RUC)
	 * 6 o 7 digitos + 1 digito verificador 
	 * ej: nnnnnnn-n
	 * @param numeroIdentif
	 * @param errors
	 * @param field
	 * @throws Exception 
	 */
	public static void validarRucParaguay(String numeroIdentif, Errors errors, String field) throws Exception{
		if (StringUtils.isNotEmpty(numeroIdentif)) {
			int mod, digVerif;
			int suma = 0;	
			String patron = "23456789";
			
			numeroIdentif = numeroIdentif.trim();
			int digitoVerificador = Integer.parseInt(numeroIdentif.substring(numeroIdentif.length(), numeroIdentif.length() + 1));
			numeroIdentif = numeroIdentif.substring(0, numeroIdentif.length());
			//Validar que tenga entre 7 y 8 caracteres
			if(numeroIdentif.length() != 7 && numeroIdentif.length() != 8){
				throw new Exception("Longitud incorrecta, debe ingresar los 6 o 7 digitos de la C�dula de Identidad de Paraguay y el digito adicional del RUC");
			}
			try{
				int index = 0;
				for (int i = 0; i < numeroIdentif.length(); i++) {
					index = numeroIdentif.length() - i;
					suma += Integer.parseInt(numeroIdentif.substring(index, index + 1)) * Integer.parseInt(patron.substring(i,i + 1));
				}

				mod = suma % 11;
				digVerif = 11 - mod;

				if (digVerif == 11) {
					digVerif = 0;
				} else {
					//Si el resto es 10 es invalido (salvo chile que acepta la letra k)
					if (digVerif == 10) 
						digVerif = -1;
				}

				if (digVerif != digitoVerificador) {
					errors.rejectValue(field, "001"," Fallo la verificacion del RUC, verifique el nro ingresado");
				}
			}catch (NumberFormatException e) {
				errors.rejectValue(field, "001"," Fallo la verificacion del RUC, verifique que los caracteres ingresados deben ser todos numericos y no deben haber espacios en blanco");
			}
		}
	}

	/**
	 * La cedula de identidad en Uruguay tiene 7 digitos + 1 digito verificador
	 * nnnnnnn-n
	 * @param cuil
	 * @param errors
	 * @param string
	 */
	public static void validarCiUruguay(String numeroIdentif, Errors errors, String field) {
		//Longitud 8
		if(numeroIdentif.length() != 8){
			errors.rejectValue(field, "001", "La Cedula de Identidad en Uruguay es un numero de 8 digitos");
			return;
		}else if(!StringUtils.isNumeric(numeroIdentif)){
			errors.rejectValue(field, "001", "La Cedula de Identidad en Uruguay deben ser todos numeros");
			return;
		}
		int mod, digVerif;
		int suma = 0;	
		String patron = "8123476";
		
		int digitoVerificador = Integer.parseInt(numeroIdentif.substring(numeroIdentif.length() -1, numeroIdentif.length()));
		numeroIdentif = numeroIdentif.substring(0, numeroIdentif.length() -1);			
		try{		
			for (int i = 0; i < numeroIdentif.length(); i++) {
				suma += Integer.parseInt(numeroIdentif.substring(i, i + 1)) * Integer.parseInt(patron.substring(i,i + 1));
			} 
			digVerif = suma % 10;
			if (digVerif == 10) {
				digVerif = 0;
			}
			if (digVerif != digitoVerificador) {
				errors.rejectValue(field, "001"," Fallo la verificacion de la Cedula de Identidad, verifique el numero ingresado");			
			}
		}catch (NumberFormatException e) {
			errors.rejectValue(field, "001"," Fallo la verificacion de la Cedula de Identidad, verifique que los caracteres ingresados deben ser todos numericos y no deben haber espacios en blanco");
		}
	}
	
	/**
	 * Rol Unico Nacional en Chile, tiene 8 digitos + 1 digito verificador (nro o la letra k)
	 * nnnnnnnn-c
	 * @param numeroIdentif
	 * @param errors
	 * @param field
	 */
	public static void validarRunChile(String numeroIdentif, Errors errors, String field) {
		//Longitud 9
		if(numeroIdentif.length() != 9){
			errors.rejectValue(field, "001", "El Rol Unico Nacional (RUN) en Chile son 8 numeros mas 1 numero o una letra");
			return;
		}
		String digitoVerificador = numeroIdentif.substring(numeroIdentif.length() -1, numeroIdentif.length());
		numeroIdentif = numeroIdentif.substring(0, numeroIdentif.length() - 1);	
		if(!StringUtils.isNumeric(numeroIdentif)){
			errors.rejectValue(field, "001", "Los primeros 8 digitos del Rol Unico Nacional (RUN) en Chile deben ser todos numeros");
			return;
		}
		int mod, digVerif;
		int suma = 0;	
		String patron = "23456723";
				
		try{
			int index = 0;
			for (int i = 0; i < numeroIdentif.length(); i++) {
				index = numeroIdentif.length() - i;
				suma += Integer.parseInt(numeroIdentif.substring(index -1, index)) * Integer.parseInt(patron.substring(i,i + 1));
			}

			mod = suma % 11;
			digVerif = 11 - mod;
			if (digVerif == 11) {
				digVerif = 0;
			}
			String verificador = digVerif == 10?"K":String.valueOf(digVerif);
			if (!verificador.equalsIgnoreCase(digitoVerificador)) {
				errors.rejectValue(field, "001"," Fallo la verificacion del Rol Unico Nacional (RUN), verifique el valor ingresado");
			}
		}catch (NumberFormatException e) {
			errors.rejectValue(field, "001"," Fallo la verificacion del Rol Unico Nacional (RUN), verifique que los caracteres ingresados sean correctos y no deben haber espacios en blanco");
		}
	}
/*	public static void main(String[] arg){
		new CuilCuitValidator().validateCuilCuit("50000003899", null, null);
		//new CuilCuitValidator().validateRucRut("021196542017", null, null);
		//new CuilCuitValidator().validateRucRut("212348520014", null, null);
		//new CuilCuitValidator().validateRucRut("020536210076", null, null);
		//new CuilCuitValidator().validateRucRut("210094030014", null, null);
	}*/

	/**
	 * Devuelve el cuil de un dni, depende de si es hombre o mujer, agrega la parte de adelante
	 * y hace el algoritmo inverso para obtener el digito verificador
	 * @param b
	 * @param trim
	 * @return
	 */
	public static String getCuil(boolean esHombre, String dni) {
		String cuil = (esHombre?"20":"23")+StringUtils.leftPad(dni, 8, '0');
		int mod, digVerif;
		int suma = 0;
		String patron = "5432765432";
		for (int i = 0; i <= 9; i++) {
			suma += Integer.parseInt(patron.substring(i, i + 1)) * Integer.parseInt(cuil.substring(i, i + 1));
		}

		mod = suma % 11;
		digVerif = 11 - mod;

		if (digVerif == 11) {
			digVerif = 0;
		} else if (digVerif == 10) {
			digVerif = 9;
		}
		return cuil+digVerif;
	}
}
