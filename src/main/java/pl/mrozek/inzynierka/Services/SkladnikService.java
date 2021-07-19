package pl.mrozek.inzynierka.Services;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        if (barekRepo.findByNazwaEquals("barek mieszkanie") == null) {
            Barek barek = new Barek();
            barek.setNazwa("barek mieszkanie");
            barekRepo.save(barek);
        }
    }

    public boolean saveSok(SkladnikP skladnikP) {

        if (sokRepo.findByNazwaEquals(skladnikP.getNazwa()) != null) return false;
        Sok sok = new Sok();
        sok.setNazwa(skladnikP.getNazwa());
        sok.setCenaZaLitr(skladnikP.getIloscML());
        sokRepo.save(sok);
        return true;

    }

    public boolean saveSyrop(SkladnikP skladnikP) {

        if (syropRepo.findByNazwaEquals(skladnikP.getNazwa()) != null) return false;
        Syrop syrop = new Syrop();
        syrop.setNazwa(skladnikP.getNazwa());
        syrop.setCenaZaLitr(skladnikP.getIloscML());
        if (skladnikP.getOpisDodatkowy() != null) {
            syrop.setPrzepis(skladnikP.getOpisDodatkowy());
        }
        syropRepo.save(syrop);
        return true;

    }

    public boolean saveInny(SkladnikP skladnikP) {

        if (innyRepo.findByNazwaEquals(skladnikP.getNazwa()) != null) return false;
        Inny inny = new Inny();
        inny.setNazwa(skladnikP.getNazwa());
        inny.setCenaZaJednostke(skladnikP.getIloscML());
        innyRepo.save(inny);
        return true;

    }


    public boolean saveSkladnik(SkladnikP skladnikP) {
        if (skladnikP.getRodzaj() == 1) return false;
        if (skladnikP.getRodzaj() == 2) return saveSok(skladnikP);
        if (skladnikP.getRodzaj() == 3) return saveSyrop(skladnikP);
        if (skladnikP.getRodzaj() == 4) return saveInny(skladnikP);
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


    public boolean deleteBottle(long bottleId) {
        if (butelkaRepo.findById(bottleId).isPresent()) {
            Butelka butelka = butelkaRepo.findById(bottleId).get();

            List<Barek> barekList = barekRepo.findAllByButelkaListContaining(butelka);
            for (Barek barek : barekList) {
                barek.getButelkaList().removeIf(butelkaBar -> butelkaBar.getId() == bottleId);
                barekRepo.save(barek);
            }
            butelkaRepo.delete(butelka);
            return true;
        }
        return false;
    }


    public boolean deleteSokFromBar(long sokId, long barId) {

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            barek.getListSok().removeIf(sok -> sokId == sok.getId());
            barekRepo.save(barek);
            return true;
        }

        return false;
    }


    public boolean deleteSok(long sokId) {
        if (sokRepo.findById(sokId).isPresent()) {
            Sok sok = sokRepo.findById(sokId).get();

            List<Barek> barekList = barekRepo.findAllByListSokContaining(sok);
            for (Barek barek : barekList) {
                barek.getListSok().removeIf(butelkaBar -> butelkaBar.getId() == sokId);
                barekRepo.save(barek);
            }
            sokRepo.delete(sok);
            return true;
        }
        return false;
    }


    public boolean deleteSyropFromBar(long syropId, long barId) {

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            barek.getListSyrop().removeIf(syrop -> syrop.getId() == syropId);
            barekRepo.save(barek);
            return true;
        }
        return false;
    }

    public boolean deleteSyrop(long syropId) {
        if (syropRepo.findById(syropId).isPresent()) {
            Syrop syrop = syropRepo.findById(syropId).get();

            List<Barek> barekList = barekRepo.findAllByListSyropContaining(syrop);
            for (Barek barek : barekList) {
                barek.getListSyrop().removeIf(butelkaBar -> butelkaBar.getId() == syropId);
                barekRepo.save(barek);
            }
            syropRepo.delete(syrop);
            return true;
        }
        return false;
    }


    public boolean deleteInnyFromBar(long innyId, long barId) {

        if (barekRepo.findById(barId).isPresent()) {
            Barek barek = barekRepo.findById(barId).get();
            barek.getListInny().removeIf(inny -> inny.getId() == innyId);
            barekRepo.save(barek);
            return true;
        }
        return false;
    }

    public boolean deleteInny(long innyId) {
        if (innyRepo.findById(innyId).isPresent()) {
            Inny inny = innyRepo.findById(innyId).get();

            List<Barek> barekList = barekRepo.findAllByListInnyContaining(inny);
            for (Barek barek : barekList) {
                barek.getListSyrop().removeIf(butelkaBar -> butelkaBar.getId() == innyId);
                barekRepo.save(barek);
            }
            innyRepo.delete(inny);
            return true;
        }
        return false;
    }

    public boolean addSokToBar(SkladnikP skladnikP, Barek barek) {
        if (!sokRepo.findById(skladnikP.getId()).isPresent()) return false;
        Sok sok = sokRepo.findById(skladnikP.getId()).get();
        if (!barek.getListSok().contains(sok)) {
            barek.getListSok().add(sok);
            barekRepo.save(barek);
        }
        return true;
    }

    public boolean addSyropToBar(SkladnikP skladnikP, Barek barek) {
        if (!syropRepo.findById(skladnikP.getId()).isPresent()) return false;
        Syrop syrop = syropRepo.findById(skladnikP.getId()).get();
        if (!barek.getListSyrop().contains(syrop)) {
            barek.getListSyrop().add(syrop);
            barekRepo.save(barek);
        }
        return true;
    }

    public boolean addInnyToBar(SkladnikP skladnikP, Barek barek) {
        if (!innyRepo.findById(skladnikP.getId()).isPresent()) return false;
        Inny inny = innyRepo.findById(skladnikP.getId()).get();
        if (!barek.getListInny().contains(inny)) {
            barek.getListInny().add(inny);
            barekRepo.save(barek);
        }
        return true;
    }


    public boolean addToBar(SkladnikP skladnikP, Long barId) {

        if (!barekRepo.findById(barId).isPresent()) return false;
        Barek barek = barekRepo.findById(barId).get();
        if (skladnikP.getRodzaj() == 1) return false;
        if (skladnikP.getRodzaj() == 2) return addSokToBar(skladnikP, barek);
        if (skladnikP.getRodzaj() == 3) return addSyropToBar(skladnikP, barek);
        if (skladnikP.getRodzaj() == 4) return addInnyToBar(skladnikP, barek);

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

    public Barek saveBarek(Barek barek) {
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

    public String completeBarModel(Model model, Barek barek, int toClick) {


        model.addAttribute("skladnikP", new SkladnikP());
        model.addAttribute("barek", barek);
        model.addAttribute("sokList", getSoksToAdd(barek));
        model.addAttribute("syropList", getSyropsToAdd(barek));
        model.addAttribute("innyList", getInnyToAdd(barek));
        model.addAttribute("barBottles", getBarButlaForms(barek.getId()));
        model.addAttribute("toClick", toClick);

        //do dodawania
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("butlaList", getAllbutlaFormsNotBarek(barek.getId()));


        return "barowe/barManager";
    }

    public String completeSkladnikiModel(Model model, int toClick) {
        model.addAttribute("skladnikP", new SkladnikP());
        model.addAttribute("sokList", sokRepo.findAll());
        model.addAttribute("syropList", syropRepo.findAll());
        model.addAttribute("innyList", innyRepo.findAll());
        model.addAttribute("toClick", toClick);


        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("butlaList", getAllbutlaForms());
        return "skladniki/skladnikManager";
    }


    public void editSok(SkladnikP skladnikP) {
        if (!sokRepo.findById(skladnikP.getId()).isPresent()) return;
        Sok sok = sokRepo.findById(skladnikP.getId()).get();
        sok.setNazwa(skladnikP.getNazwa());
        sok.setCenaZaLitr(skladnikP.getIloscML());
        sokRepo.save(sok);
    }

    public void editSyrop(SkladnikP skladnikP) {
        if (!syropRepo.findById(skladnikP.getId()).isPresent()) return;
        Syrop syrop = syropRepo.findById(skladnikP.getId()).get();
        syrop.setNazwa(skladnikP.getNazwa());
        syrop.setCenaZaLitr(skladnikP.getIloscML());
        syrop.setPrzepis(skladnikP.getOpisDodatkowy());
        syropRepo.save(syrop);
    }

    public void editInny(SkladnikP skladnikP) {
        if (!innyRepo.findById(skladnikP.getId()).isPresent()) return;
        Inny inny = innyRepo.findById(skladnikP.getId()).get();
        inny.setNazwa(skladnikP.getNazwa());
        inny.setCenaZaJednostke(skladnikP.getIloscML());
        innyRepo.save(inny);
    }


    public void editSkladnik(SkladnikP skladnikP) {

        if (skladnikP.getRodzaj() == 2) editSok(skladnikP);
        if (skladnikP.getRodzaj() == 3) editSyrop(skladnikP);
        if (skladnikP.getRodzaj() == 4) editInny(skladnikP);

//        switch (skladnikP.getRodzaj()) {
//            case 1:
//                break;
//            case 2:
//                if (sokRepo.findById(skladnikP.getId()).isPresent()) {
//                    Sok sok = sokRepo.findById(skladnikP.getId()).get();
//                    sok.setNazwa(skladnikP.getNazwa());
//                    sok.setCenaZaLitr(skladnikP.getIloscML());
//                    sokRepo.save(sok);
//                }
//                break;
//            case 3:
//                if (syropRepo.findById(skladnikP.getId()).isPresent()) {
//                    Syrop syrop = syropRepo.findById(skladnikP.getId()).get();
//                    syrop.setNazwa(skladnikP.getNazwa());
//                    syrop.setCenaZaLitr(skladnikP.getIloscML());
//                    syrop.setPrzepis(skladnikP.getOpisDodatkowy());
//                    syropRepo.save(syrop);
//                }
//                break;
//            case 4:
//                if (innyRepo.findById(skladnikP.getId()).isPresent()) {
//                    Inny inny = innyRepo.findById(skladnikP.getId()).get();
//                    inny.setNazwa(skladnikP.getNazwa());
//                    inny.setCenaZaJednostke(skladnikP.getIloscML());
//                    innyRepo.save(inny);
//                }
//                break;
//        }

    }

    public boolean deleteTyp(long typId, long alkoId) {

        if (typRepo.findById(typId).isPresent() && alkoholRepo.findById(alkoId).isPresent()) {
            Typ typ = typRepo.findById(typId).get();
            Alkohol alkohol = alkoholRepo.findById(alkoId).get();

            if (alkohol.getTypList().removeIf(typ1 -> Objects.equals(typ1.getId(), typ.getId()))) {
                alkoholRepo.save(alkohol);
                typRepo.delete(typ);
                return true;
            }
        }
        return false;
    }

    public String editAlko(long alkoId, Alkohol alkohol, Model model) {

        if (alkoholRepo.findById(alkoId).isPresent()) {
            Alkohol alkoholBaza = alkoholRepo.findById(alkoId).get();
            alkoholBaza.setNazwa(alkohol.getNazwa());
            int i = 0;
            for (Typ typ : alkoholBaza.getTypList()) {
                typ.setButelkaId(alkohol.getTypList().get(i).getButelkaId());
                typ.setNazwa(alkohol.getTypList().get(i).getNazwa());
                typRepo.save(typ);
                i++;
            }
            alkoholRepo.save(alkoholBaza);
            model.addAttribute("alkohol", alkoholBaza);
            model.addAttribute("bottleList", butelkaRepo.findAllByAlkoholIdEquals(alkoId));

            return "strukturaEdit";

        }
        return "redirect:/struktura";

    }

    public void addTyp(long id, String nowyTyp) {

        if (!alkoholRepo.findById(id).isPresent()) return;
        Alkohol alkohol = alkoholRepo.findById(id).get();
        if (typRepo.findByNazwaEquals(nowyTyp) != null) return;

        Typ newTyp = new Typ();
        newTyp.setNazwa(nowyTyp);
        newTyp.setAlkoholID(alkohol.getId());
        typRepo.save(newTyp);
        alkohol.getTypList().add(newTyp);
        alkoholRepo.save(alkohol);
    }

    public void addNewAlko(String nazwa) {
        Alkohol alkohol = new Alkohol();
        alkohol.setNazwa(nazwa);
        Typ newTyp = new Typ();
        newTyp.setNazwa("Dowolny");
        alkoholRepo.save(alkohol);
        newTyp.setAlkoholID(alkohol.getId());
        typRepo.save(newTyp);

        List<Typ> alkolist = new ArrayList<>();
        alkolist.add(newTyp);
        alkohol.setTypList(alkolist);
        alkoholRepo.save(alkohol);
    }


    public Butelka butlaToBase(Butelka butelka) {

        if (butelka.isNewAlko()) return newAlkoButelka(butelka);
        if (butelka.isNewTyp()) return newTypButelka(butelka);

        Alkohol alkohol=alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa());
        butelka.setAlkoholId(alkohol.getId());

        Typ typ= alkohol.getTypList().stream().filter(o->o.getNazwa().equals(butelka.getTypNazwa())).findFirst().orElse(null);
        if (typ== null) return null;
        butelka.setTypId(typ.getId());
        return butelka;
    }

    private Butelka newAlkoButelka(Butelka butelka) {
        if (alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa()) != null) {
            butelka.setAlkoholId(alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa()).getId());
            return butelka;
        }

        addNewAlko(butelka.getAlkoholNazwa());
        Alkohol alkohol = alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa());
        butelka.setAlkoholId(alkohol.getId());

        Typ typ = new Typ();
        typ.setAlkoholID(alkohol.getId());
        typ.setNazwa(butelka.getTypNazwa());
        typRepo.save(typ);
        alkohol.getTypList().add(typ);
        alkoholRepo.save(alkohol);
        butelka.setTypId(typ.getId());
        return butelka;
    }


    private Butelka newTypButelka(Butelka butelka) {
        Alkohol alkohol = alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa());
        butelka.setAlkoholId(alkohol.getId());

        Typ typ = new Typ();
        typ.setAlkoholID(alkohol.getId());
        typ.setNazwa(butelka.getTypNazwa());
        typRepo.save(typ);

        alkohol.getTypList().add(typ);
        alkoholRepo.save(alkohol);
        butelka.setTypId(typ.getId());
        return butelka;
    }


}
