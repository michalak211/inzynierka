package pl.mrozek.inzynierka.Dto;

import lombok.*;
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
    private boolean poBarku;


}
