package sk.richardschleger.posipanion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PosipanionApplication {

	public static void main(String[] args) {
		System.setProperty("datastax-java-driver.basic.request.timeout", "10 seconds");
		SpringApplication.run(PosipanionApplication.class, args);
	}

}
