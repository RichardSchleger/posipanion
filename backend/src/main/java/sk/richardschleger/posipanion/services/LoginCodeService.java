package sk.richardschleger.posipanion.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.LoginCode;
import sk.richardschleger.posipanion.repositories.LoginCodeRepository;

@Service
public class LoginCodeService {
    
    private LoginCodeRepository loginCodeRepository;

    public LoginCodeService(LoginCodeRepository loginCodeRepository) {
        this.loginCodeRepository = loginCodeRepository;
    }

    @Transactional
    public void saveLoginCode(LoginCode loginCode) {
        loginCodeRepository.save(loginCode);
    }

    @Transactional
    public LoginCode getLoginCodeByCode(String code) {
        return loginCodeRepository.findByCode(code).orElse(null);
    }

    @Transactional
    public LoginCode getLoginCodeByUserId(int id) {
        return loginCodeRepository.findByUserId(id).orElse(null);
    }

    @Transactional
    public void removeLoginCode(LoginCode loginCode) {
        loginCodeRepository.delete(loginCode);
    }

}
