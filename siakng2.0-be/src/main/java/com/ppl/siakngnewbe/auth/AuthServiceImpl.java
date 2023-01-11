package com.ppl.siakngnewbe.auth;

import com.ppl.siakngnewbe.dosen.DosenDTO;
import com.ppl.siakngnewbe.dosen.Dosen;
import com.ppl.siakngnewbe.dosen.DosenModelRepository;
import com.ppl.siakngnewbe.user.UserModelRole;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaDTO;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import com.ppl.siakngnewbe.user.UserModel;
import com.ppl.siakngnewbe.user.UserModelRepository;
import com.ppl.siakngnewbe.mahasiswa.StatusAkademik;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Setter
public class AuthServiceImpl implements AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserModelRepository userModelRepository, PasswordEncoder passwordEncoder) {
        this.userModelRepository = userModelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private MahasiswaModelRepository mahasiswaModelRepository;

    @Autowired
    private DosenModelRepository dosenModelRepository;

    @Override
    public Mahasiswa registerMahasiswa(MahasiswaDTO mahasiswaDTO) throws IllegalAccessException {

        var mahasiswa = new Mahasiswa();
        mahasiswa.setNamaLengkap(mahasiswaDTO.getNamaLengkap());
        mahasiswa.setUsername(mahasiswaDTO.getUsername());
        mahasiswa.setPassword(mahasiswaDTO.getPassword());
        mahasiswa.setNpm(mahasiswaDTO.getNpm());
        mahasiswa.setIpk(mahasiswaDTO.getIpk());
        mahasiswa.setUserRole(UserModelRole.MAHASISWA);
        switch (mahasiswaDTO.getStatusAkademik()) {
            case "AKTIF":
                mahasiswa.setStatus(StatusAkademik.AKTIF);
                break;
            case "TIDAK_AKTIF":
                mahasiswa.setStatus(StatusAkademik.TIDAK_AKTIF);
                break;
            case "CUTI":
                mahasiswa.setStatus(StatusAkademik.CUTI);
                break;
            case "DROP_OUT":
                mahasiswa.setStatus(StatusAkademik.DROP_OUT);
                break;
            default:
                mahasiswa.setStatus(null);
                break;
        }
        mahasiswa.setUrlFoto(mahasiswaDTO.getUrlFoto());

        registrationHelper(mahasiswa, userModelRepository);
        mahasiswaModelRepository.save(mahasiswa);
        return mahasiswa;
    }

    @Override
    public Dosen registerDosen(DosenDTO dosenDTO) throws IllegalAccessException {
        var dosenModel = new Dosen();
        dosenModel.setNamaLengkap(dosenDTO.getNamaLengkap());
        dosenModel.setUsername(dosenDTO.getUsername());
        dosenModel.setPassword(dosenDTO.getPassword());
        dosenModel.setNip(dosenDTO.getNip());
        dosenModel.setUserRole(UserModelRole.DOSEN);
        registrationHelper(dosenModel, userModelRepository);
        dosenModelRepository.save(dosenModel);
        return dosenModel;
    }

    public void registrationHelper(UserModel userModel, UserModelRepository repository) throws IllegalAccessException {
        boolean userExist = repository.findByUsername(userModel.getUsername()).isPresent();

        if (userExist) {
            throw new IllegalAccessException("username already taken");
        }

        String encodedPassword = passwordEncoder.encode(userModel.getPassword());
        userModel.setPassword(encodedPassword);
    }
}

