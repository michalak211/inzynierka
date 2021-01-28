package pl.mrozek.inzynierka.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
//@Entity
public class SkladnikP {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

//    @OneToOne
//    private Skladnik skladnik;
    private double iloscML;
    private String nazwa;
    private int rodzaj;
    private long typ;
//    @Nullable
    private String opisDodatkowy;

}
//TODO skończyć wyswietlanie
//TODO mapper toForm
//todo syropy i soki z bazy + dodawanie nowych w formularzu