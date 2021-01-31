package pl.mrozek.inzynierka.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SkladnikP {
    private double iloscML;
    private String nazwa;
    private int rodzaj;
    private long typ;
    private String opisDodatkowy;

    private String nowaNazwa;
    private String nowyTyp;
    private boolean nowy;

}
//todo syropy i soki z bazy + dodawanie nowych w formularzu
//todo nowe skladniki
//todo edycja
//todo dodawanie zdjec
//todo wyswietlanie zdjec