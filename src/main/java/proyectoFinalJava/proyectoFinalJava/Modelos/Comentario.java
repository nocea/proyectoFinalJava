package proyectoFinalJava.proyectoFinalJava.Modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comentarios", schema = "sch_pf")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comentario;

    @Column(nullable = false)
    private String contenido;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Post post;

    // Constructor, getters y setters
    // Constructor
    public Comentario() {
        super();
    }

    public Comentario(String contenido, Usuario usuario, Post post) {
        super();
        this.contenido = contenido;
        this.usuario = usuario;
        this.post = post;
    }

    // Getters y Setters
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

