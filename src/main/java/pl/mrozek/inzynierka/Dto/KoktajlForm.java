package pl.mrozek.inzynierka.Dto;

import lombok.*;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikP;
import pl.mrozek.inzynierka.Entity.skladniki.Alkohol;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class KoktajlForm {

    private String nazwa;
    private String klasa;
    private String szklo;
//    private String skladnikTyp;
    private String vegan;
    private String opisPrzyrzadzenia;
    private String zdobienie;

    private List<SkladnikP> listaSkladnikow;
}
