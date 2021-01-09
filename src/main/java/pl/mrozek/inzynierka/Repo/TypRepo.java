package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;

@Repository
public interface TypRepo extends CrudRepository<Typ,Long> {
}
