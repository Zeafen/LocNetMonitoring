package com.zeafen.LocNetMonitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LocNetMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocNetMonitoringApplication.class, args);
	}

}