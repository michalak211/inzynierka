package pl.mrozek.inzynierka.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.*;
import pl.mrozek.inzynierka.Entity.user.BarUser;
import pl.mrozek.inzynierka.Repo.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class BackupService {

    private final KoktailRepo koktailRepo;
    private final SkladnikRepo skladnikRepo;
    private final ButelkaRepo butelkaRepo;
    private final BarekRepo barekRepo;
    private final BarUserRepo barUserRepo;
    private final AlkoholRepo alkoholRepo;
    private final SyropRepo syropRepo;
    private final SokRepo sokRepo;
    private final TypRepo typRepo;
    private final SkladnikService skladnikService;
    private final InnyRepo innyRepo;


    public BackupService(KoktailRepo koktailRepo, SkladnikRepo skladnikRepo, ButelkaRepo butelkaRepo, BarekRepo barekRepo, BarUserRepo barUserRepo, AlkoholRepo alkoholRepo, SyropRepo syropRepo, SokRepo sokRepo, TypRepo typRepo, SkladnikService skladnikService, InnyRepo innyRepo) {
        this.koktailRepo = koktailRepo;
        this.skladnikRepo = skladnikRepo;
        this.butelkaRepo = butelkaRepo;
        this.barekRepo = barekRepo;
        this.barUserRepo = barUserRepo;
        this.alkoholRepo = alkoholRepo;
        this.syropRepo = syropRepo;
        this.sokRepo = sokRepo;
        this.typRepo = typRepo;
        this.skladnikService = skladnikService;
        this.innyRepo = innyRepo;
    }


    @SneakyThrows
    public void generateBackup(HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();

        String soki = objectMapper.writeValueAsString(sokRepo.findAll());
        String syropy = objectMapper.writeValueAsString(syropRepo.findAll());
        String inne = objectMapper.writeValueAsString(innyRepo.findAll());
        String alkohole = objectMapper.writeValueAsString(alkoholRepo.findAll());
        String butle = objectMapper.writeValueAsString(butelkaRepo.findAll());
        String przepisy = objectMapper.writeValueAsString(koktailRepo.findAll());

        String backup = soki
                + "lllllhakunamatatalllll" + syropy
                + "lllllhakunamatatalllll" + inne
                + "lllllhakunamatatalllll" + alkohole
                + "lllllhakunamatatalllll" + butle
                + "lllllhakunamatatalllll" + przepisy;

        byte[] bytes = backup.getBytes();

        response.setContentType("text/plain");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=backup.txt");
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
    }

    public void updateData(String data) {
        String[] parts = data.split("lllllhakunamatatalllll");

        updateSoki(parts[0]);
        updateSyrop(parts[1]);
        updateInne(parts[2]);
        updateAlko(parts[3]);
        updateButla(parts[4]);
        updateKoctails(parts[5]);

    }


    @SneakyThrows
    private void updateSoki(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Sok> sokList = objectMapper.readValue(json, new TypeReference<List<Sok>>() {
        });

        sokRepo.saveAll(sokList);

//        for (Sok sok : sokList) {
//            SkladnikP skladnikP = new SkladnikP();
//            skladnikP.setNazwa(sok.getNazwa());
//            skladnikP.setIloscML(sok.getCenaZaLitr());
//            skladnikService.saveSok(skladnikP);
//        }
    }

    @SneakyThrows
    private void updateSyrop(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Syrop> syropList = objectMapper.readValue(json, new TypeReference<List<Syrop>>() {
        });
        syropRepo.saveAll(syropList);

//        for (Syrop syrop : syropList) {
//            SkladnikP skladnikP = new SkladnikP();
//            skladnikP.setNazwa(syrop.getNazwa());
//            skladnikP.setIloscML(syrop.getCenaZaLitr());
//            skladnikP.setOpisDodatkowy(syrop.getPrzepis());
//            skladnikService.saveSyrop(skladnikP);
//        }
    }

    @SneakyThrows
    private void updateInne(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Inny> innyList = objectMapper.readValue(json, new TypeReference<List<Inny>>() {
        });
        innyRepo.saveAll(innyList);

//        for (Inny inny : innyList) {
//            SkladnikP skladnikP = new SkladnikP();
//            skladnikP.setNazwa(inny.getNazwa());
//            skladnikP.setIloscML(inny.getCenaZaJednostke());
//            skladnikService.saveInny(skladnikP);
//        }
    }

    @SneakyThrows
    private void updateAlko(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Alkohol> alkoholList = objectMapper.readValue(json, new TypeReference<List<Alkohol>>() {
        });

        for (Alkohol alkohol : alkoholList) {
            alkoAdder(alkohol);
        }
    }

    private void alkoAdder(Alkohol alkohol) {

        Alkohol alkohol1 = alkoholRepo.findByNazwaEquals(alkohol.getNazwa());
        if (alkohol1 == null) {
            skladnikService.addNewAlko(alkohol.getNazwa());
            addTypToNew(alkohol);
            return;
        }

        for (Typ typ : alkohol.getTypList()) {
            skladnikService.addTyp(alkohol1.getId(), typ.getNazwa());
        }
    }

    private void addTypToNew(Alkohol alkohol) {
        Alkohol alkohol1 = alkoholRepo.findByNazwaEquals(alkohol.getNazwa());
        for (Typ typ : alkohol.getTypList()) {
            skladnikService.addTyp(alkohol1.getId(), typ.getNazwa());
        }

    }

    @SneakyThrows
    private void updateButla(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Butelka> butelkaList = objectMapper.readValue(json, new TypeReference<List<Butelka>>() {
        });

        for (Butelka butelka : butelkaList) {
            if (isButlaInBase(butelka)) continue;
            addButla(butelka);
        }
    }

    private void addButla(Butelka butelka) {


        Alkohol alkohol=alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa());
        butelka.setAlkoholId(alkohol.getId());

        Typ typ= alkohol.getTypList().stream().filter(
                o->o.getNazwa().equals(butelka.getTypNazwa())).findFirst().orElse(null);
        assert typ != null;
        butelka.setTypId(typ.getId());
        butelkaRepo.save(butelka);
    }

    private boolean isButlaInBase(Butelka butelka){

        Butelka butelka1=butelkaRepo.findByNazwaEquals(butelka.getNazwa());
        return butelka1 != null;

    }

    @SneakyThrows
    private void updateKoctails(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Koktajl> koktajlList = objectMapper.readValue(json, new TypeReference<List<Koktajl>>() {
        });

        for (Koktajl koktajl : koktajlList) {
            updateKoctail(koktajl);
        }
    }


    private void updateKoctail(Koktajl koktajl){

        if (koktailRepo.findByNazwaEquals(koktajl.getNazwa())!= null) return;

        Koktajl koktajl1= new Koktajl();
        koktajl1.setId(koktajl.getId());
        koktajl1.setNazwa(koktajl.getNazwa());
        koktajl1.setKlasa(koktajl.getKlasa());
        koktajl1.setOcena(koktajl.getOcena());
        koktajl1.setSzklo(koktajl.getSzklo());
        koktajl1.setVegan(koktajl.isVegan());
        koktajl1.setOpisPrzyzadzenia(koktajl.getOpisPrzyzadzenia());
        koktajl1.setZdobienie(koktajl.getZdobienie());
        koktajl1.setSkladnikBList(new ArrayList<>());

        for (SkladnikB skladnikB:koktajl.getSkladnikBList()){
            SkladnikB skladnikB1= new SkladnikB();
            skladnikB1.setSkladnikId(skladnikB.getSkladnikId());
            skladnikB1.setOpisDodatkowy(skladnikB.getOpisDodatkowy());
            skladnikB1.setIlosc(skladnikB.getIlosc());
            koktajl1.getSkladnikBList().add(skladnikB1);
        }

        koktailRepo.save(koktajl1);
    }


    @SneakyThrows
    public void getFiletoUpdate(MultipartFile multipartFile) {
        String s = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
        updateData(s);
    }


}
