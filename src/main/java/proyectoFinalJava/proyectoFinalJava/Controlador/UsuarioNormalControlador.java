package proyectoFinalJava.proyectoFinalJava.Controlador;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
import proyectoFinalJava.proyectoFinalJava.DTO.ComentarioDTO;
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
import proyectoFinalJava.proyectoFinalJava.Util.Util;
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
	/**
	 * metodo para mostrar los datos del usuario que ha iniciado sesión
	 * @param model
	 * @param authentication
	 * @return
	 */
	@GetMapping("/inicio/miCuenta")
	public String miCuenta(Model model,Authentication authentication) {
		try {
		String username = authentication.getName();//obtengo el nombre de la autentificación
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(username);//lo busco
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);//lo convierto a DTO
		byte[] imagen_usuario=usuarioDTO.getImagen_usuario();
		String imagenBase64 = Base64.getEncoder().encodeToString(imagen_usuario);
		model.addAttribute("imagenBase64", imagenBase64);//añado imagen
		model.addAttribute("usuarioDTO", usuarioDTO);//añado usuario
		return "miCuenta";
		}catch (Exception e) {
			Util.log("Se ha producido un error al mostrar los datos d el usuario");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * Método para mostrar la vista de post
	 * @param model
	 * @return
	 */
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
			Util.log("Se ha producido un error al mostrar los post de paraTi");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * Método para mostrar la vista de crear post
	 * @param model
	 * @return
	 */
	@GetMapping("/inicio/crearPost")
	public String crearPost(Model model) {
		try {
		return "crearPost";
		}catch (Exception e) {
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * Método para guardar un post en la base de datos
	 * @param titulo
	 * @param imagen
	 * @param pieDeFoto
	 * @param model
	 * @return
	 */
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
        Util.log("Se ha guardado un post nuevo");
		return "redirect:/inicio/paraTi";
		}catch (Exception e) {
			Util.log("Se ha producido un error al guardar un post nuevo");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
    }
	/**
	 * método para cambiar la imagen del usuario desde la vista de miCuenta
	 * @param imagen
	 * @return
	 */
	@PostMapping("/inicio/cambiarImagen")
    public String guardarPost(@RequestParam("imagen") MultipartFile imagen) {
		try {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario=usuarioRepositorio.findFirstByEmailUsuario(username);
        byte[] imagenBytes = imagen.getBytes();
        usuario.setImagen_usuario(imagenBytes);
        usuarioRepositorio.save(usuario);
        Util.log("Se ha cambiado la imagen del usuario:"+username);
        return "redirect:/inicio/miCuenta";
		}catch (Exception e) {
			Util.log("Se ha producido un error al cambiar la imagen de un usuario");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
	/**
	 * método para mostrar la vista de comentarios de un post con todos los comentarios de ese post
	 * @param idPost
	 * @param model
	 * @return
	 */
	@GetMapping("/inicio/comentar/{id_post}")
    public String mostrarFormularioComentario(@PathVariable("id_post") Long idPost, Model model) {
		try {
        model.addAttribute("id_post", idPost);
        List<Comentario> listaComentarios = comentarioRepositorio.findByPostId(idPost);//obtengo todos los comentarios de ese post
        List<ComentarioDTO> listaComentariosDTO = new ArrayList();
        //paso los comentarios a dto
        for (Comentario comentario : listaComentarios) {
            ComentarioDTO comentarioDTO = new ComentarioDTO(
                    comentario.getContenido(),
                    comentario.getUsuario(),
                    comentario.getPost()
            );
            comentarioDTO.setId_comentario(comentario.getId_comentario());
            listaComentariosDTO.add(comentarioDTO);
        }
        model.addAttribute("comentarios",listaComentariosDTO);
        return "comentarPost";
		}catch (Exception e) {
			Util.log("Se ha producido un error al mostrar los comentarios de un post");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
    }
	/**
	 * método para guardar un comentario en la base de datos
	 * @param postid
	 * @param textoComentario
	 * @param model
	 * @return
	 */
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
		Util.log("Se ha añadido un nuevo  comentario al post:"+postid);
		return "redirect:/inicio/paraTi";
		}catch (Exception e) {
			Util.log("Se ha producido un error al comentar un post");
			return "redirect:/controller/ERRORPAGE?error=Se+ha+producido+un+error+inesperado.";
	    }
	}
}
