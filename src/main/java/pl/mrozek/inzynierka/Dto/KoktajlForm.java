package pl.mrozek.inzynierka.Dto;

import lombok.*;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class KoktajlForm {

    private long id;
    private String nazwa;
    private String klasa;
    private String szklo;
    //    private String skladnikTyp;
    private String vegan;
    private String opisPrzyrzadzenia;
    private String zdobienie;

    private List<SkladnikP> listaSkladnikow;

    private Long ocena;


}
