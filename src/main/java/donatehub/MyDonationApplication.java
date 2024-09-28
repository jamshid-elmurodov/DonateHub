package donatehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MyDonationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyDonationApplication.class, args);
    }
}
