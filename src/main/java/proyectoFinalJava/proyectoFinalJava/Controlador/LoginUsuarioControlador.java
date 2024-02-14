package proyectoFinalJava.proyectoFinalJava.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;

@Controller
public class LoginUsuarioControlador {
	@Autowired
	private UsuarioServicio usuarioServicio;

	@GetMapping("/controller/login")
	public String login(Model model) {
		// Se agrega un nuevo objeto UsuarioDTO al modelo para el formulario de login
		model.addAttribute("usuario", new UsuarioDTO());
		return "login";
	}

	@GetMapping("/controller/registrar")
	public String registrarGet(Model model) {
		System.out.println("registrar");
		model.addAttribute("usuario", new UsuarioDTO());
		return "registro";
	}

	@PostMapping("/controller/registrar")
	public String registrarPost(@ModelAttribute UsuarioDTO usuario, Model model) {
		Boolean registrado;
		registrado = usuarioServicio.registrar(usuario);
		if (registrado) {
			System.out.println("registrado");
			return "redirect:/controller/registroExitoso";
		} else {
			return "redirect:/controller/ERRORPAGE";
		}
	}

	@GetMapping("/controller/registroExitoso")
	public String registroExitoso(Model model) {
		model.addAttribute("usuario", new UsuarioDTO());
		return "registroExitoso";
	}
	@GetMapping("/controller/ERRORPAGE")
	public String ERRORPAGE(Model model) {
		model.addAttribute("usuario", new UsuarioDTO());
		return "ERRORPAGE";
	}
}
