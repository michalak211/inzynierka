package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Repo.KoktailRepo;
import pl.mrozek.inzynierka.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class KoktailService {

    final
    KoktailRepo koktailRepo;
    private final Mapper mapper;

    public KoktailService(KoktailRepo koktailRepo, Mapper mapper) {
        this.koktailRepo = koktailRepo;
        this.mapper = mapper;
    }

    public void addKoktajl(){
        Koktajl koktajl =new Koktajl();
        koktajl.setNazwa("nowy");
        System.out.println("utworzono nowy koktajl "+ koktajl);
        koktailRepo.save(koktajl);
        System.out.println("dodano koktail "+ koktajl);
    }
    public List<Koktajl> getAllUserKoktajls(){

        return (List<Koktajl>) koktailRepo.findAll();
    }

    public List<KoktajlForm> getAllUserForms(){

        List<Koktajl> list = (List<Koktajl>) koktailRepo.findAll();
        List<KoktajlForm> koktajlFormList= new ArrayList<>();
        for (Koktajl koktajl:list){
            koktajlFormList.add(mapper.toKoktajlForm(koktajl));
        }

        return koktajlFormList;
    }

}
