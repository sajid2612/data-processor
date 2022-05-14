package com.sajid.processor.dataprocessor;

import com.sajid.processor.dataprocessor.processor.EventProcessor;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.h2.tools.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class DataProcessorApplication {
	private final EventProcessor eventConsumer;
	public static void main(String[] args) {
		SpringApplication.run(DataProcessorApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> eventConsumer.consumeEvents(Integer.parseInt(args[0]));
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server inMemoryH2DatabaseaServer() throws SQLException {
		return Server.createTcpServer(
				"-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
	}

}
