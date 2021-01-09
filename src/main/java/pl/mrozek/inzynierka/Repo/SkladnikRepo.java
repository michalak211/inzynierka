package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;

@Repository
public interface SkladnikRepo extends CrudRepository<Skladnik, Long> {
}
