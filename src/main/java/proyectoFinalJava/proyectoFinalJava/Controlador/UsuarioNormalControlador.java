package proyectoFinalJava.proyectoFinalJava.Controlador;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import proyectoFinalJava.proyectoFinalJava.DTO.PostDTO;
import proyectoFinalJava.proyectoFinalJava.DTO.UsuarioDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Post;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
import proyectoFinalJava.proyectoFinalJava.Repositorio.PostRepositorio;
import proyectoFinalJava.proyectoFinalJava.Repositorio.UsuarioRepositorio;
import proyectoFinalJava.proyectoFinalJava.Servicios.PostServicio;
import proyectoFinalJava.proyectoFinalJava.Servicios.UsuarioServicio;



@Controller
public class UsuarioNormalControlador {
	@Autowired
	private UsuarioServicio usuarioServicio;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	PostRepositorio postRepositorio;
	@Autowired
	PostServicio postServicio;
	@GetMapping("/inicio/miCuenta")
	public String miCuenta(Model model,Authentication authentication) {
		String username = authentication.getName();
		Usuario usuario = usuarioRepositorio.findFirstByEmailUsuario(username);
		UsuarioDTO usuarioDTO = usuarioServicio.convertirUsuarioADTO(usuario);
		byte[] imagen_usuario=usuarioDTO.getImagen_usuario();
		String imagenBase64 = Base64.getEncoder().encodeToString(imagen_usuario);
		model.addAttribute("imagenBase64", imagenBase64);
		model.addAttribute("usuarioDTO", usuarioDTO);
		return "miCuenta";
	}
	@GetMapping("/inicio/paraTi")
	public String paraTi(Model model) {
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
	}
	@GetMapping("/inicio/crearPost")
	public String crearPost(Model model) {
		return "crearPost";
	}
	@PostMapping("/inicio/guardarPost")
    public String guardarPost(@RequestParam("titulo") String titulo,
                              @RequestParam("imagen") MultipartFile imagen,
                              @RequestParam("pieDeFoto") String pieDeFoto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
        Usuario usuario=usuarioRepositorio.findFirstByEmailUsuario(username);
        byte[] imagenBytes = imagen.getBytes();
        Post post = new Post();
        post.setTitulo_post(titulo);
        post.setImagen_post(imagenBytes);
        post.setPieDeFoto_post(pieDeFoto);
        post.setUsuario(usuario);
        postRepositorio.save(post);
        
        }catch (IOException e) {
            e.printStackTrace();
        }
		return "redirect:/inicio/paraTi";
    }
}
