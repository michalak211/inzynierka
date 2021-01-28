package pl.mrozek.inzynierka.mapper;

import org.springframework.stereotype.Component;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;

import java.util.ArrayList;

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


    public Koktail toKoktajl(Koktail koktail, KoktajlForm koktajlForm) {

        koktail.setNazwa(koktajlForm.getNazwa());
        if (koktajlForm.getKlasa() != null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa() != null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getSzklo() != null) {
            koktail.setSzklo(koktajlForm.getSzklo());
        }

        koktail.setVegan(koktajlForm.getVegan().equals("tak"));

        if (koktajlForm.getZdobienie() != null) {
            koktail.setZdobienie(koktajlForm.getZdobienie());
        }


        if (koktajlForm.getOpisPrzyrzadzenia() != null) {
            koktail.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }
        if (koktajlForm.getOcena() != null) {
            koktail.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }




        koktail.setSkladnikBList(new ArrayList<>());
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
                koktail.getSkladnikBList().add(skladnikB);
            }

        }


        System.out.println(koktail);
        return koktail;
    }


}
