package proyectoFinalJava.proyectoFinalJava.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyectoFinalJava.proyectoFinalJava.DTO.PostDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Post;
import proyectoFinalJava.proyectoFinalJava.Repositorio.PostRepositorio;

@Service
public class PostServicioImpl implements PostServicio{
	/**
	 * Método para convertir un post a dto
	 */
	@Override
	public PostDTO convertirPostADTO(Post post) {
		PostDTO postDTO =new PostDTO();
		postDTO.setId_post(post.getId_post());
		postDTO.setTitulo_post(post.getTitulo_post());
		postDTO.setUsuario(post.getUsuario());
		postDTO.setImagen_post(post.getImagen_post());
		postDTO.setPieDeFoto_post(post.getPieDeFoto_post());
		return postDTO;
	}
	@Autowired
	PostRepositorio postRepositorio;
	/**
	 * Método para borrar un post segun su id
	 */
	@Transactional
    public void borrarPost(Long postId) {
        // Buscar el post por su ID en la base de datos
        Post post = postRepositorio.findById(postId)
                                  .orElseThrow(() -> new RuntimeException("No se encontró el post con ID: " + postId));
        
        // Borrar el post de la base de datos
        postRepositorio.delete(post);
    }

	
	
}
