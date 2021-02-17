package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Repo.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkladnikService {

    private final AlkoholRepo alkoholRepo;
    private final TypRepo typRepo;
    private final KoktailRepo koktailRepo;
    private final SokRepo sokRepo;
    private final SyropRepo syropRepo;
    private final InnyRepo innyRepo;
    private final BarekRepo barekRepo;

    public SkladnikService(AlkoholRepo alkoholRepo, TypRepo typRepo, KoktailRepo koktailRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, BarekRepo barekRepo) {
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.koktailRepo = koktailRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.barekRepo = barekRepo;
    }


    public boolean saveSkladnik(SkladnikP skladnikP){
        switch (skladnikP.getRodzaj()){
            case 1:
                break;
            case 2:
                if (sokRepo.findByNazwaEquals(skladnikP.getNazwa())==null) {
                    //else strona bledu?
                    Sok sok= new Sok();
                    sok.setNazwa(skladnikP.getNazwa());
                    sok.setCenaZaLitr(skladnikP.getIloscML());
                    sokRepo.save(sok);
                    return true;

                }
                break;
            case 3:

                if (syropRepo.findByNazwaEquals(skladnikP.getNazwa())==null) {
                    Syrop syrop= new Syrop();
                    syrop.setNazwa(skladnikP.getNazwa());
                    syrop.setCenaZaLitr(skladnikP.getIloscML());
                    if (skladnikP.getOpisDodatkowy()!=null) {syrop.setPrzepis(skladnikP.getOpisDodatkowy());}
                    syropRepo.save(syrop);
                    return true;
                }
                break;
            case 4:
                if (innyRepo.findByNazwaEquals(skladnikP.getNazwa())==null) {
                    Inny inny= new Inny();
                    inny.setNazwa(skladnikP.getNazwa());
                    inny.setCenaZaJednostke(skladnikP.getIloscML());
                    innyRepo.save(inny);
                    return true;

                }
                break;
        }
        return false;
    }

    public boolean deleteSokFromBar(long sokId, long barId){

        if (barekRepo.findById(barId).isPresent()){
            Barek barek=barekRepo.findById(barId).get();
            int inte= (int) (sokId-1);
            barek.getListSok().remove(inte);
            barekRepo.save(barek);
            return true;
        }

        return false;
    }
    public boolean deleteSyropFromBar(long syropId, long barId){

        if (barekRepo.findById(barId).isPresent()){
            Barek barek=barekRepo.findById(barId).get();
            int inte= (int) (syropId-1);
            barek.getListSyrop().remove(inte);
            barekRepo.save(barek);
            return true;
        }

        return false;
    }

    public boolean deleteInnyFromBar(long innyId, long barId){

        if (barekRepo.findById(barId).isPresent()){
            Barek barek=barekRepo.findById(barId).get();
            int inte= (int) (innyId-1);
            barek.getListInny().remove(inte);
            barekRepo.save(barek);
            return true;
        }

        return false;
    }

    public boolean addToBar(SkladnikP skladnikP,Long barId){
        if (barekRepo.findById(barId).isPresent()){
            Barek barek=barekRepo.findById(barId).get();

            switch (skladnikP.getRodzaj()){
                case 1:
                    break;
                case 2:
                    if (sokRepo.findById(skladnikP.getId()).isPresent()) {
                        Sok sok=sokRepo.findById(skladnikP.getId()).get();
                        if (!barek.getListSok().contains(sok)) {
                            barek.getListSok().add(sok);
                            barekRepo.save(barek);
                        }
                    }
                    break;
                case 3:

                    if (syropRepo.findById(skladnikP.getId()).isPresent()) {
                        Syrop syrop =syropRepo.findById(skladnikP.getId()).get();
                        if (!barek.getListSyrop().contains(syrop)) {
                            barek.getListSyrop().add(syrop);
                            barekRepo.save(barek);
                        }
                    }
                    break;
                case 4:
                    if (innyRepo.findById(skladnikP.getId()).isPresent()) {
                        Inny inny =innyRepo.findById(skladnikP.getId()).get();
                        if (!barek.getListInny().contains(inny)) {
                            barek.getListInny().add(inny);
                            barekRepo.save(barek);
                        }
                    }
                    break;
            }
        }

        return false;
    }

    public ArrayList<Sok> getSoksToAdd(Barek barek){
        ArrayList<Sok> arrayList= (ArrayList<Sok>) sokRepo.findAll();
        for (Sok sok:barek.getListSok()){
            arrayList.remove(sok);
        }
        return arrayList;
    }

    public ArrayList<Syrop> getSyropsToAdd(Barek barek){
        ArrayList<Syrop> arrayList= (ArrayList<Syrop>) syropRepo.findAll();
        for (Syrop syrop:barek.getListSyrop()){
            arrayList.remove(syrop);
        }
        return arrayList;
    }

    public ArrayList<Inny> getInnyToAdd(Barek barek){
        ArrayList<Inny> arrayList= (ArrayList<Inny>) innyRepo.findAll();
        for (Inny inny:barek.getListInny()){
            arrayList.remove(inny);
        }
        return arrayList;
    }


}
