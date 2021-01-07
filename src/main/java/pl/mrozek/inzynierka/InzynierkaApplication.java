package pl.mrozek.inzynierka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mrozek.inzynierka.Service.KoktailService;

@SpringBootApplication
public class InzynierkaApplication {


    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
    public static void main(String[] args) {
        SpringApplication.run(InzynierkaApplication.class, args);
//        System.out.println("jest?");
//        KoktailService koktailService = new KoktailService();
//        koktailService.addKoktai();
    }

//    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(InzynierkaApplication.class);
    }

}
