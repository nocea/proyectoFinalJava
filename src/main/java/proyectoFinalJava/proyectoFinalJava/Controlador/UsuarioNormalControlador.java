package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import proyectoFinalJava.proyectoFinalJava.DTO.PostDTO;
import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Comentario;
import proyectoFinalJava.proyectoFinalJava.Modelos.Post;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.ComentarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Repositorio.PostRepositorio;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Servicios.PostServicio;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;
@Controller
public class UsuarioNormalControlador {
	//IMPLEMENTO SERVICIOS Y REPOSITORIOS PARA PODER USARLOS
	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	PostRepositorio postRepositorio;
	@Autowired
	PostServicio postServicio;
	@Autowired
	ComentarioRepositorio comentarioRepositorio;
	@GetMapping("/inicio/miCuenta")
	public String miCuenta(Model model,Authentication authentication) {
		try {
		String username = authentication.getName();
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(username);
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
		byte[] imagen_usuario=usuarioDTO.getImagen_usuario();
		String imagenBase64 = Base64.getEncoder().encodeToString(imagen_usuario);
		model.addAttribute("imagenBase64", imagenBase64);
		model.addAttribute("usuarioDTO", usuarioDTO);
		return "miCuenta";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/inicio/paraTi")
	public String paraTi(Model model) {
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
		return "paraTi";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@GetMapping("/inicio/crearPost")
	public String crearPost(Model model) {
		try {
		return "crearPost";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
	@PostMapping("/inicio/guardarPost")
    public String guardarPost(@RequestParam("titulo") String titulo,
                              @RequestParam("imagen") MultipartFile imagen,
                              @RequestParam("pieDeFoto") String pieDeFoto,Model model) {
		try {
			System.out.println("entra en guardar post");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("obtiene el usuario:"+username);
        Usuario usuario=usuarioRepositorio.findFirstByEmailUsuario(username);
        byte[] imagenBytes = imagen.getBytes();
        System.out.println("obtiene la imagen");
        Post post = new Post();
        post.setTitulo_post(titulo);
        post.setImagen_post(imagenBytes);
        post.setPieDeFoto_post(pieDeFoto);
        post.setUsuario(usuario);
        postRepositorio.save(post);
        System.out.println("guarda el post");
		return "redirect:/inicio/paraTi";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
    }
	@GetMapping("/inicio/comentar/{id_post}")
    public String mostrarFormularioComentario(@PathVariable("id_post") Long idPost, Model model) {
		try {
        model.addAttribute("id_post", idPost);
        List<Comentario> listaComentarios = comentarioRepositorio.findByPostId(idPost);
        model.addAttribute("comentarios",listaComentarios);
        
        return "comentarPost";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
    }
	@PostMapping("/inicio/guardarComentario")
	public String guardarComentario(@RequestParam("id_post") Long postid,
			@RequestParam("contenido") String textoComentario,Model model) {
		try {
		Post post = postRepositorio.findById(postid).orElse(null);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuarioComentario=usuarioRepositorio.findFirstByEmailUsuario(username);
		Comentario nuevoComentario=new Comentario();
		nuevoComentario.setContenido(textoComentario);
		nuevoComentario.setUsuario(usuarioComentario);
		nuevoComentario.setPost(post);
		comentarioRepositorio.save(nuevoComentario);
		return "redirect:/inicio/paraTi";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.+Por+favor,+inténtelo+de+nuevo+más+tarde.";
	    }
	}
}
