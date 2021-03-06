package pl.mrozek.inzynierka.Entity.przepis;


import lombok.*;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class SkladnikB {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @OneToOne
//    private Skladnik skladnik;

    private long skladnikId;
    private String opisDodatkowy;
    private double ilosc;

}
