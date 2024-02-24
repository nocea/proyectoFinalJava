package proyectoFinalJava.proyectoFinalJava.DTO;

import proyectoFinalJava.proyectoFinalJava.Modelos.Post;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;

public class ComentarioDTO {
		//atributos
	    private Long id_comentario;
	    private String contenido;
	    private Usuario usuario;
	    private Post post;
	    //constructores
	    public ComentarioDTO() {
	        super();
	    }
	    public ComentarioDTO(String contenido, Usuario usuario, Post post) {
	        super();
	        this.contenido = contenido;
	        this.usuario = usuario;
	        this.post = post;
	    }
	    //GETS Y SETS
		public Long getId_comentario() {
			return id_comentario;
		}
		public void setId_comentario(Long id_comentario) {
			this.id_comentario = id_comentario;
		}
		public String getContenido() {
			return contenido;
		}
		public void setContenido(String contenido) {
			this.contenido = contenido;
		}
		public Usuario getUsuario() {
			return usuario;
		}
		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}
		public Post getPost() {
			return post;
		}
		public void setPost(Post post) {
			this.post = post;
		}
}
