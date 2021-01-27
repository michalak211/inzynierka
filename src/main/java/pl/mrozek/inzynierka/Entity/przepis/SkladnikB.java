package pl.mrozek.inzynierka.Entity.przepis;


import com.sun.istack.Nullable;
import lombok.*;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;

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

    @OneToOne
    private Skladnik skladnik;
    private String opisDodatkowy;
    @OneToOne
    @Nullable
    private Typ typ;

}
