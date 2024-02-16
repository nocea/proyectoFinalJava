package proyectoFinalJava.proyectoFinalJava.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyectoFinalJava.proyectoFinalJava.Modelos.Token;

@Repository
public interface TokenRepositorio extends JpaRepository<Token, Long> {
	Token findByCadenaToken(String cadenaToken);
}
