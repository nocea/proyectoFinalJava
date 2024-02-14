package proyectoFinalJava.proyectoFinalJava.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;



@Controller
public class usuarioNormalControlador {
	@Autowired
	private UsuarioServicio usuarioServicio;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@GetMapping("/inicio/miCuenta")
	public String miCuenta(Model model,Authentication authentication) {
		String username = authentication.getName();
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(username);
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
		model.addAttribute("usuarioDTO", usuarioDTO);
		return "miCuenta";
	}
}
