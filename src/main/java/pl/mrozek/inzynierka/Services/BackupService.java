package pl.mrozek.inzynierka.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;
import pl.mrozek.inzynierka.Entity.user.BarUser;
import pl.mrozek.inzynierka.Repo.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
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


    public BackupService(KoktailRepo koktailRepo, SkladnikRepo skladnikRepo, ButelkaRepo butelkaRepo, BarekRepo barekRepo, BarUserRepo barUserRepo, AlkoholRepo alkoholRepo) {
        this.koktailRepo = koktailRepo;
        this.skladnikRepo = skladnikRepo;
        this.butelkaRepo = butelkaRepo;
        this.barekRepo = barekRepo;
        this.barUserRepo = barUserRepo;
        this.alkoholRepo = alkoholRepo;
    }


    public void generateBackup(HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String skladnikiJson = objectMapper.writeValueAsString(skladnikRepo.findAll());
            String strukturaJson = objectMapper.writeValueAsString(alkoholRepo.findAll());
            String butleJson = objectMapper.writeValueAsString(butelkaRepo.findAll());
            String userJson = objectMapper.writeValueAsString(barUserRepo.findAll());
            String barekJson = objectMapper.writeValueAsString(barekRepo.findAll());
            String koktajlJson = objectMapper.writeValueAsString(koktailRepo.findAll());

            String backup=skladnikiJson+"lllllhakunamatatalllll"+strukturaJson+
                    "lllllhakunamatatalllll"+butleJson+"lllllhakunamatatalllll"
//                    +userJson+"lllllhakunamatatalllll"
                    +barekJson+"lllllhakunamatatalllll"+koktajlJson;
            byte[] bytes= backup.getBytes();

            response.setContentType("text/plain");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=backup.txt");
            BufferedOutputStream outputStream= new BufferedOutputStream(response.getOutputStream());
            outputStream.write(bytes);
            outputStream.flush();


        } catch (JsonProcessingException e) {
            System.out.println("generowanie JSON się zjebało");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("zjebał się plik");
            e.printStackTrace();
        }


    }


    public void getFiletoUpdate(MultipartFile multipartFile){

        try {
            String s = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
            updateData(s);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void updateData(String data) {

        String[] parts = data.split("lllllhakunamatatalllll");

//        updateSkladniki(parts[0]);
        updateStruktura(parts[1]);
        updateButelki(parts[2]);
//        updateUsers(parts[3]);
        updateBars(parts[3]);
        updateKoktajls(parts[4]);

    }

    private void updateSkladniki(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Skladnik> skladnikList = objectMapper.readValue(json, new TypeReference<List<Skladnik>>() {
            });
            skladnikRepo.saveAll(skladnikList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void updateStruktura(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Alkohol> alkoholList = objectMapper.readValue(json, new TypeReference<List<Alkohol>>() {
            });
            alkoholRepo.saveAll(alkoholList);
//            for (Alkohol alkohol:alkoholList){
//                System.out.println(alkohol);
//            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void updateButelki(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Butelka> butelkaList = objectMapper.readValue(json, new TypeReference<List<Butelka>>() {
            });
            butelkaRepo.saveAll(butelkaList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

//    private void updateUsers(String json) {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            List<BarUser> barUserList = objectMapper.readValue(json, new TypeReference<List<BarUser>>() {
//            });
//            barUserRepo.saveAll(barUserList);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//    }
    private void updateBars(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Barek> barekList = objectMapper.readValue(json, new TypeReference<List<Barek>>() {
            });
            barekRepo.saveAll(barekList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void updateKoktajls(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Koktajl> koktajlList = objectMapper.readValue(json, new TypeReference<List<Koktajl>>() {
            });
            for (Koktajl koktajl:koktajlList){
                System.out.println(koktajl);
            }
            koktailRepo.saveAll(koktajlList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }



}
