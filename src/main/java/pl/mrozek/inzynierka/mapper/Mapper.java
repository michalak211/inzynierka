package pl.mrozek.inzynierka.mapper;

import org.springframework.stereotype.Component;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {
    private final
    AlkoholRepo alkoholRepo;
    private final
    SkladnikRepo skladnikRepo;
    private final
    SokRepo sokRepo;
    private final
    SyropRepo syropRepo;
    private final TypRepo typRepo;
    private final SkladnikBRepo skladnikBRepo;
    private final InnyRepo innyRepo;

    public Mapper(AlkoholRepo alkoholRepo, SkladnikRepo skladnikRepo, SokRepo sokRepo, SyropRepo syropRepo, TypRepo typRepo, SkladnikBRepo skladnikBRepo, InnyRepo innyRepo) {
        this.alkoholRepo = alkoholRepo;
        this.skladnikRepo = skladnikRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.typRepo = typRepo;
        this.skladnikBRepo = skladnikBRepo;
        this.innyRepo = innyRepo;
    }


    public Koktajl toKoktajl(Koktajl koktajl, KoktajlForm koktajlForm) {

        koktajl.setNazwa(koktajlForm.getNazwa());
        if (koktajlForm.getKlasa() != null) {
            koktajl.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa() != null) {
            koktajl.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getSzklo() != null) {
            koktajl.setSzklo(koktajlForm.getSzklo());
        }

        koktajl.setVegan(koktajlForm.getVegan().equals("tak"));

        if (koktajlForm.getZdobienie() != null) {
            koktajl.setZdobienie(koktajlForm.getZdobienie());
        }


        if (koktajlForm.getOpisPrzyrzadzenia() != null) {
            koktajl.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }
        if (koktajlForm.getOcena() != null) {
            koktajl.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }

        koktajlForm.setId(koktajl.getId());



        koktajl.setSkladnikBList(new ArrayList<>());
        if (koktajlForm.getListaSkladnikow()!= null) {
            for (SkladnikP skladnikP : koktajlForm.getListaSkladnikow()) {

                if (skladnikP.getRodzaj()>0) {
                    SkladnikB skladnikB = new SkladnikB();
                    skladnikB.setIlosc(skladnikP.getIloscML());
                    skladnikB.setOpisDodatkowy(skladnikP.getOpisDodatkowy());

                    switch (skladnikP.getRodzaj()) {
                        case 1:
                            if (typRepo.findById(skladnikP.getTyp()).isPresent()) {
                                Typ typ = typRepo.findById(skladnikP.getTyp()).get();
                                System.out.println("TYP: "+typ);
                                skladnikB.setSkladnik(typ);
                            }
                            break;
                        case 2:
                            Sok sok = new Sok();
                            sok.setNazwa(skladnikP.getNazwa());
                            sokRepo.save(sok);
                            skladnikB.setSkladnik(sok);
                            break;
                        case 3:
                            Syrop syrop = new Syrop();
                            syrop.setNazwa(skladnikP.getNazwa());
                            syropRepo.save(syrop);
                            break;
                        case 4:
                            Inny inny = new Inny();
                            inny.setNazwa(skladnikP.getNazwa());
                            innyRepo.save(inny);
                            break;
                    }
                    skladnikBRepo.save(skladnikB);
                    koktajl.getSkladnikBList().add(skladnikB);
                }

            }
        }


        System.out.println(koktajl);
        return koktajl;
    }

    public KoktajlForm toKoktajlForm (Koktajl koktajl){
        KoktajlForm koktajlForm=new KoktajlForm();

        koktajlForm.setNazwa(koktajl.getNazwa());

        if (koktajl.getKlasa()!=null){
            koktajlForm.setKlasa(koktajl.getKlasa());
        }
        if (koktajl.getSzklo()!=null){
            koktajlForm.setSzklo(koktajl.getSzklo());
        }
        if (koktajl.getOpisPrzyzadzenia()!=null){
            koktajlForm.setOpisPrzyrzadzenia(koktajl.getOpisPrzyzadzenia());
        }
        if (koktajl.getZdobienie()!=null){
            koktajlForm.setZdobienie(koktajl.getZdobienie());
        }
            koktajlForm.setOcena(koktajl.getOcena());

        if (koktajl.isVegan()){
            koktajlForm.setVegan("tak");
        } else {
            koktajlForm.setVegan("nie");
        }

        List<SkladnikP> skladnikPList= new ArrayList<>();

        for (SkladnikB skladnikB:koktajl.getSkladnikBList()){
            SkladnikP skladnikP= new SkladnikP();
            skladnikP.setIloscML(skladnikB.getIlosc());
            skladnikP.setOpisDodatkowy(skladnikB.getOpisDodatkowy());

            if (skladnikB.getSkladnik() instanceof Typ){
                skladnikP.setRodzaj(1);
                skladnikP.setTyp(skladnikB.getSkladnik().getId());
                skladnikP.setNazwa(((Typ) skladnikB.getSkladnik()).getAlkoholID().toString());
            }else if (skladnikB.getSkladnik() instanceof Sok){
                skladnikP.setRodzaj(2);
                skladnikP.setNazwa(skladnikB.getSkladnik().getNazwa());
            }else if (skladnikB.getSkladnik() instanceof Syrop){
                skladnikP.setRodzaj(3);
                skladnikP.setNazwa(skladnikB.getSkladnik().getNazwa());
            }else if (skladnikB.getSkladnik() instanceof Inny){
                skladnikP.setRodzaj(4);
                skladnikP.setNazwa(skladnikB.getSkladnik().getNazwa());
            }else {
                continue;
            }

            skladnikPList.add(skladnikP);
        }

        koktajlForm.setListaSkladnikow(skladnikPList);



        return koktajlForm;
    }

}
