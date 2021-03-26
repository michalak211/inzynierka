package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;

import java.util.List;

@Repository
public interface BarekRepo extends CrudRepository<Barek,Long> {

    List<Barek> findAllByButelkaListContaining(Butelka butelka);
    List<Barek> findAllByListSokContaining(Sok sok);
    List<Barek> findAllByListSyropContaining(Syrop syrop);
    List<Barek> findAllByListInnyContaining(Inny inny);
    Barek findByNazwaEquals(String nazwa);

}
