package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;

@Repository
public interface SyropRepo extends CrudRepository<Syrop,Long> {
}
