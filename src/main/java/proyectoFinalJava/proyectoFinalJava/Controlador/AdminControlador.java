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
import proyectoFinalJava.proyectoFinalJava.Util.Util;

@Controller
public class AdminControlador {
	//Inyecciones de servicios y repositorios
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	PostRepositorio postRepositorio;
	@Autowired
	PostServicio postServicio;
	/**
	 * Método para mostrar la vista de admin
	 * @param model
	 * @param authentication
	 * @return
	 */
	@GetMapping("/admin")
	public String admin(Model model, Authentication authentication) {
		Util.log("se ha entrado en la vista admin");
		return "admin";
	}
	/**
	 * Metodo para mostrar la lista de usuarios y poder administrarlos
	 * @param model
	 * @param authentication
	 * @return
	 */
	@GetMapping("/admin/usuarios")
	public String usuarios(Model model, Authentication authentication) {
		try {
			Util.log("se ha entrado en la vista admin/usuarios");
		List<Usuario> listaUsuarios = usuarioRepositorio.findAll();//obtengo todos los usuarios
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<>();
		//los paso a DTO
		for (Usuario usuario : listaUsuarios) {
			UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
			listaUsuariosDTO.add(usuarioDTO);
		}
		//se los paso a la vista
		model.addAttribute("usuarios", listaUsuariosDTO);
		return "usuarios";
		}catch (Exception e) {
			Util.log("Ha ocurrido un error en la vista admin/usuarios");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * Método para mostrar todos los post de la base de datos y administrarlos
	 * @param model
	 * @param authentication
	 * @return
	 */
	@GetMapping("/admin/posts")
	public String posts(Model model, Authentication authentication) {
		try {
			Util.log("se ha entrado en la vista admin/post");
		List<Post> posts = postRepositorio.findAll();//obtengo todos los posts
		List<PostDTO> listaPostDTO = posts.stream()//convierto la lista post a stream para poder realizar operaciones sobre ella
                .map(postServicio::convertirPostADTO)//tomo cada elemento de post y de postServicio uso el metodo convertir a DTO
                .collect(Collectors.toList());//por último paso todos los elementos a una lista.
		//Por cada post en listaPost
		for (PostDTO postDTO : listaPostDTO) {
            byte[] imagen_post = postDTO.getImagen_post();//obtengo la imagen
            String imagenBase64 = Base64.getEncoder().encodeToString(imagen_post); //convierto los bytes a string
            postDTO.setString_imagen_post(imagenBase64); //en el dto la pongo como string
            postDTO.setUsuario_alias_post(postDTO.getUsuario().getAlias_usuario());//le añado el alias para poder mostrarlo
        }
        model.addAttribute("posts", listaPostDTO);//añado la lista a la vista
		return "posts";}
		catch (Exception e) {
			Util.log("ha ocurrido un error en la vista admin/post");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * Método para poder borrar un post.
	 * @param postId
	 * @param model
	 * @return
	 */
	@PostMapping("/admin/borrarPost")
    public String borrarPost(@RequestParam("postId") Long postId,Model model) {
        try {
            postServicio.borrarPost(postId); // Suponiendo que tienes un método en tu servicio para borrar el post por su ID
            Util.log("se ha borrado un post"+postId);
            return "redirect:/admin/posts";
        } catch (Exception e) {
        	Util.log("se ha producido un error al borrar un post");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
    }
	/**
	 * Método para mostrar un formlario en el que se edita un usuario
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/editar/{id}")
	public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
		try {
			Util.log("Se ha entrado en la vista de /admin/editar/"+id);
		Usuario usuarioDAO = usuarioRepositorio.findById(id).orElse(null);//busco el usuario por la id
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuarioDAO);//convierto a dto
		model.addAttribute("usuario", usuarioDTO);//lo madno a la vista
		return "editarUsuario";
		}catch (Exception e) {
			Util.log("Se ha producido un error en la vista /admin/editar/"+id);
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * metodo para que al admin se le muestre la vista de crear un nuevo usuario
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/nuevoUsuario")
	public String nuevoUsuario(Model model) {
		try {
			Util.log("se ha entrado en la vista /admin/nuevoUsuario");
		model.addAttribute("usuario", new UsuarioDTO());
		return "nuevoUsuario";
		}catch (Exception e) {
			Util.log("Ha ocurrido un error en la vista /admin/nuevoUsuario");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * metodo para que el admin cree un nuevo usuario
	 * @param usuario
	 * @param model
	 * @return
	 */
	@PostMapping("/admin/registrar")
	public String nuevoUsuarioRegistro(@ModelAttribute UsuarioDTO usuario, Model model) {
		try {
			
		Boolean registrado;
		registrado = usuarioServicio.registrar(usuario);
		if (registrado) {
			System.out.println("registrado");
			Util.log("se ha registrado un usuario desde la vista admin");
			return "redirect:/admin/usuarios";
		} else {
			return "usuarios";
		}
		}catch (Exception e) {
			Util.log("ha ocurrido un error al registrar un usuario desde la vista admin");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * metodo para editar un usuario o borrarlo dependiendo de la accion que se use.
	 * @param usuario
	 * @param accion
	 * @param model
	 * @return
	 */
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
			Util.log("se han guardado los cambios de un usuario desde la vista de admin:"+usuario.getNombreCompleto_usuario());
			return "/admin";
		} else if (accion.equals("borrar")) {
			Usuario usuarioDAO=usuarioRepositorio.findById(usuario.getId_usuario()).orElse(null);
			usuarioRepositorio.delete(usuarioDAO);
			Util.log("se ha borrado un usuario desde la vista administrador"+usuarioDAO.getEmailUsuario());
			return "/admin";
		} else {
			return "usuarios";
		}
		}catch (Exception e) {
			Util.log("Se ha producido un error en la gestion de un usuario desde la vista admin");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
		    }
	}
}
