package com.bolsadeideas.springboot.datajpa.app;

import com.bolsadeideas.springboot.datajpa.app.service.UploadFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataJpaApplication implements CommandLineRunner {
	@Autowired
	UploadFileServiceImpl uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
	}
}
