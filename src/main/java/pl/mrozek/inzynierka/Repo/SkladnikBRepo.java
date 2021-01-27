package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;

@Repository
public interface SkladnikBRepo extends CrudRepository<SkladnikB, Long> {
}
