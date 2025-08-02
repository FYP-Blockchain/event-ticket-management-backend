package com.ruhuna.event_ticket_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class EventTicketManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventTicketManagementSystemApplication.class, args);
	}

}
