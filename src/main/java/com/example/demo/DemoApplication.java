package com.example.demo;

import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private UserRepo userRepo;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public void run(String... args) throws Exception {
		User adminAcc = userRepo.findByRole(Role.ADMIN);
		if (adminAcc == null) {
			User admin = new User();
			admin.setFirstName("super");
			admin.setLastName("admin");
			admin.setEmail("admin@gmail.com");
			admin.setRole(Role.ADMIN);
			admin.setPassword(new BCryptPasswordEncoder().encode("123456789"));
			userRepo.save(admin);
		}
	}
}
