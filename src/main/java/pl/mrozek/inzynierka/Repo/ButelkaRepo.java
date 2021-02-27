package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.bar.Butelka;

@Repository
public interface ButelkaRepo extends CrudRepository<Butelka,Long> {
    Butelka findByNazwaEquals(String nazwa);
}

