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
    private String typ;
    private String opisDodatkowy;
    private boolean nowy;

}
//todo nowe alkohole
//todo edycja
//todo dodawanie zdjec
//todo wyswietlanie zdjec