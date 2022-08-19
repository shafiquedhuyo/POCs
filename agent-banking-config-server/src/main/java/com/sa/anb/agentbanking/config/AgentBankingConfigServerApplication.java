package com.sa.anb.agentbanking.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class AgentBankingConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgentBankingConfigServerApplication.class, args);
	}

}
