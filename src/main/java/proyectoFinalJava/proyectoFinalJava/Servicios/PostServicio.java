package proyectoFinalJava.proyectoFinalJava.Servicios;



import proyectoFinalJava.proyectoFinalJava.DTO.PostDTO;
import proyectoFinalJava.proyectoFinalJava.Modelos.Post;


public interface PostServicio {
 public PostDTO convertirPostADTO(Post post);
 public void borrarPost(Long postId);
}
