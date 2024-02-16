package proyectoFinalJava.proyectoFinalJava.Modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts",schema="sch_pf")
public class Post {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contenido;

    @Column(nullable = false)
    private byte[] imagenUrl; 

    @Column(nullable = true)
    private String pieDeFoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

	public Post(Long id, String contenido, byte[] imagenUrl, String pieDeFoto, Usuario usuario) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.imagenUrl = imagenUrl;
		this.pieDeFoto = pieDeFoto;
		this.usuario = usuario;
	}

	public Post() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getContenido() {
		return contenido;
	}

	public byte[] getImagenUrl() {
		return imagenUrl;
	}

	public String getPieDeFoto() {
		return pieDeFoto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public void setImagenUrl(byte[] imagenUrl) {
		this.imagenUrl = imagenUrl;
	}

	public void setPieDeFoto(String pieDeFoto) {
		this.pieDeFoto = pieDeFoto;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

   
}
