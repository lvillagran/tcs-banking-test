package com.banking.operaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OperacionesMain {

	public static void main(String[] args) {
		SpringApplication.run(OperacionesMain.class, args);
		System.out.println("******* SERVICIO CUENTA INICIADO *******");
	}

}
