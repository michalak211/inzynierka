package pl.mrozek.inzynierka.Dto;

import com.sun.istack.Nullable;
import lombok.*;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;

import javax.persistence.*;

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
    private String typ;
//    @Nullable
    private String opisDodatkowy;

}
//TODO
//Dodać pobieranie zjec w gformularzu bedac w pracy