package pl.mrozek.inzynierka.Entity.skladniki;

import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class Syrop extends Skladnik {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String przepis;
    @Nullable
    private double cenaZaLitr;

}
