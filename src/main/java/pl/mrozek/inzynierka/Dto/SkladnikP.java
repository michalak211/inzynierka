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
// nowa nazawa lakoholi
// mapper po dodawaniu alkoholi
// edycja
// dodawanie zdjec
// wyswietlanie zdjec