package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Repo.KoktailRepo;
import pl.mrozek.inzynierka.mapper.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        ArrayList<Koktajl> list = (ArrayList<Koktajl>) koktailRepo.findAll();
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

    public void addPhoto(String zdjecie, HttpServletRequest request){

        long id=Integer.parseInt(zdjecie);
        Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            fileMap = multiRequest.getFileMap();
        }

        for (Map.Entry<String, MultipartFile> entry: fileMap.entrySet()){
            if (id==Integer.parseInt(entry.getKey())){
                addPictureToKoktajl(entry.getValue(),id);
                break;
            }
        }


    }



}
