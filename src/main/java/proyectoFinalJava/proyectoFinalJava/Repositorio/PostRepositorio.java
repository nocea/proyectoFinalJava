package proyectoFinalJava.proyectoFinalJava.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyectoFinalJava.proyectoFinalJava.Modelos.Post;


@Repository
public interface PostRepositorio extends JpaRepository<Post, Long> {
}
