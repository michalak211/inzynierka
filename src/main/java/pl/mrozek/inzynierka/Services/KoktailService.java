package pl.mrozek.inzynierka.Services;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Repo.AlkoholRepo;
import pl.mrozek.inzynierka.Repo.BarekRepo;
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

    private final
    KoktailRepo koktailRepo;
    private final Mapper mapper;
    private final BarekRepo barekRepo;

    public KoktailService(KoktailRepo koktailRepo, Mapper mapper, BarekRepo barekRepo, AlkoholRepo alkoholRepo) {
        this.koktailRepo = koktailRepo;
        this.mapper = mapper;
        this.barekRepo = barekRepo;
    }

    public List<Koktajl> getAllKoktajls() {
        return (List<Koktajl>) koktailRepo.findAll();
    }

    public void addKoktajl(KoktajlForm koktajlForm) {
        Koktajl koktajl = new Koktajl();
        koktajl = mapper.toKoktajl(koktajl, koktajlForm);
        koktailRepo.save(koktajl);
    }

    public KoktajlForm getKoktajlForm(Long id) {
        Koktajl koktajl = koktailRepo.findById(id).orElse(new Koktajl());
        return mapper.toKoktajlForm(koktajl);
    }

    public void editKoktajl(Long id, KoktajlForm koktajlForm) {

        if (koktailRepo.findById(id).isPresent()) {
            Koktajl koktajl = koktailRepo.findById(id).get();
            koktajl = mapper.toKoktajl(koktajl, koktajlForm);
            koktailRepo.save(koktajl);
            return;
        }
        addKoktajl(koktajlForm);

    }

    public List<KoktajlForm> getAllKoktajlForms() {

        ArrayList<Koktajl> list = (ArrayList<Koktajl>) koktailRepo.findAll();
        List<KoktajlForm> koktajlFormList = new ArrayList<>();
        for (Koktajl koktajl : list) {
            koktajlFormList.add(mapper.toKoktajlForm(koktajl));
        }

        return koktajlFormList;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        if (barekRepo.findByNazwaEquals("barek mieszkanie") == null) {
            Barek barek = new Barek();
            barek.setId((long) 1);
            barek.setNazwa("barek mieszkanie");
            barekRepo.save(barek);
        }
    }



    private void addPictureToKoktajl(MultipartFile file, long id) {

        try {
            byte[] bytes = file.getBytes();
            if (bytes.length < 1000) return;
            if (koktailRepo.findById(id).isPresent()) {
                Koktajl koktajl = koktailRepo.findById(id).get();
                koktajl.setZdjecie(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getPhoto(long id) {
        if (koktailRepo.findById(id).isPresent()) {
            Koktajl koktajl = koktailRepo.findById(id).get();
            return koktajl.getZdjecie();
        } else {
            return null;
        }
    }

    public void addPhoto(String zdjecie, HttpServletRequest request) {

        long id = Integer.parseInt(zdjecie);
        Map<String, MultipartFile> fileMap = new HashMap<>();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            fileMap = multiRequest.getFileMap();
        }

        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            if (id == Integer.parseInt(entry.getKey())) {
                addPictureToKoktajl(entry.getValue(), id);
                break;
            }
        }
    }


    //QUERRIES



}
