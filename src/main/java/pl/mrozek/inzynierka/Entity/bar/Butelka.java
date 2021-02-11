package pl.mrozek.inzynierka.Entity.bar;


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

public class Butelka {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nazwa;

    private Long AlkoholId;

    private Long TypId;

    @Nullable
    private double cenaZaLitr;

    @Nullable
    private String opis;
    @Nullable
    private long ocena;
}
