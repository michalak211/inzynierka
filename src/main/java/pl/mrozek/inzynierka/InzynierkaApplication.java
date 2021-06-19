package pl.mrozek.inzynierka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class InzynierkaApplication {



    public static void main(String[] args) {
        SpringApplication.run(InzynierkaApplication.class, args);
//        System.out.println("jest?");
    }

//    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(InzynierkaApplication.class);
    }

}
