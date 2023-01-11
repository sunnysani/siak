package com.ppl.siakngnewbe.dosen;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppl.siakngnewbe.email.EmailService;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswaRepository;
import com.ppl.siakngnewbe.mahasiswa.Mahasiswa;
import com.ppl.siakngnewbe.mahasiswa.MahasiswaModelRepository;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLonceng;
import com.ppl.siakngnewbe.notifikasilonceng.NotifikasiLoncengRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ppl.siakngnewbe.security.utils.SecurityConstant.SECRET;
import static com.ppl.siakngnewbe.security.utils.SecurityConstant.TOKEN_PREFIX;

import java.util.List;
import java.util.Set;

@Service
public class DosenServiceImpl implements DosenService {

    @Autowired
    private MahasiswaModelRepository mahasiswaModelRepository;

    @Autowired
    DosenModelRepository dosenModelRepository;

    @Autowired
    IrsMahasiswaRepository irsMahasiswaRepository;

    @Autowired
    private NotifikasiLoncengRepository notifikasiLoncengRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public DecodedJWT decodeJwt(String token) {
        return JWT
                .require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
    }

    @Override
    public Boolean addPembimbingAkademik(String token, String npm) {
        DecodedJWT verifier = decodeJwt(token);
        String nip = verifier.getClaim("nip").toString().replace("\"", "");

        Mahasiswa mahasiswa = mahasiswaModelRepository.findByNpm(npm);
        Dosen dosen = dosenModelRepository.findByNip(nip);

        if (mahasiswa != null && dosen != null) {
            mahasiswa.setPembimbingAkademik(dosen);
            mahasiswaModelRepository.save(mahasiswa);
            return true;
        }
        return false;
    }

    @Override
    public List<Dosen> getAllDosen() {
        return dosenModelRepository.findAll();
    }

    @Override
    public Dosen getDosenById(String token) {
        DecodedJWT verifier = decodeJwt(token);
        String nip = verifier.getClaim("nip").toString().replace("\"", "");
        return dosenModelRepository.findByNip(nip);
    }

    @Override
    public Set<Mahasiswa> getMahasiswaBimbingan(String token) {
        DecodedJWT verifier = decodeJwt(token);
        String nip = verifier.getClaim("nip").toString().replace("\"", "");
        return dosenModelRepository.findByNip(nip).getMahasiswaModelSet();
    }

    @Override
    public Boolean setPersetujuanIrsMahasiswa(String token, String npm, IrsMahasiswa dataIRSMahasiswa) {
        DecodedJWT verifier = decodeJwt(token);
        String nip = verifier.getClaim("nip").toString().replace("\"", "");
        Dosen dosen = dosenModelRepository.findByNip(nip);

        Mahasiswa mahasiswa = mahasiswaModelRepository.findByNpm(npm);
        if (mahasiswa != null && dosen != null) {
            if (mahasiswa.getPembimbingAkademik().getNamaLengkap().equals(dosen.getNamaLengkap())) {

                IrsMahasiswa irsMahasiswa = irsMahasiswaRepository.findFirstByMahasiswaOrderBySemesterDesc(mahasiswa);
                irsMahasiswa.setStatusPersetujuan(dataIRSMahasiswa.getStatusPersetujuan());
                irsMahasiswaRepository.save(irsMahasiswa);

                String message;
                if (dataIRSMahasiswa.getStatusPersetujuan().toString().equals("DISETUJUI")) {
                    message = "IRS anda telah disetujui oleh pembimbing akademik";
                    emailService.sendSimpleMessage((mahasiswa.getUsername() + "@ui.ac.id"), "PERBARUAN STATUS IRS",
                                ("Halo " + mahasiswa.getNamaLengkap()
                                        + ",\n\nStatus IRS anda telah disetujui.\n\nCek academic.ui.ac.id untuk informasi lebih lengkap. "));

                } else {
                    message = "IRS anda tidak disetujui oleh pembimbing akademik";
                    emailService.sendSimpleMessage((mahasiswa.getUsername() + "@ui.ac.id"), "PERBARUAN STATUS IRS",
						("Halo " + mahasiswa.getNamaLengkap()
							+ ",\n\nStatus IRS anda tidak disetujui.\n\nCek academic.ui.ac.id untuk informasi lebih lengkap. "));
                }
                var notifikasiLonceng = new NotifikasiLonceng(message, mahasiswa);
                notifikasiLoncengRepository.save(notifikasiLonceng);
                Set<NotifikasiLonceng> listNotifikasi = mahasiswa.getNotifikasiLoncengSet();
                listNotifikasi.add(notifikasiLonceng);
                mahasiswa.setNotifikasiLoncengSet(listNotifikasi);
                return true;
            }
            return false;
        }
        return false;
    }
}
