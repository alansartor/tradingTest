package ar.com.signals.trading.support.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.objects.User;

import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.service.RespoSrv;
import ar.com.signals.trading.seguridad.service.RespoUsuarioSrv;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.telegram.support.MyTelegramBotSrvImpl;
import ar.com.signals.trading.util.web.ServidorUtil;

/**
 * EndPoints sin seguridad para dar soporte a las jsp
 * Se utilizaria para reemplazar a DWR
 * NO Tienen Seguridad!
 * @author pepe@hotmail.com
 *
 */
@RestController
@RequestMapping(value = "/api/support/REST")
public class SupportPublicRestController {
	@Resource private ServidorUtil servidorUtil;
	@Resource private UsuarioSrv usuarioSrv;
	@Resource private RespoSrv respoSrv;
	@Resource private RespoUsuarioSrv respoUsuarioSrv;

	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	@Resource(name = "tradingBotSrvImpl") private MyTelegramBotSrvImpl tradingBotSrvImpl;
	
	private static Logger logger = LoggerFactory.getLogger(SupportPublicRestController.class);
	
	@RequestMapping( value = "/inicializacion", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String initialLoad() throws Exception{
		String rta = "";
		//uso este metodo para hacer inicializaciones, tanto en test como en produccion
		//*** creacion de usuario inicial ***
		Usuario usuario = usuarioSrv.obtenerPorUsername("Pepe");
		if(usuario == null) {//solo ejecutar el siguiente codigo si el sujeto Pepe no existe
			usuario = new Usuario();
			usuario.setUsername("Pepe");
			usuario.setUser_enabled(true);
			usuario.setDescripcion("Pepe ser");
			usuario.setUser_email("pepe@hotmail.com");
			usuario.setCreation_date(new Timestamp(new Date().getTime()));
			usuario.setLast_update_date(usuario.getCreation_date());
			usuarioSrv.persistir(usuario, "1234");
			Respo respo = new Respo();
			//respo.setId(-1l);esto causa error, hace que hibernate haga un update en vez de un insert del usuario
			respo.setCodigo("superUsuario");
			respo.setDescripcion("Super usuario que tiene acceso a la administracion general de Responsabilidades y Usuarios");
			respo.setTipo(TipoResponsabilidad.SuperUsuario);
			respo.setCreation_date(usuario.getCreation_date());
			respo.setLast_update_date(usuario.getCreation_date());
			respo.setActivo(true);
			List<Privilegio> privilegios = new ArrayList<>();
			privilegios.add(Privilegio.USUARIO_NUEVO);
			privilegios.add(Privilegio.USUARIO_EDITAR);
			privilegios.add(Privilegio.USUARIO_LISTAR);
			privilegios.add(Privilegio.RESPONSABILIDAD_NUEVO);
			privilegios.add(Privilegio.RESPONSABILIDAD_EDITAR);
			privilegios.add(Privilegio.RESPONSABILIDAD_LISTAR);
			privilegios.add(Privilegio.RESPONSABILIDAD_VER);
			privilegios.add(Privilegio.RESPOUSUARIO_NUEVO);
			privilegios.add(Privilegio.RESPOUSUARIO_EDITAR);
			privilegios.add(Privilegio.RESPOUSUARIO_LISTAR);
			respo.setPrivilegios(privilegios);
			respoSrv.persistir(respo);
			RespoUsuario ru = new RespoUsuario();
			ru.setRespo(respo);
			ru.setUsuario(usuario);
			ru.setCreation_date(usuario.getCreation_date());
			ru.setLast_update_date(usuario.getCreation_date());
			ru.setActivo(true);
			respoUsuarioSrv.persistir(ru);
			rta += "Usuario "+usuario.getUsername() +" creado \n";
		}else {
			rta += "No se crea Usuario, ya existe \n";
		}
		return rta;
	}
	

	/**
	 * Temporal, para probar a comunicarme con el que cree en Telegram
	 * @param produccionesId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/testTelegram"}, method = RequestMethod.GET)
    @ResponseBody
	public User testTelegram() throws Exception {
		return tradingBotSrvImpl.execute(new GetMe());
	}
	
	/**
	 * Temporal, para setear configuracion basica del Bot (descripcion y comandos)
	 * @param produccionesId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/inicializarBotTelegram"}, method = RequestMethod.GET)
    @ResponseBody
	public String inicializarBotTelegram() throws Exception {
		tradingBotSrvImpl.actualizarComandos();
		return "Bot configurado/actualizado con exito";
		
	}
}
