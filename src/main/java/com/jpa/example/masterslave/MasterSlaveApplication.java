package com.jpa.example.masterslave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@SpringBootApplication
@Slf4j
public class MasterSlaveApplication {

	public static void main(String[] args) throws Exception{
		SpringApplication app = new SpringApplication(MasterSlaveApplication.class);
		Environment env = app.run(args).getEnvironment();
		log.info(
				"Access URLs:\n----------------------------------------------------------\n\t"
						+ "Local: \t\thttp://127.0.0.1:{}\n\t"
						+ "External: \thttp://{}:{}\n----------------------------------------------------------",
				env.getProperty("server.port"), InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"));
	}

}

