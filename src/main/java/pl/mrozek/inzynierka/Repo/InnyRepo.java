package pl.mrozek.inzynierka.Repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;

@Repository
public interface InnyRepo extends CrudRepository<Inny, Long> {
    Inny findByNazwaEquals(String nazwa);
}
