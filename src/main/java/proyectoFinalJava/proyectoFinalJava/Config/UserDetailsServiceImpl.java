package proyectoFinalJava.proyectoFinalJava.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsuarioRepositorio usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.printf("\nIntento de inicio de sesión para el usuario: %s\n", username);
		Usuario user = usuarioRepository.findFirstByEmailUsuario(username);
		UserBuilder builder = null;
		//Para comprobar que ha pinchado en el enlace de registro y se ha cammbiado el registro a true
		System.out.println(user.getRegistrado());
		if (user != null&& user.getRegistrado()) {
			System.out.printf("\nUsuario encontrado en la base de datos: %s\n", user.getEmailUsuario());

			builder = User.withUsername(username);
			builder.disabled(false);
			builder.password(user.getPasswd_usuario());
			builder.authorities(user.getRol_usuario());
		} else {
			System.out.println("Usuario no encontrado en la base de datos");
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		return builder.build();
	}
}
