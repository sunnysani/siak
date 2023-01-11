function getMaksSks(ips) {
    parseFloat(ips);
    let maks = 24;
    if (ips < 3.5) {
        maks = 21;
    } else if (ips < 3) {
        maks = 18;
    } else if (ips < 2.5) {
        maks = 15;
    } else if (ips < 2) {
        maks = 12;
    }
    return maks;
}

function getJadwal(jadwals) {
    let result = '';
    jadwals.forEach(function(jadwal){
        var waktuMulai = new Date(jadwal.waktuMulai);
        var waktuSelesai = new Date(jadwal.waktuSelesai);
        result += jadwal.hari + ', ' + waktuMulai.getHours() + ':' + (waktuMulai.getMinutes()==0? '00': waktuMulai.getMinutes()) + ' - ' + waktuSelesai.getHours() + ':' + (waktuSelesai.getMinutes()==0? '00': waktuSelesai.getMinutes()) + '<br>';
    })
    result = result.substring(0, result.length - 4);
    result = '<td id="td-irs">' + result + '</td>';
    return result;
}

function getRuangan(jadwals) {
    let result = '';
    jadwals.forEach(function(jadwal){
        result += jadwal.ruang + '<br>';
    })
    result = result.substring(0, result.length - 4);
    result = '<td id="td-irs">' + result + '</td>';
    return result;
}

function getDosen(dosens) {
    let result = '';
    dosens.forEach(function(dosen){
        result += dosen.namaLengkap + '<br>';
    })
    result = result.substring(0, result.length - 4);
    result = '<td id="td-irs">' + result + '</td>';
    return result;
}

function buildMatkulTable(classes) {

    let count = 1;
    let result = '';
    if(classes.length <= 0) {
        result = '<tr><td id="td-irs" class="d-table-cell text-center fst-italic" colspan="9">Belum ada kelas</td></tr>'
    }
    classes.forEach(function(data){
        result += '<tr>';
        
        result += '<td id="td-irs">' + count + '</td>' +
        '<td id="td-irs">' + data.kelas.nama + '</td>' +
        '<td id="td-irs">' + data.kelas.sks + '</td>' +
        '<td id="td-irs">' + data.kelas.nama + '</td>' +
        '<td id="td-irs">' + 'Ilmu Komputer, S1 Reguler' + '</td>';
            
        let jadwals = data.kelas.jadwalSet;
        result += getJadwal(jadwals);
        result += getRuangan(jadwals);

        let dosens = data.kelas.dosenSet;
        result += getDosen(dosens);
        
        result += '</tr>';
        count += 1;
    })
    return result;
}

$(document).ready(function () {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        contentType: 'application/json',
        url: url + "/irsMahasiswa/get",
        success: function (response) {
            if (response.result.statusPersetujuan === 'TIDAK_DISETUJUI') {
                $("#status_irs").html('Tidak disetujui');
            } else {
                $("#status_irs").html('Disetujui');
            }
            $("#sksMaks").html(getMaksSks(response.result.ips) + " SKS");
            $("#persIrs").html('Harus dengan persetujuan pembimbing akademis');
            $("#ipTerakhir").html(response.result.ips);
            $("#tableIrs > tbody").html(buildMatkulTable(response.result.kelasIrsSet));
            $("#bermasalah").hide();
        },
    });

    //  PENGECEKAN IRS

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
            error: function (error) {
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
        $('#list-pengecekan').html('');

        if(!sksResponse.result.data.ok) {
            ok = false;
            $('#list-pengecekan').append(
                `
                <div id="bermasalah">
                    <p>Sks diambil melebihi maksimal</p>
                </div>
                `
            );
        }

        for (let data of jadwalResponse.result.data) {
            if (!data.ok) {
                ok = false;
                for (var i = 0 ; i < data.konflik.length ; i++) {
                    $('#list-pengecekan').append(
                        `
                        <div id="bermasalah">
                        <p>Terdapat konflik pada jadwal ` + data.namaMataKuliah + ` dengan ` + data.konflik[i].namaMataKuliah + `</p>
                        </div>
                        `
                    );
                }
            }
        }

        for (let data of kapasitasResponse.result.data) {
            if (!data.ok) {
                ok = false;
                $('#list-pengecekan').append(
                    `
                    <div id="bermasalah">
                    <p>Posisi ` + data.posisi + ` dari ` + data.kapasitasTotal + ` pada mata kuliah ` + data.namaMataKuliah + `</p>
                    </div>
                    `
                );
            } 
        }

        for (let data of prasyaratResponse.result.data) {
            if (!data.ok) {
                ok = false;
                for (var i = 0 ; i < data.prasyarat.length ; i++) {
                    $('#list-pengecekan').append(
                        `
                        <div id="bermasalah">
                        <p>Mata Kuliah ` + data.namaMataKuliah + ` dengan prasyarat ` + data.prasyarat[i].namaMataKuliah + ` tidak terpenuhi</p>
                        </div>
                        `
                    );
                }
            }
        }

        if(ok){
            $('#list-pengecekan').append(
            `<div id="irs_tidak_bermasalah">
                <p>IRS Tidak Bermasalah</p>
            </div>`);
        }
    });

    $.ajax({
        type: "GET",
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        contentType: 'application/json',
        url: url + "/irsMahasiswa/get",
        success: function (response) {
            $(".wrapper").html('')
            kelas = response.result['kelasIrsSet']
    
            if(kelas.length <= 0) {
                $(".wrapper").html(
                `<div class="spinner-container">
                    <span class="text-center fst-italic" style="font-size: 16px">Belum ada jadwal</span>
                </div>
                `)
            }
            var height = 70 // Height of each time
            var jam_awal = 24
            var jam_akhir = 0
            for (let i = 0; i < kelas.length; i++) {
    
                kelas_obj = kelas[i]['kelas']
                jadwal_set = kelas_obj['jadwalSet']
    
                // Set Earliest Hour and Latest Hour
                for (let j = 0; j < jadwal_set.length; j++) {
                    tmp_jam_awal = new Date(jadwal_set[j]['waktuMulai']).getHours();
                    tmp_jam_akhir = new Date(jadwal_set[j]['waktuSelesai']).getHours();
                    if (tmp_jam_awal < jam_awal)
                        jam_awal = tmp_jam_awal
                    if (tmp_jam_akhir > jam_akhir)
                        jam_akhir = tmp_jam_akhir
                }
            }
    
            var row_count = 1
            for (let i = jam_awal; i <= jam_akhir; i++) {
                let hour = String(i.toString()).padStart(2, '0') + '.00'
    
                $(".wrapper").append(`<div style="grid-column: 1; grid-row: ` + row_count + `; height: ` + height + `px; line-height: ` + height + `px;" class="hour">` + hour + `</div>`)
                $(".wrapper").append(`<div style="grid-column: 2 / 8; grid-row: ` + (row_count + 1).toString() + `;" class="line"></div>`)
                row_count++
            }
    
            for (let i = 0; i < kelas.length; i++) {
    
                kelas_obj = kelas[i]['kelas']
                jadwal_set = kelas_obj['jadwalSet']
    
    
                for (let j = 0; j < jadwal_set.length; j++) {
    
                    // Set boxes column for schedule
                    if (jadwal_set[j]['hari'] == "Senin")
                        col_grid = 2
                    else if (jadwal_set[j]['hari'] == "Selasa")
                        col_grid = 3
                    else if (jadwal_set[j]['hari'] == "Rabu")
                        col_grid = 4
                    else if (jadwal_set[j]['hari'] == "Kamis")
                        col_grid = 5
                    else if (jadwal_set[j]['hari'] == "Jumat")
                        col_grid = 6
                    else if (jadwal_set[j]['hari'] == "Sabtu")
                        col_grid = 7
    
                    // Set boxes height for schedule
                    // total_minute is its default height because the default height for a row in grid is 60 -> (height = total_minute / 60 * height)
                    total_minute = 0
                    jam_mulai = new Date(jadwal_set[j]['waktuMulai']).getHours();
                    jam_selesai = new Date(jadwal_set[j]['waktuSelesai']).getHours();
                    menit_mulai = new Date(jadwal_set[j]['waktuMulai']).getMinutes();
                    menit_selesai = new Date(jadwal_set[j]['waktuSelesai']).getMinutes();
                    let last_evaluated_minute = menit_mulai
                    for (let j = jam_mulai; j < jam_selesai; j++) {
                        total_minute += 60 - last_evaluated_minute
                        last_evaluated_minute = 0
                    }
                    total_minute += menit_selesai - last_evaluated_minute
                    row_grid = (jam_mulai - jam_awal + 1).toString() + ' / ' + row_count.toString()
                    margin_top = menit_mulai / 60 * height
    
                    total_minute = total_minute / 60 * height
    
                    jamMulaiStr = String(jam_mulai.toString()).padStart(2, '0') + '.' + String(menit_mulai.toString()).padStart(2, '0')
                    jamSelesaiStr = String(jam_selesai.toString()).padStart(2, '0') + '.' + String(menit_selesai.toString()).padStart(2, '0')
    
                    var div = `
            <div style="grid-column: ` + col_grid + `; grid-row: ` + row_grid + `; height: ` + total_minute + `px; margin-top: ` + margin_top + `px" class="box">
                <div class="box-header">
                    <div class="box-header-time">` + jamMulaiStr + ` - ` + jamSelesaiStr + `</div>
                    <div class="box-header-room">` + jadwal_set[j]["ruang"] + `</div>
                </div>
                <div class="box-content">` + kelas_obj["nama"] + `</div>
            </div>
            `
                    $(".wrapper").append(div)
                }
    
            }
        },
    });
});

$('.toggle-data').click( () => { 
    if ($('.toggle-data').attr('id') === 'toggle-table') {
        $('.toggle-data').attr('id','toggle-calendar');
        $('.data-result-table').hide();
        $('.data-result-calendar').show();
    } else {
        $('.toggle-data').attr('id','toggle-table');
        $('.data-result-table').show();
        $('.data-result-calendar').hide();
    }
    
});
