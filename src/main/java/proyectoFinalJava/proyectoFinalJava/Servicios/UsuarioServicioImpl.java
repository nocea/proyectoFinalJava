package proyectoFinalJava.proyectoFinalJava.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
@Service
public class UsuarioServicioImpl implements UsuarioServicio {
	@Autowired
    private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	private Usuario usuarioDTOaUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuarioDAO=new Usuario(usuarioDTO.getNombreCompleto_usuario(),
				usuarioDTO.getEmail_usuario(), usuarioDTO.getAlias_usuario(), usuarioDTO.getMovil_usuario(),
				usuarioDTO.getPasswd_usuario(), "USUARIO");
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

}
