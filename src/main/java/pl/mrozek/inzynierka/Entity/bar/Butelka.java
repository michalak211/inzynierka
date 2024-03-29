package pl.mrozek.inzynierka.Entity.bar;


import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;

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
    private Long alkoholId;
    private Long typId;
    @Nullable
    private double cenaZaLitr;
    @Nullable
    private String opis;
    @Nullable
    private long ocena;
    @Lob
    private byte[] zdjecie;

    private String alkoholNazwa;
    private String typNazwa;
    @Transient
    private  boolean newAlko;
    @Transient
    private boolean newTyp;


}
