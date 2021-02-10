package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Repo.KoktailRepo;
import pl.mrozek.inzynierka.mapper.Mapper;

import java.io.IOException;
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
        koktailRepo.save(koktajl);
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

    public void addPictureToKoktajl(MultipartFile file, long id){

        try {
            byte[] bytes = file.getBytes();
            if (koktailRepo.findById(id).isPresent()){
                Koktajl koktajl= koktailRepo.findById(id).get();
                koktajl.setZdjecie(bytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getPhoto(long id){
        if (koktailRepo.findById(id).isPresent()){
            Koktajl koktajl= koktailRepo.findById(id).get();
            return koktajl.getZdjecie();
        }else {
            return null;
        }

    }

}
