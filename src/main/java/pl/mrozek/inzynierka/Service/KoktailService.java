package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
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

    final
    KoktailRepo koktailRepo;
    private final Mapper mapper;
    private final BarekRepo barekRepo;
    private final AlkoholRepo alkoholRepo;

    public KoktailService(KoktailRepo koktailRepo, Mapper mapper, BarekRepo barekRepo, AlkoholRepo alkoholRepo) {
        this.koktailRepo = koktailRepo;
        this.mapper = mapper;
        this.barekRepo = barekRepo;
        this.alkoholRepo = alkoholRepo;
    }

    public void addKoktajl(){
        Koktajl koktajl =new Koktajl();
        koktajl.setNazwa("nowy");
        koktailRepo.save(koktajl);
    }

    public List<KoktajlForm> getAllKoktajlForms(){

        ArrayList<Koktajl> list = (ArrayList<Koktajl>) koktailRepo.findAll();
        List<KoktajlForm> koktajlFormList= new ArrayList<>();
        for (Koktajl koktajl:list){
            koktajlFormList.add(mapper.toKoktajlForm(koktajl));
        }

        return koktajlFormList;
    }

    public List<KoktajlForm> getFormByBarek(Long barekId){
        List<KoktajlForm> koktajlFormList= new ArrayList<>();
        List<Koktajl> koktajlList= (List<Koktajl>) koktailRepo.findAll();

        Barek barek= barekRepo.findById(barekId).orElse(null);
        if (barek==null) return koktajlFormList;

        for (Koktajl koktajl:koktajlList){
            if (checkKoktailPossibility(barek,koktajl)) {
                koktajlFormList.add(mapper.toKoktajlForm(koktajl));
            }
        }
        return koktajlFormList;
    }


    public List<KoktajlForm> checkSkladnikAccesability(Long barId){
        List<KoktajlForm> koktajlFormList= getAllKoktajlForms();


        Barek barek= barekRepo.findById(barId).orElse(null);
        if (barek==null) return null;
        List<Long> typList=getBarekTypesId(barek);
        typList.addAll(addDowolnyId(barek,typList));

        List<Long> sokIds= new ArrayList<>();
        for (Sok sok:barek.getListSok()){
            sokIds.add(sok.getId());
        }

        List<Long> syropIds= new ArrayList<>();
        for (Syrop syrop:barek.getListSyrop()){
            syropIds.add(syrop.getId());
        }

        List<Long> innyIds= new ArrayList<>();
        for (Inny inny:barek.getListInny()){
            innyIds.add(inny.getId());
        }

        for (KoktajlForm koktajlForm:koktajlFormList){
            for (SkladnikP skladnikP:koktajlForm.getListaSkladnikow()){
                skladnikP.setPresent(setSkladnikPresence(skladnikP,typList,sokIds,syropIds,innyIds));
            }
        }
        return koktajlFormList;
    }


    private boolean setSkladnikPresence(SkladnikP skladnikP,List<Long> typList,List<Long> sokIds,
                                        List<Long> syropIds,List<Long> innyIds){
        if (typList.contains(skladnikP.getId())) return true;
        if (sokIds.contains(skladnikP.getId())) return true;
        if (syropIds.contains(skladnikP.getId())) return true;
        return innyIds.contains(skladnikP.getId());
    }

    private boolean checkKoktailPossibility(Barek barek, Koktajl koktajl){

        List<Long> typList=getBarekTypesId(barek);
        typList.addAll(addDowolnyId(barek,typList));
        List<Long> sokIds= new ArrayList<>();
        List<Long> syropIds= new ArrayList<>();
        List<Long> innyIds= new ArrayList<>();

        for (Sok sok:barek.getListSok()){
            sokIds.add(sok.getId());
        }
        for (Syrop syrop:barek.getListSyrop()){
            syropIds.add(syrop.getId());
        }
        for (Inny inny:barek.getListInny()){
            innyIds.add(inny.getId());
        }

        for (SkladnikB skladnikB:koktajl.getSkladnikBList()){
            if (sokIds.contains(skladnikB.getSkladnikId())) continue;
            if (syropIds.contains(skladnikB.getSkladnikId())) continue;
            if (innyIds.contains(skladnikB.getSkladnikId())) continue;
            if (typList.contains(skladnikB.getSkladnikId())) continue;
            return false;
        }
        return true;
    }


    private List<Long> getBarekTypesId(Barek barek){
        List<Long> typList= new ArrayList<>();

        for (Butelka butelka:barek.getButelkaList()){
            if (!typList.contains(butelka.getTypId())) {
                typList.add(butelka.getTypId());
            }
        }
        return typList;
    }

    private List<Long> addDowolnyId(Barek barek, List<Long> alkoList){

        List<Long> checkedAlko= new ArrayList<>();

        for (Butelka butelka:barek.getButelkaList()){
            if (checkedAlko.contains(butelka.getAlkoholId())) continue;
            Alkohol alkohol= alkoholRepo.findById(butelka.getAlkoholId()).orElse(new Alkohol());

            for (Typ typ:alkohol.getTypList()){

                if (typ.getNazwa().equals("Dowolny")){
                    if (!alkoList.contains(typ.getId())) {
                        alkoList.add(typ.getId());
                        checkedAlko.add(typ.getAlkoholID());
                    }
                    break;
                }
            }
        }

        return alkoList;
    }

    private void addPictureToKoktajl(MultipartFile file, long id){

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
