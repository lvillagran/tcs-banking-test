package com.banking.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackOfficeMain {

	public static void main(String[] args) {
		SpringApplication.run(BackOfficeMain.class, args);
		System.out.println("************ SERVICIO BACKOFFICE INICIADO ************");
	}

}
