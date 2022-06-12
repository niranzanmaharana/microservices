package com.niranzan.photoapp.api.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PhotoAppApiUsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUsersServiceApplication.class, args);
	}

}
