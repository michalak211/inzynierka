package pl.mrozek.inzynierka.Repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;

import java.util.List;

@Repository
public interface InnyRepo extends CrudRepository<Inny, Long> {
}
