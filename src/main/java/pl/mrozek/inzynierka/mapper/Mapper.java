package pl.mrozek.inzynierka.mapper;

import org.springframework.stereotype.Component;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;

@Component
public class Mapper {

    public Koktail toKoktajl(Koktail koktail, KoktajlForm koktajlForm){

        koktail.setNazwa(koktajlForm.getNazwa());
        if (koktajlForm.getKlasa()!=null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa()!=null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa()!=null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa()!=null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa()!=null) {
            koktail.setKlasa(koktajlForm.getKlasa());
        }


        return koktail;
    }



}
