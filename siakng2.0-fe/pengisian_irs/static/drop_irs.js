const token = window.localStorage.getItem("tokenRes");
const irsMahasiswa = getIrsMahasiswa();
var allMatkul = [];

function getDosen(dosens) {
    var ret = "";
    ret += 
    `<td>
    <ul>`;
        for(const dosen of dosens){
            ret += 
                `<li>` + dosen.namaLengkap + `</li>`
        }
    ret += 
    `</ul>`
    return ret;
}

function getJadwal(jadwals) {
    var ret = "";
    var awal = 1;
        for(const waktu of jadwals){
            if(awal == 0) {
                ret += `</br>`;
            }
            var mulai = new Date(waktu.waktuMulai).toString();
            var selesai = new Date(waktu.waktuSelesai).toString();
            ret += waktu.hari + `, ` + mulai.substring(16,21) + `-` + selesai.substring(16,21);
            awal = 0;
        }
    ret += 
    `</td>`;
    return ret;
}

function getSksMahasiswa(matkuls) {
    var sks = 0;
    for (const matkul of matkuls) {
        sks += matkul.sks;
    }
    return sks;
}

function getIrsMahasiswa() {
    const ret = new Set();
    $.ajax({
        type: "GET",
        url: url + "/irsMahasiswa/get",
        dataType: "json",
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            for (const kelas of data.result.kelasIrsSet) {
                ret.add(kelas.kelas.id);
            }
        }
    });
    return ret;
}

function needPrintMatkul(kelass) {
    for (const kelas of kelass) {
        if(irsMahasiswa.has(kelas.id)) {
            return true;
        }
    }
    return false;
}

function getKelas(kelass, id) {
    var ret = "";
    for (const kelas of kelass) {
        if(irsMahasiswa.has(kelas.id)) {
            var namaKelas = kelas.nama;
            ret +=
            `<tr id="table-content">
            <td>` + namaKelas + `</td>` + `<td>`+ getJadwal(kelas.jadwalSet) + `</td>` + getDosen(kelas.dosenSet) + `</td>` + `<td><button class="btn" data-bs-toggle="modal" onClick="deleteModal(` + kelas.id + `,'` + namaKelas + `')" data-bs-target="#exampleModal"><i class="far fa-trash-alt" style="color:red"></i></button></td>` +
            `</tr>`;
        }
    }
    return ret;
}

function deleteModal(id, nama){
    $("#confModalBody").empty();
    $("#confModalFooter").empty();

    $("#confModalBody").append('Apakah anda yakin akan menghapus kelas ' + nama + '?');
    var $button = $(`<button type="button" class="btn btn-danger" onClick="deleteKelas(` + id + `)">Hapus Kelas</button><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>`);
    $button.appendTo($("#confModalFooter"));
}

function deleteKelas(id){
    const dataPost = [];
    for (const matkul of allMatkul) {
        
        for (const kelas of matkul.kelasSet) {
            if(id != kelas.id && irsMahasiswa.has(kelas.id)){
                dataPost.push(kelas);
            }
        }
    }
    $.ajax({
        type: "POST",
        url: url + "/irsMahasiswa/post",
        contentType: "application/json",
        data: JSON.stringify(dataPost),
        dataType: "json",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
        window.location = redirectUrl + "/main/CoursePlan/CoursePlanDrop";
        },
    });


}

function showData(data) {
    var matkuls = [];
    $(".table > tbody").html('');
    for (const matkul of data) {
        if(needPrintMatkul(matkul.kelasSet)){
            matkuls.push(matkul);
            $(".table > tbody").append(getKelas(matkul.kelasSet, matkul.id));
        }
    }
    if (matkuls.length == 0) {
        $("#dropTableContent").hide();
        $("#noClassMessage").show();
    } else {
        $("#noClassMessage").hide();
    }

    $("#jumlahSks > .placeholder-glow").remove();
    $("#jumlahSks").append(getSksMahasiswa(matkuls));
}

$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: url + "/mataKuliah/getAll?periode=latest&type=add-drop",
        dataType: "json",
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            $(".spinner-container").remove();
            if(data.status == 200) {
                $("#showIsiIrs").show();
                var result = JSON.parse(data.result);
                showData(result);
                allMatkul = result;
            }
            else {
                $("#showError").show();
            }
        }
    });

    const sksPromise = new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            dataType: "json",
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
            },
            contentType: 'application/json',
            url: url + "/pengecekan-irs/cek-sks",
            success: function (data) {
                resolve(data);
            },
            error: function (error) {
                reject(error);
            }
        });
    });

    const jadwalPromise = new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            dataType: "json",
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
            },
            contentType: 'application/json',
            url: url + "/pengecekan-irs/cek-jadwal",
            success: function(data) {
                resolve(data);
            },
            error: function(error) {
                reject(error);
            }
        });
    });

    const kapasitasPromise = new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            dataType: "json",
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
            },
            contentType: 'application/json',
            url: url + "/pengecekan-irs/cek-kapasitas",
            success: function (data) {
                resolve(data);
            },
            error: function (error) {
                reject(error);
            }
        });
    });
        
    const prasyaratPromise = new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            dataType: "json",
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
            },
            contentType: 'application/json',
            url: url + "/pengecekan-irs/cek-prasyarat",
            success: function (data) {
                resolve(data);
            },
            error: function (error) {
                reject(error);
            }
        });
    });

    Promise.all([sksPromise, jadwalPromise, kapasitasPromise, prasyaratPromise])
    .then(([sksResponse, jadwalResponse, kapasitasResponse, prasyaratResponse]) => {
        let ok = true;
        $('#pengecekanItem').html('');

        if(!sksResponse.result.data.ok) {
            ok = false;
            $('#pengecekanItem').append(
                `<li class="list-group-item" style="color: red;">SKS diambil melebihi maksimal</li>`
             );
        }

        for (let data of jadwalResponse.result.data) {
            if (!data.ok) {
                ok = false;
                for (var i = 0 ; i < data.konflik.length ; i++) {
                    $('#pengecekanItem').append(
                        `<li class="list-group-item" style="color: red;">Jadwal ` + data.namaMataKuliah + ` dengan ` + data.konflik[i].namaMataKuliah + ` terdapat konflik ` + `</li>`
                    );
                }
            }
        }

        for (let data of kapasitasResponse.result.data) {
            if (!data.ok) {
                ok = false;
                $('#pengecekanItem').append(
                    `<li class="list-group-item" style="color: red;">Posisi ` + data.posisi + ` dari ` + data.kapasitasTotal + ` pada mata kuliah ` + data.namaMataKuliah + `</li>`
                );
            } 
        }

        for (let data of prasyaratResponse.result.data) {
            if (!data.ok) {
                ok = false;
                for (var i = 0 ; i < data.prasyarat.length ; i++) {
                    $('#pengecekanItem').append(
                        `<li class="list-group-item" style="color: red;">Mata kuliah ` + data.namaMataKuliah+ ` dengan prasyarat ` + data.prasyarat[i].namaMataKuliah + ` tidak terpenuhi</li>`
                    );
                }
            }
        }

        if(ok){
            $('#pengecekanItem').html('<li class="list-group-item" style="color: green;">IRS tidak bermasalah</li>')
        }
    });
});
