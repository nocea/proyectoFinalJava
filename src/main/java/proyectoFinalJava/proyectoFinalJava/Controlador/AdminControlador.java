package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;

@Controller
public class AdminControlador {

	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	UsuarioServicio usuarioServicio;

	@GetMapping("/admin")
	public String admin(Model model, Authentication authentication) {
		return "admin";
	}

	@GetMapping("/admin/usuarios")
	public String usuarios(Model model, Authentication authentication) {
		List<Usuario> listaUsuarios = usuarioRepositorio.findAll();
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<>();
		for (Usuario usuario : listaUsuarios) {
			UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
			listaUsuariosDTO.add(usuarioDTO);
		}
		model.addAttribute("usuarios", listaUsuariosDTO);
		return "usuarios";
	}

	@GetMapping("/admin/editar/{id}")
	public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
		Usuario usuarioDAO = usuarioRepositorio.findById(id).orElse(null);
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuarioDAO);
		model.addAttribute("usuario", usuarioDTO);
		return "editarUsuario";
	}
	@GetMapping("/admin/nuevoUsuario")
	public String nuevoUsuario(Model model) {
		model.addAttribute("usuario", new UsuarioDTO());
		return "nuevoUsuario";
	}
	@PostMapping("/admin/registrar")
	public String nuevoUsuarioRegistro(@ModelAttribute UsuarioDTO usuario, Model model) {
		Boolean registrado;
		registrado = usuarioServicio.registrar(usuario);
		if (registrado) {
			System.out.println("registrado");
			return "usuarios";
		} else {
			return "usuarios";
		}
	}
	@PostMapping("/admin/gestionUsuario")
	public String gestionUsuario(@ModelAttribute UsuarioDTO usuario,String accion) {
		
		 if (accion.equals("guardar")) {
			Usuario usuarioDAO=usuarioRepositorio.findById(usuario.getId_usuario()).orElse(null);
			usuarioDAO.setNombreCompletoUsuario(usuario.getNombreCompleto_usuario());
			usuarioDAO.setMovil_usuario(usuario.getMovil_usuario());
			usuarioDAO.setAlias_usuario(usuario.getAlias_usuario());
			usuarioDAO.setEmailUsuario(usuario.getEmail_usuario());
			usuarioRepositorio.save(usuarioDAO);
			return "/admin";
		} else if (accion.equals("borrar")) {
			Usuario usuarioDAO=usuarioRepositorio.findById(usuario.getId_usuario()).orElse(null);
			usuarioRepositorio.delete(usuarioDAO);
			return "/admin";
		} else {
			return "usuarios";
		}
	}
}
