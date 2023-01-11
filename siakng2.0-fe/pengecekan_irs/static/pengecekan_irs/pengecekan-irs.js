// dummy data
// const prasyarat = { "data": [ { "namaMataKuliah": "Dummy", "idMataKuliah": "DDDD000000", "prasyarat": [ { "namaMataKuliah": "Mock", "idMataKuliah": "MMMM000000", "lulus": true } ], "ok": true }, { "namaMataKuliah": "Spy", "idMataKuliah": "SSSS000000", "prasyarat": [ { "namaMataKuliah": "Dummy", "idMataKuliah": "DDDD000000", "lulus": false }, { "namaMataKuliah": "Mock", "idMataKuliah": "MMMM000000", "lulus": true } ], "ok": false } ] }
// const kapasitas = { "data": [ { "namaKelas": "Kelas Dummy A", "namaMataKuliah": "Dummy", "idMataKuliah": "DDDD000000", "posisi": 75, "kapasitasTotal": 70, "ok": false }, { "namaKelas": "Kelas Spy A", "namaMataKuliah": "Spy", "idMataKuliah": "SSSS000000", "posisi": 32, "kapasitasTotal": 32, "ok": true } ] }
// const jadwal = { "data": [ { "namaKelas": "Kelas Dummy A", "namaMataKuliah": "Dummy", "idMataKuliah": "DDDD000000", "konflik": [ { "namaKelas": "Kelas Spy A", "namaMataKuliah": "Spy", "idMataKuliah": "SSSS000000" } ], "ok": false }, { "namaKelas": "Kelas Spy A", "namaMataKuliah": "Spy", "idMataKuliah": "SSSS000000", "konflik": [ { "namaKelas": "Kelas Dummy A", "namaMataKuliah": "Dummy", "idMataKuliah": "DDDD000000" } ], "ok": false }, { "namaKelas": "Kelas Mock A", "namaMataKuliah": "Mock", "idMataKuliah": "MMMM000000", "konflik": [], "ok": true } ] }
// const sks = { "data": { "periode": 2023, "ipAcuan": 3.14, "sksDiambil": 15, "sksMaksimum": 18, "ok": true } }

function buildSKS(sksData) {
    var retDiv = `
    <p>IP acuan ` + sksData['ipAcuan'].toFixed(2) + ` dari tahun ajaran ` + sksData['periode'] + `</p>
    <p>SKS Maksimum ` + sksData['sksMaksimum'] + ` SKS berdasarkan IP acuan ` + sksData['ipAcuan'].toFixed(2) + `</p>`
    if (sksData['ok'])
        retDiv += `<p class="no-error">SKS diambil ` + sksData['sksDiambil'] + ` SKS</p>`
    else
        retDiv += `<p class="error">SKS diambil ` + sksData['sksDiambil'] + ` SKS</p>`
    return retDiv
}

function buildKapasitas(kapasitasData) {
    var isOk = true
    var retDiv = ''
    for (let i = 0; i < kapasitasData.length; i++) {
        var data = kapasitasData[i]
        if (!data['ok']) {
            isOk = false
            retDiv += `<p class="notOk">` + data['idMataKuliah'] + ` - ` + data['namaMataKuliah'] + `; ` + data['namaKelas'] + ` : ` + data['posisi'] + `/` + data['kapasitasTotal'] + `</p>`
        } else {
            retDiv += `<p>` + data['idMataKuliah'] + ` - ` + data['namaMataKuliah'] + `; ` + data['namaKelas'] + ` : ` + data['posisi'] + `/` + data['kapasitasTotal'] + `</p>`
        }
    }
    if (!isOk)
        retDiv += '<p class="error" style="font-size: 16px;">Terdapat konflik pada kapasitas</p>'
    else
        retDiv += '<p class="no-error" style="font-size: 16px;">Tidak ada konflik pada kapasitas</p>'
    return retDiv
}

function buildPrasyarat(prasyaratData) {
    var ada = false
    for (let i = 0; i < prasyaratData.length; i++) {
        var data = prasyaratData[i]
        if(data['prasyarat'].length != 0) {
            ada = true
        }
    }
    if(!ada) {
        return '<p class="no-error" style="font-size: 16px;">Tidak ada mata kuliah yang memiliki prasyarat</p>';
    }
    var retDiv = `
    <div class="table-responsive table-prasyarat">
        <table  aria-describedby="jadwalKuliah">
            <thead>
                <tr>
                    <th id="th-pengecekan" scope="col">Kode MK</th>
                    <th id="th-pengecekan" scope="col">Nama MK</th>
                    <th id="th-pengecekan" scope="col">Prasyarat</th>
                    <th id="th-pengecekan" scope="col">Status</th>
                </tr>
            </thead>`

    for (let i = 0; i < prasyaratData.length; i++) {
        var data = prasyaratData[i]
        for (let j = 0; j < data['prasyarat'].length; j++) {
            var prasyaratTmp = data['prasyarat'][j]
            retDiv += `
                <tbody>
                    <tr>
                        <td id="td-pengecekan" >` + data['idMataKuliah'] + `</td>
                        <td id="td-pengecekan" >` + data['namaMataKuliah'] + `</td>
                        <td id="td-pengecekan" >` + prasyaratTmp['idMataKuliah'] + ` - ` + prasyaratTmp['namaMataKuliah'] + `</td>
                        <td id="td-pengecekan" >`
            if (prasyaratTmp['lulus']) {
                retDiv += `<div class="lulus">Lulus</div>`
            } else {
                retDiv += `<div class="tidak-lulus">Tidak Lulus</div>`
            }
            retDiv += `
                        </td>
                    </tr>
                </tbody>`
        }
    }

    retDiv += `</table> </div>`
    return retDiv
}

function buildJadwal(jadwalData) {
    var jumlahJadwal = jadwalData.length
    var retDiv = '<p>Memeriksa ' + jumlahJadwal + ' jadwal untuk kemungkinan konflik</p>'

    var isOk = true
    for (let i = 0; i < jumlahJadwal; i++) {
        if (!jadwalData[i]['ok']) {
            isOk = false
            break
        }
    }
    if (isOk) {
        retDiv += '<p class="no-error">Tidak ada konflik pada jadwal</p>'
        return retDiv
    }

    var conflicted = []
    for (let i = 0; i < jumlahJadwal; i++) {
        var data = jadwalData[i]
        for (let j = 0; j < data['konflik'].length; j++) {
            var konflik = data['konflik'][j]
            // only display if id1 < id2 so not appear twice (id1 id2 and id2 id1)
            if (data['idMataKuliah'] < konflik['idMataKuliah']) {
                conflicted.push([data['idMataKuliah'] + ' - ' + data['namaMataKuliah'], konflik['idMataKuliah'] + ' - ' + konflik['namaMataKuliah']])
            }
        }
    }
    conflicted.sort();
    retDiv += '<p class="error">Terdapat ' + conflicted.length + ' konflik pada jadwal</p>'
    retDiv += `
<div class="table-responsive table-jadwal">
    <table  aria-describedby="jadwalKuliah">
        <thead>
            <tr>
                <th id="th-pengecekan" scope="col">Kelas</th>
                <th id="th-pengecekan" scope="col">Konflik</th>
            </tr>
        </thead>`
    for (let i = 0; i < conflicted.length; i++) {
        var isi = conflicted[i]
        retDiv += `
                <tbody>
                    <tr>
                        <td id="td-pengecekan">` + isi[0] + `</td>
                        <td id="td-pengecekan" class="notOk">` + isi[1] + `</td>
                    </tr>
                </tbody>`
    }

    retDiv += `</table> </div>`



    return retDiv
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
        url: url + "/pengecekan-irs/cek-sks",
        success: function (response) {
            const sks = document.getElementById("sks")
            sks.removeChild(sks.lastElementChild)
            sks.innerHTML += buildSKS(response.result['data'])
        },
    });

    $.ajax({
        type: "GET",
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        contentType: 'application/json',
        url: url + "/pengecekan-irs/cek-jadwal",
        success: function (response) {
            const jadwal = document.getElementById("jadwal")
            jadwal.removeChild(jadwal.lastElementChild)
            jadwal.innerHTML += buildJadwal(response.result['data'])
        },
    });

    $.ajax({
        type: "GET",
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        contentType: 'application/json',
        url: url + "/pengecekan-irs/cek-kapasitas",
        success: function (response) {
            const kapasitas = document.getElementById("kapasitas")
            kapasitas.removeChild(kapasitas.lastElementChild)
            kapasitas.innerHTML += buildKapasitas(response.result['data'])
        },
    });

    $.ajax({
        type: "GET",
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        contentType: 'application/json',
        url: url + "/pengecekan-irs/cek-prasyarat",
        success: function (response) {
            const prasyarat = document.getElementById("prasyarat")
            prasyarat.removeChild(prasyarat.lastElementChild)
            prasyarat.innerHTML += buildPrasyarat(response.result['data'])
        },
    });
})