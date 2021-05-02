package pl.mrozek.inzynierka.Services;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Entity.user.Authoritiy;
import pl.mrozek.inzynierka.Entity.user.BarUser;
import pl.mrozek.inzynierka.Entity.user.Role;
import pl.mrozek.inzynierka.Entity.user.VerificationToken;
import pl.mrozek.inzynierka.Repo.AuthoritiyRepo;
import pl.mrozek.inzynierka.Repo.BarUserRepo;
import pl.mrozek.inzynierka.Repo.VerificationTokenRepo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private final BarUserRepo barUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthoritiyRepo authoritiyRepo;
    private final MailSenderService mailSenderService;
    private final VerificationTokenRepo verificationTokenRepo;


    public UserService(BarUserRepo barUserRepo, PasswordEncoder passwordEncoder, AuthoritiyRepo authoritiyRepo, MailSenderService mailSenderService, VerificationTokenRepo verificationTokenRepo) {
        this.barUserRepo = barUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.authoritiyRepo = authoritiyRepo;
        this.mailSenderService = mailSenderService;
        this.verificationTokenRepo = verificationTokenRepo;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        if (barUserRepo.findAll().size()==0){adminInit();}
    }



    public void addNewUser(BarUser barUser, HttpServletRequest request) {

        if (barUser.getPassword() == null) return;
        if (barUserRepo.findAll().size()<1) adminInit();

        saveUser(barUser);
        sendToken(barUser,request);
    }


    public void saveUser(BarUser barUser){
        barUser.setPassword(passwordEncoder.encode(barUser.getPassword()));
        List<Authoritiy> authoritiyList= new ArrayList<>();
        authoritiyList.add(new Authoritiy(UUID.randomUUID(),Role.ROLE_USER.toString(),barUser));
        barUser.setAuthorities(authoritiyList);
        barUserRepo.save(barUser);
    }

    public void sendToken(BarUser barUser,HttpServletRequest request){
        String token= UUID.randomUUID().toString();
        VerificationToken verificationToken= new VerificationToken();
        verificationToken.setBarUser(barUser);
        verificationToken.setValue(token);
        verificationTokenRepo.save(verificationToken);

        String url = "http://" + request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath() +
                "/verify-token?token="+token;


        try {
            mailSenderService.sendMail(barUser.getMail(), "Verification Token", url, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }



    public void verifyToken(String token) {
        BarUser barUser = verificationTokenRepo.findByValue(token).getBarUser();
        barUser.setEnabled(true);
        barUserRepo.save(barUser);
    }

    private void adminInit() {

        BarUser barUser = new BarUser();
        barUser.setUsername("admin");
        barUser.setPassword(passwordEncoder.encode("admin"));

        barUser.setEnabled(true);
        List<Authoritiy> authoritiyList= new ArrayList<>();
        authoritiyList.add(new Authoritiy(UUID.randomUUID(),Role.ROLE_USER.toString(),barUser));
        authoritiyList.add(new Authoritiy(UUID.randomUUID(),Role.ROLE_ADMIN.toString(),barUser));


        barUser.setAuthorities(authoritiyList);

        barUserRepo.save(barUser);
        System.out.println("utworzono admina");


    }

}
