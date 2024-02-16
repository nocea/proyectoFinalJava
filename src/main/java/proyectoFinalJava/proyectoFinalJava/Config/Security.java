package proyectoFinalJava.proyectoFinalJava.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class Security{
	
	@Autowired
    private UserDetailsService userDetailsService;
	@Bean
    BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
    DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	   
	    return authProvider;
	}
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	}
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            //.csrf(csrf -> csrf.disable())
            // Configura las reglas de autorización para las solicitudes HTTP.
            .authorizeHttpRequests(auth -> 
                auth
                	// Permite el acceso público a ciertos recursos y direcciones de URL que no requieren autenticación.
                    .requestMatchers("/webjars/**", "/css/**", "/script/**", "/controller/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/inicio/**").hasRole("USUARIO")
                    .requestMatchers("/index/**").hasAnyRole("ADMIN","USUARIO")
                    .anyRequest().authenticated()// Exige autenticación para cualquier otra solicitud.
            )
            // Configura el proceso de inicio de sesión y la página de inicio de sesión.
            .formLogin(login ->
                login
                    .loginPage("/controller/login") // Establece la página de inicio de sesión personalizada.
                    .defaultSuccessUrl("/index", true) // Establece la URL de redirección después de un inicio de sesión exitoso.
                    .loginProcessingUrl("/controller/login-post") // Establece la URL de procesamiento del formulario de inicio de sesión.
            )
            // Configura el proceso de cierre de sesión.
            .logout(logout ->
                logout
                    .logoutUrl("/controller/logout") // Establece la URL de cierre de sesión personalizada.
                    .logoutSuccessUrl("/") // Establece la URL de redirección después de un cierre de sesión exitoso.
            );
        
        // Configura un proveedor de autenticación personalizado.
        http.authenticationProvider(authenticationProvider());

        // Construye y devuelve la cadena de filtros de seguridad configurada.
        return http.build();
    }
    
}
