package proyectoFinalJava.proyectoFinalJava.Modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios",schema="sch_pf")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_usuario;
	private String nombreCompleto_usuario;
	@Column(name = "email_usuario", nullable = false, unique = true, length = 100)
	private String emailUsuario;
	private String alias_usuario;
	private String movil_usuario;
	private String passwd_usuario;
	private String rol_usuario;
	public Long getId_usuario() {
		return id_usuario;
	}
	public String getNombreCompleto_usuario() {
		return nombreCompleto_usuario;
	}
	public String getAlias_usuario() {
		return alias_usuario;
	}
	public String getMovil_usuario() {
		return movil_usuario;
	}
	public String getPasswd_usuario() {
		return passwd_usuario;
	}
	
	public String getEmailUsuario() {
		return emailUsuario;
	}
	public void setNombreCompleto_usuario(String nombreCompleto_usuario) {
		this.nombreCompleto_usuario = nombreCompleto_usuario;
	}
	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
	public String getRol_usuario() {
		return rol_usuario;
	}
	public void setId_usuario(Long id_usuario) {
		this.id_usuario = id_usuario;
	}
	public void setNombreCompletp_usuario(String nombreCompleto_usuario) {
		this.nombreCompleto_usuario = nombreCompleto_usuario;
	}
	public void setAlias_usuario(String alias_usuario) {
		this.alias_usuario = alias_usuario;
	}
	public void setMovil_usuario(String movil_usuario) {
		this.movil_usuario = movil_usuario;
	}
	public void setPasswd_usuario(String passwd_usuario) {
		this.passwd_usuario = passwd_usuario;
	}
	public void setRol_usuario(String rol_usuario) {
		this.rol_usuario = rol_usuario;
	}
	public Usuario(String nombreCompleto_usuario, String emailUsuario, String alias_usuario,
			String movil_usuario, String passwd_usuario, String rol_usuario) {
		super();
		this.nombreCompleto_usuario = nombreCompleto_usuario;
		this.emailUsuario = emailUsuario;
		this.alias_usuario = alias_usuario;
		this.movil_usuario = movil_usuario;
		this.passwd_usuario = passwd_usuario;
		this.rol_usuario = rol_usuario;
	}
	public Usuario() {
		super();
	}
	
}
