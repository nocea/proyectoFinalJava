package proyectoFinalJava.proyectoFinalJava.DTO;

import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;

public class PostDTO {
		//atributos
	    private Long id_post;
	    private String titulo_post;
	    private byte[] imagen_post; 
	    private String string_imagen_post;
	    private String pieDeFoto_post;
	    private String usuario_alias_post;
	    private Usuario usuario;
	    //gets y sets
	    public String getUsuario_alias_post() {
			return usuario_alias_post;
		}


		public void setUsuario_alias_post(String usuario_alias_post) {
			this.usuario_alias_post = usuario_alias_post;
		}
		public Long getId_post() {
			return id_post;
		}


		public void setId_post(Long id_post) {
			this.id_post = id_post;
		}


		public String getTitulo_post() {
			return titulo_post;
		}


		public void setTitulo_post(String titulo_post) {
			this.titulo_post = titulo_post;
		}


		public byte[] getImagen_post() {
			return imagen_post;
		}


		public void setImagen_post(byte[] imagen_post) {
			this.imagen_post = imagen_post;
		}


		public String getString_imagen_post() {
			return string_imagen_post;
		}


		public void setString_imagen_post(String string_imagen_post) {
			this.string_imagen_post = string_imagen_post;
		}


		public String getPieDeFoto_post() {
			return pieDeFoto_post;
		}


		public void setPieDeFoto_post(String pieDeFoto_post) {
			this.pieDeFoto_post = pieDeFoto_post;
		}


		public Usuario getUsuario() {
			return usuario;
		}


		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}
		//contructores
		public PostDTO(Long id_post, String titulo_post, byte[] imagen_post, String string_imagen_post,
				String pieDeFoto_post, String usuario_alias_post, Usuario usuario) {
			super();
			this.id_post = id_post;
			this.titulo_post = titulo_post;
			this.imagen_post = imagen_post;
			this.string_imagen_post = string_imagen_post;
			this.pieDeFoto_post = pieDeFoto_post;
			this.usuario_alias_post = usuario_alias_post;
			this.usuario = usuario;
		}


		public PostDTO() {
			super();
		}
}

