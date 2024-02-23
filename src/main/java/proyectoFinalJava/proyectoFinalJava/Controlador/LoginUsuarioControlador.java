package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    public String loginPost(@RequestParam("username") String username, @RequestParam("password") String password,Model model) {
		try {
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(username);
        // Autenticar al usuario utilizando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
        // Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
        model.addAttribute("usuarioDTO", usuarioDTO);
        return "redirect:/index";
		}
		catch (AuthenticationException e) {
            // Manejar errores de autenticación
            model.addAttribute("error", "Credenciales inválidas. Por favor, inténtelo de nuevo.");
            // Se agrega un nuevo objeto UsuarioDTO al modelo para el formulario de login
            model.addAttribute("usuario", new UsuarioDTO());
            return "login";
        }
		 catch (Exception e) {
			 return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
		    }
    }
	@GetMapping("/index")
    public String index(Model model, Authentication authentication) {
		try {
        return "index";
        }catch (Exception e) {
        	return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
		
    }
	@GetMapping("/controller/logout")
    public String logout(Model model) {
		try {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/controller/login";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }// Redirigir a la página de inicio de sesión
	}
	@GetMapping("/controller/login")
	public String login(@RequestParam(value = "error", required = false) String error,Model model) {
		try {
		if (error != null) {
            model.addAttribute("error", "Credenciales inválidas. Por favor, inténtelo de nuevo.");
        }
		// Se agrega un nuevo objeto UsuarioDTO al modelo para el formulario de login
		model.addAttribute("usuario", new UsuarioDTO());
		return "login";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}

	@GetMapping("/controller/registrar")
	public String registrarGet(Model model) {
		try {
		System.out.println("registrar");
		model.addAttribute("usuario", new UsuarioDTO());
		return "registro";
		}
		catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	    }
	@PostMapping("/controller/registrar")
	public String registrarPost(@ModelAttribute UsuarioDTO usuario, Model model) {
		try {
		Boolean registrado;
		registrado = usuarioServicio.registrar(usuario);
		if (registrado) {
			System.out.println("registrado");
			return "redirect:/controller/registroExitoso";
		} else {
			return "redirect:/controller/ERRORPAGE";
		}
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    
	    }
	}

	@GetMapping("/controller/registroExitoso")
	public String registroExitoso(Model model) {
		try {
		model.addAttribute("usuario", new UsuarioDTO());
		return "registroExitoso";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("controller/confirmarRegistro/{email}")
	public String confirmarRegistro(@PathVariable String email,Model model) {
		try {
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(email);
		usuario.setRegistrado(true);
		usuarioRepositorio.save(usuario);
		return "registroConfirmado";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/controller/ERRORPAGE")
	public String ERRORPAGE(@RequestParam(name = "error", required = false) String error, Model model) {
		if (error != null) {
	        model.addAttribute("error", error);
	        System.out.println(error);
	    }
	    return "ERRORPAGE";
	}
	@GetMapping("/controller/recordarContrasena")
	public String recordarContrasena(Model model) {
		try {
		return "recordarContrasena";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/controller/correoEnviado")
	public String correoEnviado(Model model) {
		try {
		return "correoEnviado";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@PostMapping("/controller/mandarEmail")
	public String mandarEmail(@RequestParam("username") String username,Model model) {
		try {
		System.out.println("Email al que mandar:"+username);
		String cadena_token;		
		cadena_token=usuarioServicio.generarToken();
		System.out.println("Email al que mandar:"+cadena_token);
		usuarioServicio.EnviarEmailRecuperar(username,cadena_token);
		return "correoEnviado";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/controller/cambiarContrasena/{email}/{token}")
	public String recuperarExitoso(@PathVariable String email,@PathVariable String token,Model model) {
		try {
		System.out.println("cambiar contraseña");
		System.out.println(email);
		System.out.println(token);
		model.addAttribute("email", email);
		model.addAttribute("token", token);
		return "cambiarContrasena";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	
	@PostMapping("/controller/confirmarCambioContrasena")
	public String confirmarCambioContrasena(@RequestParam ("email")String email,@RequestParam ("token")
	String token,@RequestParam ("contrasenaNueva")String contrasenaNueva,Model model) {
		try {
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(email);
		Token tokenValido=tokenRepositorio.findByCadenaToken(token);
		
		Calendar calendar = Calendar.getInstance();
		if(usuario != null && tokenValido != null && tokenValido.getFechafinToken().after(calendar)) {
			usuario.setPasswd_usuario(passwordEncoder.encode(contrasenaNueva));
			usuarioRepositorio.save(usuario);
			return "cambiarContrasenaExitoso";
		}
		else {
		return "ERRORPAGE";
		}
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/controller/cambiarContrasenaExitoso")
	public String cambiarContrasenaExitoso(Model model) {
		try {
		return "cambiarContrasenaExitoso";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
}
