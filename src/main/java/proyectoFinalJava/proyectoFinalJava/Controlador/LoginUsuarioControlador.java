package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;

@Controller
public class LoginUsuarioControlador {
	@Autowired
	private UsuarioServicio usuarioServicio;
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/controller/login-post")
    public void loginPost(@RequestParam("username") String username, @RequestParam("password") String password,Model model) {
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(username);
        // Autenticar al usuario utilizando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
        // Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
        model.addAttribute("usuarioDTO", usuarioDTO);
    }
	@GetMapping("/index")
    public String index(Model model, Authentication authentication) {
        return "index"; 
    }
	@GetMapping("/controller/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/controller/login"; // Redirigir a la página de inicio de sesión
    }
	@GetMapping("/inicio/home")
	public String home(Model model, Authentication authentication) {
		System.out.println("entra a home");
		return "home";
	}
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
