package JV20.isapsw.service;

import JV20.isapsw.common.TimeProvider;
import JV20.isapsw.model.AdministratorKlinickogCentra;
import JV20.isapsw.model.Authority;
import JV20.isapsw.model.UserRequest;
import JV20.isapsw.repository.AdministratorKlinickogCentraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdministratorKlinickogCentraService {

    @Autowired
    private AdministratorKlinickogCentraRepository akcRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityService authService;

    @Autowired
    private TimeProvider timeProvider;

    public AdministratorKlinickogCentra findOne(Long id){
        return akcRepository.findById(id).orElseGet(null);
    }

    public List<AdministratorKlinickogCentra> findAll(){
        return akcRepository.findAll();
    }

    public Page<AdministratorKlinickogCentra> findAll(Pageable page) {
        return akcRepository.findAll(page);
    }

    public AdministratorKlinickogCentra save(AdministratorKlinickogCentra akc) {
        return akcRepository.save(akc);
    }

    public AdministratorKlinickogCentra save(UserRequest userRequest) throws ParseException {
        AdministratorKlinickogCentra akc = new AdministratorKlinickogCentra();
        akc.setKorisnickoIme(userRequest.getKorisnickoIme());
        akc.setLozinka(passwordEncoder.encode(userRequest.getLozinka()));
        akc.setIme(userRequest.getIme());
        akc.setPrezime(userRequest.getPrezime());
        akc.setEmail(userRequest.getEmail());
        akc.setEnabled(true);
        akc.setConfirmed(true);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        akc.setDatumRodjenja(timeProvider.now());
        akc.setDatumRegistrovanja(timeProvider.now());
        akc.setEnabled(true);

        List<Authority> auth = new ArrayList<>();
        auth.add(authService.findByname("ROLE_ADMIN"));
        auth.add(authService.findByname("ROLE_USER"));
        akc.setAuthorities(auth);

        akc = this.akcRepository.save(akc);
        return akc;
    }

    public void remove(Long id) {
        akcRepository.deleteById(id);
    }

    public AdministratorKlinickogCentra findOneByUsername(String korisnickoIme) {
        return akcRepository.findByKorisnickoIme(korisnickoIme);
    }
}
