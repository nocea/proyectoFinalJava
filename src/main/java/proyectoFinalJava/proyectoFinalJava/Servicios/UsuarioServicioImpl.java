package proyectoFinalJava.proyectoFinalJava.Servicios;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
	/**
	 * método para pasar de dto a dao
	 */
	@Override
	public Usuario usuarioDTOaUsuario(UsuarioDTO usuarioDTO) {
		String imagePath = "src/main/resources/static/fotoInicial.png";
		byte[] imagen_usuario = convertImageToByteArray(imagePath);
		Usuario usuarioDAO=new Usuario(usuarioDTO.getNombreCompleto_usuario(),
				usuarioDTO.getEmail_usuario(), usuarioDTO.getAlias_usuario(), usuarioDTO.getMovil_usuario(),
				usuarioDTO.getPasswd_usuario(), "ROLE_USUARIO",false,imagen_usuario);
		return usuarioDAO;
	}
	/**
	 * metodo para convertir un string de una imagen a array
	 * @param imagePath
	 * @return
	 */
	private static byte[] convertImageToByteArray(String imagePath) {
        byte[] imageData = null;
        try (FileInputStream fis = new FileInputStream(new File(imagePath))) {
            imageData = new byte[fis.available()];
            fis.read(imageData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageData;
    }
	/**
	 * Método para registrar un usuario
	 */
	@Override
	public Boolean registrar(UsuarioDTO usuarioDTO) {
		Usuario usuarioDAO=usuarioDTOaUsuario(usuarioDTO);
		Usuario usuComprobar=usuarioRepositorio.findFirstByEmailUsuario(usuarioDAO.getEmailUsuario());
		if(usuComprobar!=null) {
			return false;
		}else {
			usuarioDAO.setPasswd_usuario(passwordEncoder.encode(usuarioDTO.getPasswd_usuario()));
		usuarioDAO = usuarioRepositorio.save(usuarioDAO);
		EnviarEmailRegistro(usuarioDAO.getEmailUsuario());
		return true;
		}
		
	}
	/**
	 * Método para enviar el email de registro
	 */
	@Override
	public void EnviarEmailRegistro(String email) {
		String urlRecuperar="http://localhost:8080/controller/confirmarRegistro/"+email;
		try {
		 MimeMessage mensaje = javaMailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

         helper.setFrom("blogship@mnocea.eu");
         helper.setTo(email);
         helper.setSubject("Confirmar Registro BlogShip");
         
         helper.setText("Con este enlace puede confirmar su registro:"+urlRecuperar, true);
         javaMailSender.send(mensaje);
		}catch(Exception ex) {
			System.out.println("error al mandar email");
		}
		
	}
	/**
	 * Método para convertir un usuario dao a dto
	 */
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
	    usuarioDTO.setImagen_usuario(usuario.getImagen_usuario());
	    // Asignar otros campos según sea necesario
	    
	    return usuarioDTO;
	}
	/**
	 * método que crea el usuario admin al iniciar la aplicación
	 */
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
	/**
	 * método para generar un token nuevo
	 */
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
	/**
	 * método para enviar el email de recuperar
	 */
	@Override
	public void EnviarEmailRecuperar(String email, String token) {
		String urlRecuperar="http://localhost:8080/controller/cambiarContrasena/"+email+"/"+token;
		try {
		 MimeMessage mensaje = javaMailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

         helper.setFrom("blogship@mnocea.eu");
         helper.setTo(email);
         helper.setSubject("Recuperar Contraseña BlogShip");
         
         helper.setText("Con este enlace puede cambiar su contraseña:"+urlRecuperar, true);
         javaMailSender.send(mensaje);
		}catch(Exception ex) {
			System.out.println("error al mandar email");
		}
		
	}
	
	

}
