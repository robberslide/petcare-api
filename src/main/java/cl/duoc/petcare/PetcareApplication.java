package cl.duoc.petcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUNTO DE ENTRADA de la aplicación.
 *
 * @SpringBootApplication junta 3 cosas:
 *  - @Configuration: esta clase puede definir configuración.
 *  - @EnableAutoConfiguration: Spring configura solo lo que necesita según las
 *    dependencias del pom.xml (ej: ve JPA + H2 y crea la conexión a la BD).
 *  - @ComponentScan: busca en este paquete y subpaquetes las clases marcadas con
 *    @RestController, @Service, @Repository, etc. y las registra como "beans".
 *
 * Al ejecutar main() se levanta un servidor Tomcat embebido en el puerto 8080.
 */
@SpringBootApplication
public class PetcareApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetcareApplication.class, args);
    }
}
