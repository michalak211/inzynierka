package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;

@Repository
public interface AlkoholRepo extends CrudRepository<Alkohol, Long> {
}
