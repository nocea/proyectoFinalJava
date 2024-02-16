package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.util.Calendar;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Token;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.TokenRepositorio;
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
	TokenRepositorio tokenRepositorio;
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
	@GetMapping("controller/confirmarRegistro/{email}")
	public String confirmarRegistro(@PathVariable String email) {
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(email);
		usuario.setRegistrado(true);
		usuarioRepositorio.save(usuario);
		return "registroConfirmado";
	}
	@GetMapping("/controller/ERRORPAGE")
	public String ERRORPAGE(Model model) {
		model.addAttribute("usuario", new UsuarioDTO());
		return "ERRORPAGE";
	}
	@GetMapping("/controller/recordarContrasena")
	public String recordarContrasena() {
		return "recordarContrasena";
	}
	@PostMapping("/controller/mandarEmail")
	public String mandarEmail(@RequestParam("username") String username) {
		System.out.println("Email al que mandar:"+username);
		String cadena_token;		
		cadena_token=usuarioServicio.generarToken();
		System.out.println("Email al que mandar:"+cadena_token);
		usuarioServicio.EnviarEmailRecuperar(username,cadena_token);
		return "recordarContrasena";
	}
	@GetMapping("/controller/cambiarContrasena/{email}/{token}")
	public String recuperarExitoso(@PathVariable String email,@PathVariable String token,Model model) {
		System.out.println("cambiar contraseña");
		System.out.println(email);
		System.out.println(token);
		model.addAttribute("email", email);
		model.addAttribute("token", token);
		return "cambiarContrasena";
	}
	
	@PostMapping("/controller/confirmarCambioContrasena")
	public String confirmarCambioContrasena(@RequestParam ("email")String email,@RequestParam ("token")
	String token,@RequestParam ("contrasenaNueva")String contrasenaNueva) {
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(email);
		Token tokenValido=tokenRepositorio.findByCadenaToken(token);
		Calendar calendar = Calendar.getInstance();
		if(usuario != null && tokenValido != null && tokenValido.getFechafinToken().after(calendar)) {
			usuario.setPasswd_usuario(passwordEncoder.encode(contrasenaNueva));
			usuarioRepositorio.save(usuario);
			return "cambiarContrasena";
		}
		else {
		return "ERRORPAGE";
		}
	}
}
