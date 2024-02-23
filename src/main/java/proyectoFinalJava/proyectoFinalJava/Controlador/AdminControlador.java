package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import proyectoFinalJava.proyectoFinalJava.Modelos.Post;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.DTO.PostDTO;
import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Repositorio.PostRepositorio;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Servicios.PostServicio;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;

@Controller
public class AdminControlador {

	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	PostRepositorio postRepositorio;
	@Autowired
	PostServicio postServicio;
	@GetMapping("/admin")
	public String admin(Model model, Authentication authentication) {
		return "admin";
	}

	@GetMapping("/admin/usuarios")
	public String usuarios(Model model, Authentication authentication) {
		try {
		List<Usuario> listaUsuarios = usuarioRepositorio.findAll();
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<>();
		for (Usuario usuario : listaUsuarios) {
			UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
			listaUsuariosDTO.add(usuarioDTO);
		}
		model.addAttribute("usuarios", listaUsuariosDTO);
		return "usuarios";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/admin/posts")
	public String posts(Model model, Authentication authentication) {
		try {
		List<Post> posts = postRepositorio.findAll();
		List<PostDTO> listaPostDTO = posts.stream()
                .map(postServicio::convertirPostADTO)
                .collect(Collectors.toList());
		for (PostDTO postDTO : listaPostDTO) {
            byte[] imagen_post = postDTO.getImagen_post();
            String imagenBase64 = Base64.getEncoder().encodeToString(imagen_post);
            postDTO.setString_imagen_post(imagenBase64);
            postDTO.setUsuario_alias_post(postDTO.getUsuario().getAlias_usuario());
        }
        model.addAttribute("posts", listaPostDTO);
		return "posts";}
		catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@PostMapping("/admin/borrarPost")
    @ResponseBody
    public String borrarPost(@RequestParam("postId") Long postId,Model model) {
        try {
            postServicio.borrarPost(postId); // Suponiendo que tienes un método en tu servicio para borrar el post por su ID
            return "Post borrado correctamente";
        } catch (Exception e) {
        	return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
    }
	@GetMapping("/admin/editar/{id}")
	public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
		try {
		Usuario usuarioDAO = usuarioRepositorio.findById(id).orElse(null);
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuarioDAO);
		model.addAttribute("usuario", usuarioDTO);
		return "editarUsuario";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/admin/nuevoUsuario")
	public String nuevoUsuario(Model model) {
		try {
		model.addAttribute("usuario", new UsuarioDTO());
		return "nuevoUsuario";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@PostMapping("/admin/registrar")
	public String nuevoUsuarioRegistro(@ModelAttribute UsuarioDTO usuario, Model model) {
		try {
		Boolean registrado;
		registrado = usuarioServicio.registrar(usuario);
		if (registrado) {
			System.out.println("registrado");
			return "usuarios";
		} else {
			return "usuarios";
		}
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@PostMapping("/admin/gestionUsuario")
	public String gestionUsuario(@ModelAttribute UsuarioDTO usuario,String accion,Model model) {
		try {
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
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
		    }
	}
}
