package pl.mrozek.inzynierka.Service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.mapper.Mapper;

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
    private final Mapper mapper;
    private final ButelkaRepo butelkaRepo;


    public SkladnikService(AlkoholRepo alkoholRepo, TypRepo typRepo, KoktailRepo koktailRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, BarekRepo barekRepo, Mapper mapper, ButelkaRepo butelkaRepo) {
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.koktailRepo = koktailRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.barekRepo = barekRepo;
        this.mapper = mapper;
        this.butelkaRepo = butelkaRepo;
    }


    public boolean saveSkladnik(SkladnikP skladnikP) {
        switch (skladnikP.getRodzaj()) {
            case 1:
                break;
            case 2:
                if (sokRepo.findByNazwaEquals(skladnikP.getNazwa()) == null) {
                    //else strona bledu?
                    Sok sok = new Sok();
                    sok.setNazwa(skladnikP.getNazwa());
                    sok.setCenaZaLitr(skladnikP.getIloscML());
                    sokRepo.save(sok);
                    return true;
                }
                break;
            case 3:

                if (syropRepo.findByNazwaEquals(skladnikP.getNazwa()) == null) {
                    Syrop syrop = new Syrop();
                    syrop.setNazwa(skladnikP.getNazwa());
                    syrop.setCenaZaLitr(skladnikP.getIloscML());
                    if (skladnikP.getOpisDodatkowy() != null) {
                        syrop.setPrzepis(skladnikP.getOpisDodatkowy());
                    }
                    syropRepo.save(syrop);
                    return true;
                }
                break;
            case 4:
                if (innyRepo.findByNazwaEquals(skladnikP.getNazwa()) == null) {
                    Inny inny = new Inny();
                    inny.setNazwa(skladnikP.getNazwa());
                    inny.setCenaZaJednostke(skladnikP.getIloscML());
                    innyRepo.save(inny);
                    return true;

                }
                break;
        }
        return false;
    }

    public boolean deleteBottleFromBar(long bottleId, long barId) {

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            barek.getButelkaList().removeIf(butelka -> butelka.getId() == bottleId);
            barekRepo.save(barek);
            return true;
        }

        return false;
    }


    public boolean deleteSokFromBar(long sokId, long barId) {

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            barek.getListSok().removeIf(sok -> sokId==sok.getId());
            barekRepo.save(barek);
            return true;
        }

        return false;
    }

    public boolean deleteSyropFromBar(long syropId, long barId) {

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            barek.getListSyrop().removeIf(syrop -> syrop.getId()==syropId);

            barekRepo.save(barek);
            return true;
        }

        return false;
    }

    public boolean deleteInnyFromBar(long innyId, long barId) {

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            barek.getListInny().removeIf(inny -> inny.getId()==innyId);
            barekRepo.save(barek);
            return true;
        }

        return false;
    }

    public boolean addToBar(SkladnikP skladnikP, Long barId) {
        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();

            switch (skladnikP.getRodzaj()) {
                case 2:
                    if (sokRepo.findById(skladnikP.getId()).isPresent()) {
                        Sok sok = sokRepo.findById(skladnikP.getId()).get();
                        if (!barek.getListSok().contains(sok)) {
                            barek.getListSok().add(sok);
                            barekRepo.save(barek);
                        }
                    }
                    break;
                case 3:

                    if (syropRepo.findById(skladnikP.getId()).isPresent()) {
                        Syrop syrop = syropRepo.findById(skladnikP.getId()).get();
                        if (!barek.getListSyrop().contains(syrop)) {
                            barek.getListSyrop().add(syrop);
                            barekRepo.save(barek);
                        }
                    }
                    break;
                case 4:
                    if (innyRepo.findById(skladnikP.getId()).isPresent()) {
                        Inny inny = innyRepo.findById(skladnikP.getId()).get();
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

    public boolean addBottleToBar(long bottleId, Long barId) {

        if (butelkaRepo.findById(bottleId).isPresent() && barekRepo.findById(barId).isPresent()) {
            Butelka butelka = butelkaRepo.findById(bottleId).get();
            Barek barek = barekRepo.findById(barId).get();

            if (!barek.getButelkaList().contains(butelka)) {
                barek.getButelkaList().add(butelka);
                barekRepo.save(barek);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Sok> getSoksToAdd(Barek barek) {
        ArrayList<Sok> arrayList = (ArrayList<Sok>) sokRepo.findAll();
        for (Sok sok : barek.getListSok()) {
            arrayList.remove(sok);
        }
        return arrayList;
    }

    public ArrayList<Syrop> getSyropsToAdd(Barek barek) {
        ArrayList<Syrop> arrayList = (ArrayList<Syrop>) syropRepo.findAll();
        for (Syrop syrop : barek.getListSyrop()) {
            arrayList.remove(syrop);
        }
        return arrayList;
    }

    public ArrayList<Inny> getInnyToAdd(Barek barek) {
        ArrayList<Inny> arrayList = (ArrayList<Inny>) innyRepo.findAll();
        for (Inny inny : barek.getListInny()) {
            arrayList.remove(inny);
        }
        return arrayList;
    }

    public ArrayList<Butelka> getAllbutlaForms() {
        ArrayList<Butelka> butelkaList = new ArrayList<>();

        for (Butelka butelka : butelkaRepo.findAll()) {
            butelkaList.add(mapper.butlaToForm(butelka));
        }
        return butelkaList;
    }

    public ArrayList<Butelka> getBarButlaForms(long barId) {
        ArrayList<Butelka> butelkaList = new ArrayList<>();


        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            for (Butelka butelka : barek.getButelkaList()) {
                butelkaList.add(mapper.butlaToForm(butelka));
            }
        }


        return butelkaList;
    }


    public ArrayList<Butelka> getAllbutlaFormsNotBarek(long barId) {
        ArrayList<Butelka> butelkaList = new ArrayList<>();

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();

            for (Butelka butelka : butelkaRepo.findAll()) {
                if (!barek.getButelkaList().contains(butelka)) {
                    butelkaList.add(mapper.butlaToForm(butelka));
                }
            }
        }
        return butelkaList;

    }

    public Barek saveBarek(Barek barek){
        List<Butelka> typList = new ArrayList<>();
        List<Sok> sokList = new ArrayList<>();
        List<Syrop> syropList = new ArrayList<>();
        List<Inny> innyList = new ArrayList<>();

        barek.setButelkaList(typList);
        barek.setListInny(innyList);
        barek.setListSyrop(syropList);
        barek.setListSok(sokList);

        barekRepo.save(barek);


        return barek;
    }

    public String completeBarModel(Model model, Barek barek,int toClick){


        model.addAttribute("skladnikP", new SkladnikP());
        model.addAttribute("barek", barek);
        model.addAttribute("sokList", getSoksToAdd(barek));
        model.addAttribute("syropList", getSyropsToAdd(barek));
        model.addAttribute("innyList", getInnyToAdd(barek));
        model.addAttribute("barBottles", getBarButlaForms(barek.getId()));
        model.addAttribute("toClick",toClick);

        //do dodawania
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("butlaList", getAllbutlaFormsNotBarek(barek.getId()));


        return "barowe/barManager";
    }

    public String completeSkladnikiModel(Model model,int toClick){
        model.addAttribute("skladnikP", new SkladnikP());
        model.addAttribute("sokList", sokRepo.findAll());
        model.addAttribute("syropList", syropRepo.findAll());
        model.addAttribute("innyList", innyRepo.findAll());
        model.addAttribute("toClick",toClick);


        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("butlaList", getAllbutlaForms());
        return "skladniki/skladnikManager";
    }

    public void editSkladnik(SkladnikP skladnikP){

        switch (skladnikP.getRodzaj()){
            case 1:
                break;
            case 2:
                if (sokRepo.findById(skladnikP.getId()).isPresent()){
                    Sok sok= sokRepo.findById(skladnikP.getId()).get();
                    sok.setNazwa(skladnikP.getNazwa());
                    sok.setCenaZaLitr(skladnikP.getIloscML());
                    sokRepo.save(sok);
                }
                break;
            case 3:
                if (syropRepo.findById(skladnikP.getId()).isPresent()){
                    Syrop syrop= syropRepo.findById(skladnikP.getId()).get();
                    syrop.setNazwa(skladnikP.getNazwa());
                    syrop.setCenaZaLitr(skladnikP.getIloscML());
                    syrop.setPrzepis(skladnikP.getOpisDodatkowy());
                    syropRepo.save(syrop);
                }
                break;
            case 4:
                if (innyRepo.findById(skladnikP.getId()).isPresent()){
                    Inny inny= innyRepo.findById(skladnikP.getId()).get();
                    inny.setNazwa(skladnikP.getNazwa());
                    inny.setCenaZaJednostke(skladnikP.getIloscML());
                    innyRepo.save(inny);
                }
                break;
        }

    }


}
