package proyectoFinalJava.proyectoFinalJava.Modelos;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts",schema="sch_pf")
public class Post {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_post;
    @Column(nullable = false)
    private String titulo_post;

    @Column(nullable = false)
    private byte[] imagen_post; 

    @Column(nullable = true)
    private String pieDeFoto_post;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comentario> comentarios;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
	public Post(Long id, String titulo_post, byte[] imagen_post, String pieDeFoto_post, Usuario usuario) {
		super();
		this.id_post = id;
		this.titulo_post = titulo_post;
		this.imagen_post = imagen_post;
		this.pieDeFoto_post = pieDeFoto_post;
		this.usuario = usuario;
	}
	public Post() {
		super();
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
	

   
}
