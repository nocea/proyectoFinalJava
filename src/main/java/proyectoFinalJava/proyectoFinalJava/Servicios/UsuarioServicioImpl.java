package proyectoFinalJava.proyectoFinalJava.Servicios;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Token;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.TokenRepositorio;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
@Service
public class UsuarioServicioImpl implements UsuarioServicio {
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		inicializarUsuarioAdmin();
	}
	@Autowired
    private UsuarioRepositorio usuarioRepositorio;
	@Autowired
    private TokenRepositorio tokenRepositorio;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Override
	public Usuario usuarioDTOaUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuarioDAO=new Usuario(usuarioDTO.getNombreCompleto_usuario(),
				usuarioDTO.getEmail_usuario(), usuarioDTO.getAlias_usuario(), usuarioDTO.getMovil_usuario(),
				usuarioDTO.getPasswd_usuario(), "ROLE_USUARIO",false);
		return usuarioDAO;
	}
	@Override
	public Boolean registrar(UsuarioDTO usuarioDTO) {
		Usuario usuarioDAO=usuarioDTOaUsuario(usuarioDTO);
		Usuario usuComprobar=usuarioRepositorio.findFirstByEmailUsuario(usuarioDAO.getEmailUsuario());
		if(usuComprobar!=null) {
			return false;
		}else {
			usuarioDAO.setPasswd_usuario(passwordEncoder.encode(usuarioDTO.getPasswd_usuario()));
		usuarioDAO = usuarioRepositorio.save(usuarioDAO);
		return true;
		}
		
	}
	@Override
	public UsuarioDTO convertirUsuarioADTO(Usuario usuario) {
	    UsuarioDTO usuarioDTO = new UsuarioDTO();
	    usuarioDTO.setId_usuario(usuario.getId_usuario());
	    usuarioDTO.setNombreCompleto_usuario(usuario.getNombreCompletoUsuario());
	    usuarioDTO.setEmail_usuario(usuario.getEmailUsuario());
	    usuarioDTO.setAlias_usuario(usuario.getAlias_usuario());
	    usuarioDTO.setMovil_usuario(usuario.getMovil_usuario());
	    usuarioDTO.setRol_usuario(usuario.getRol_usuario());
	    usuarioDTO.setPasswd_usuario(usuario.getPasswd_usuario());
	    usuarioDTO.setRol_usuario(usuario.getRol_usuario());
	    // Asignar otros campos según sea necesario
	    
	    return usuarioDTO;
	}
	private void inicializarUsuarioAdmin() {	
		if (!usuarioRepositorio.existsByNombreCompletoUsuario("admin")) {
			Usuario admin = new Usuario();
			admin.setNombreCompletoUsuario("admin");
			admin.setPasswd_usuario(passwordEncoder.encode("Admin1234"));
			admin.setAlias_usuario(null);
			admin.setEmailUsuario("admin@admin");
			admin.setRol_usuario("ROLE_ADMIN");
			admin.setRegistrado(true);
			usuarioRepositorio.save(admin);
		}
	}
	@Override
	public String generarToken() {
		UUID uuid = UUID.randomUUID();
        String cadena_token = uuid.toString();
        Calendar fechafinToken = Calendar.getInstance();
        fechafinToken.setTimeInMillis(System.currentTimeMillis());
        fechafinToken.add(Calendar.HOUR_OF_DAY, 24);
        System.out.println(fechafinToken);
        Token tokenNuevo=new Token(cadena_token,fechafinToken);
        tokenRepositorio.save(tokenNuevo);
		return cadena_token;
	}
	@Autowired
	private JavaMailSender javaMailSender;
	@Override
	public void EnviarEmailRecuperar(String email, String token) {
		try {
		MimeMessage mensaje = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
		}catch(Exception ex) {
			System.out.println("fallo mail");
		}
	}
	

}
