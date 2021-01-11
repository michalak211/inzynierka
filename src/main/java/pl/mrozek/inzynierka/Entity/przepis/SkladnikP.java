package pl.mrozek.inzynierka.Entity.przepis;

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
@Entity
public class SkladnikP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Skladnik skladnik;
    private double iloscML;
    @Nullable
    private String opisDodatkowy;

}
