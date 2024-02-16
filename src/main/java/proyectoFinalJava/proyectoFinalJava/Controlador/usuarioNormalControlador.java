package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Post;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.PostRepositorio;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;



@Controller
public class UsuarioNormalControlador {
	@Autowired
	private UsuarioServicio usuarioServicio;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	PostRepositorio postRepositorio;
	@GetMapping("/inicio/miCuenta")
	public String miCuenta(Model model,Authentication authentication) {
		String username = authentication.getName();
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(username);
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
		model.addAttribute("usuarioDTO", usuarioDTO);
		return "miCuenta";
	}
	@GetMapping("/inicio/paraTi")
	public String paraTi(Model model) {
		List<Post> listaPosts=postRepositorio.findAll();
		model.addAttribute("listaPost",listaPosts);
		return "paraTi";
	}
}
