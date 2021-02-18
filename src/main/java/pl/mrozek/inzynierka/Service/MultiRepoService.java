package pl.mrozek.inzynierka.Service;


import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;
import pl.mrozek.inzynierka.Repo.*;

@Service
public class MultiRepoService {

    private final AlkoholRepo alkoholRepo;
    private final TypRepo typRepo;
    private final KoktailRepo koktailRepo;
    private final SokRepo sokRepo;
    private final SyropRepo syropRepo;
    private final InnyRepo innyRepo;
    private final BarekRepo barekRepo;


    public MultiRepoService(AlkoholRepo alkoholRepo, TypRepo typRepo, KoktailRepo koktailRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, BarekRepo barekRepo) {
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.koktailRepo = koktailRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.barekRepo = barekRepo;
    }


    public void saveSkladnik(Skladnik skladnik){





    }





}
