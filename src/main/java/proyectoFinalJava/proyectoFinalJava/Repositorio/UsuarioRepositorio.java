package proyectoFinalJava.proyectoFinalJava.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyectoFinalJava.proyectoFinalJava.Modelos.Usuario;
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	public Usuario findFirstByEmailUsuario(String email_usuario);

	public boolean existsByNombreCompletoUsuario(String nombreCompletoUsuario);
}
