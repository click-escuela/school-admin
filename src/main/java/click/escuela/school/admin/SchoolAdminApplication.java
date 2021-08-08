package click.escuela.school.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SchoolAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolAdminApplication.class, args);
	}

}
