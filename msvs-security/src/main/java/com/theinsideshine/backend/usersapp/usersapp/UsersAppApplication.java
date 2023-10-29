package com.theinsideshine.backend.usersapp.usersapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class UsersAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersAppApplication.class, args);
	}

}
