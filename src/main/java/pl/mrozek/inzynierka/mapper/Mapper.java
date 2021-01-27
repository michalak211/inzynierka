package pl.mrozek.inzynierka.mapper;

import org.springframework.stereotype.Component;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class Mapper {
    final
    AlkoholRepo alkoholRepo;
    final
    SkladnikRepo skladnikRepo;
    final
    SokRepo sokRepo;
    final
    SyropRepo syropRepo;
    final TypRepo typRepo;

    public Mapper(AlkoholRepo alkoholRepo, SkladnikRepo skladnikRepo, SokRepo sokRepo, SyropRepo syropRepo, TypRepo typRepo) {
        this.alkoholRepo = alkoholRepo;
        this.skladnikRepo = skladnikRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.typRepo = typRepo;
    }


    public Koktail toKoktajl(Koktail koktail, KoktajlForm koktajlForm){

        koktail.setNazwa(koktajlForm.getNazwa());
        if (koktajlForm.getKlasa()!=null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa()!=null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getSzklo()!=null) {
            koktail.setSzklo(koktajlForm.getSzklo());
        }

        koktail.setVegan(koktajlForm.getVegan().equals("tak"));

        if (koktajlForm.getZdobienie()!=null) {
            koktail.setZdobienie(koktajlForm.getZdobienie());
        }
        /dsda

        if (koktajlForm.getOpisPrzyrzadzenia()!=null) {
            koktail.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }
        koktail.setSkladnikBList(new ArrayList<>());

        for (SkladnikP skladnikP:koktajlForm.getListaSkladnikow()){

            SkladnikB skladnikB= new SkladnikB();
            skladnikB.setIlosc(skladnikP.getIloscML());
            skladnikB.setOpisDodatkowy(skladnikP.getOpisDodatkowy());

            switch (skladnikP.getRodzaj()){
                case 0:
                    Optional<Typ> typ= typRepo.findById(skladnikP.getTyp());
                    skladnikB.setSkladnik(typ);

                    koktail.getSkladnikBList()


            }
        }


        return koktail;
    }



}
//TODO
