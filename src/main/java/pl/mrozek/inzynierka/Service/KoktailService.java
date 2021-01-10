package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Entity.User;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;
import pl.mrozek.inzynierka.Repo.KoktailRepo;

import java.util.List;

@Service
public class KoktailService {

    final
    KoktailRepo koktailRepo;

    public KoktailService(KoktailRepo koktailRepo) {
        this.koktailRepo = koktailRepo;
    }

    public void addKoktajl(){
        Koktail koktail=new Koktail();
        koktail.setNazwa("nowy");
        System.out.println("utworzono nowy koktajl "+koktail);
        koktailRepo.save(koktail);
        System.out.println("dodano koktail "+ koktail);
    }
    public List<Koktail> getAllUserKoktajls(){

        return (List<Koktail>) koktailRepo.findAll();
    }

}
