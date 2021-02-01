package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;

@Repository
public interface SokRepo extends CrudRepository<Sok,Long> {
    Sok findByNazwaEquals(String nazwa);
}
