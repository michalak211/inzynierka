package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;
import pl.mrozek.inzynierka.Repo.KoktailRepo;

@Service
public class KoktailService {

    final
    KoktailRepo koktailRepo;

    public KoktailService(KoktailRepo koktailRepo) {
        this.koktailRepo = koktailRepo;
    }

    public void addKoktai(){
        Koktail koktail=new Koktail();
        koktail.setNazwa("nowy");
        System.out.println("utworzono nowy koktajl "+koktail);
        koktailRepo.save(koktail);
        System.out.println("dodano koktail "+ koktail);
    }

}
