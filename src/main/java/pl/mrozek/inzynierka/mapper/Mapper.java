package pl.mrozek.inzynierka.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.Alkohol;
import pl.mrozek.inzynierka.Repo.AlkoholRepo;
import pl.mrozek.inzynierka.Repo.SkladnikRepo;
import pl.mrozek.inzynierka.Repo.SokRepo;
import pl.mrozek.inzynierka.Repo.SyropRepo;

import java.util.ArrayList;

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

    public Mapper(AlkoholRepo alkoholRepo, SkladnikRepo skladnikRepo, SokRepo sokRepo, SyropRepo syropRepo) {
        this.alkoholRepo = alkoholRepo;
        this.skladnikRepo = skladnikRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
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

        if (koktajlForm.getOpisPrzyrzadzenia()!=null) {
            koktail.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }
        koktail.setSkladnikBList(new ArrayList<>());

        for (SkladnikP skladnikP:koktajlForm.getListaSkladnikow()){

            switch (skladnikP.getRodzaj()){
                case 0:
//                    Alkohol alkohol= alkoholRepo.fi
            }
        }


        return koktail;
    }



}
//TODO
