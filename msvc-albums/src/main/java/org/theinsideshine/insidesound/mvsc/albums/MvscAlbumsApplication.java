package org.theinsideshine.insidesound.mvsc.albums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients

@SpringBootApplication
public class MvscAlbumsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvscAlbumsApplication.class, args);
	}

}
