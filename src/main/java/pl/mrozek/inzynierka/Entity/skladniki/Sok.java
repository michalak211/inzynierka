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
public class Sok {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Nullable
    private String opisDodatkowy;
    @Nullable
    private double cenaZaLitr;

}
