package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikP;

@Repository
public interface SkladnikPRepo extends CrudRepository<SkladnikP, Long> {
}
