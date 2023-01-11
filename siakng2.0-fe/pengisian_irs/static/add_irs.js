const token = window.localStorage.getItem("tokenRes");

// Selected class, everytime selectedmatkul is updated, render sticky info

// Get IRS mahasiswa (Check matkul apa saja yang sudah diambil mahasiswa)
var irsMahasiswa = selectedClass = getIrsMahasiswa()

function getMatkulMahasiswa(data) {
    var ret = []
    for (let i = 0; i < data.length; i++) {
        var isExist = false
        for (let j = 0; j < data[i]["kelasSet"].length; j++) {
            if (irsMahasiswa.includes(parseInt(data[i]["kelasSet"][j]["id"]))) {
                ret.push(data[i].id)
                break
            }
        }
    }
    return ret
}

function getIrsMahasiswa() {
    ret = []
    $.ajax({
        type: "GET",
        url: url + "/irsMahasiswa/get",
        dataType: "json",
        contentType: 'application/json',
        async: false,
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            for (const kelas of data.result.kelasIrsSet) {
                ret.push(parseInt(kelas.kelas.id));
            }
        }
    });
    return ret;
}

// Get all matkul
const data = getLatestMatkul();
function getLatestMatkul() {
    var ret;
    $.ajax({
        type: "GET",
        url: url + "/mataKuliah/getAll?periode=latest&type=add-drop",
        dataType: "json",
        contentType: 'application/json',
        async: false,
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            $(".spinner-container").remove();
            if (data.status == 200) {
                console.log(JSON.parse(data.result))
                ret = JSON.parse(data.result)
            }
            else {
                $("#summary").hide();
                $("#content").hide();
                $("#content-mobile").hide();
                $("#mobile-summary").hide();
                $("#showError").show();
            }
        }
    });
    return ret
}

// Get maks SKS
const sksMax = getSksMax();
function getSksMax() {
    var ret;
    $.ajax({
        type: "GET",
        url: url + "/pengecekan-irs/cek-sks",
        dataType: "json",
        contentType: 'application/json',
        async: false,
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            ret = data.result.data.sksMaksimum
        }
    });
    return ret
}

function showData(data) {
    var matkulMahasiswa = getMatkulMahasiswa(data)
    function constructMatkulRow(data, matkul) {
        // Construct Kapasitas
        if (data.kapasitasSaatIni >= data.kapasitasTotal)
            kapasitasDiv = `<td class="kapasitas-full">${data.kapasitasSaatIni}/${data.kapasitasTotal}</td>`
        else if (data.kapasitasSaatIni >= data.kapasitasTotal * 0.75)
            kapasitasDiv = `<td class="kapasitas-sedikit">${data.kapasitasSaatIni}/${data.kapasitasTotal}</td>`
        else
            kapasitasDiv = `<td class="kapasitas-banyak">${data.kapasitasSaatIni}/${data.kapasitasTotal}</td>`

        // Construct Waktu
        jadwalDiv = ``
        if (data.jadwalSet.length == 0) {
            jadwalDiv += `<td class="italic">Jadwal belum ditentukan</td>`;
        } else {
            jadwalDiv += `<td>`
            for (let i = 0; i < data.jadwalSet.length; i++) {
                mulai = new Date(data.jadwalSet[i].waktuMulai).toString();
                selesai = new Date(data.jadwalSet[i].waktuSelesai).toString();
                jadwalDiv += `<nobr>• ${data.jadwalSet[i].hari}, ${mulai.substring(16, 21)}-${selesai.substring(16, 21)}</nobr><br>`;
            }
            jadwalDiv += `</td>`
        }

        // Construct Ruang
        ruangDiv = ``
        if (data.jadwalSet.length == 0) {
            ruangDiv += `<td class="italic">Ruangan belum ditentukan</td>`;
        } else {
            ruangDiv += `<td>`
            for (let i = 0; i < data.jadwalSet.length; i++) {
                ruangDiv += `<nobr>${data.jadwalSet[i].ruang}</nobr><br>`;
            }
            ruangDiv += `</td>`
        }

        // Construct Pengajar
        pengajarDiv = ``
        if (data.dosenSet.length == 0) {
            pengajarDiv += `<td class="italic">Pengajar belum ditentukan</td>`;
        } else {
            pengajarDiv += `<td class="responsive-dosen-table">`
            for (let i = 0; i < data.dosenSet.length; i++) {
                for (let i = 0; i < data.dosenSet.length; i++) {
                    for (let i = 0; i < data.dosenSet.length; i++) {
                        pengajarDiv += `• ${data.dosenSet[i].namaLengkap}<br>`;
                    }
                }
                pengajarDiv += `</td>`
            }
        }

        // Return for body
        return `<tr>
                <td>${data.nama}</td>
                ${kapasitasDiv}
                ${jadwalDiv}
                ${ruangDiv}
                ${pengajarDiv}
                <td><input type="radio" name="${matkul}" value="${data.id}" id="matkul-${data.id}" ${selectedClass.includes(parseInt(data.id)) ? 'checked data-waschecked=true' : ''}></td>
            </tr>`
    }

    $("#content").html('')
    for (const matkul of data) {
        if (matkulMahasiswa.includes(matkul.id)) {
            continue;
        }
        constructedTableBody = ``
        for (let i = 0; i < matkul.kelasSet.length; i++)
            constructedTableBody += constructMatkulRow(matkul.kelasSet[i], matkul.nama)
        let constructInfoPrasyarat = ``
        if (matkul.prasyaratMataKuliahSet.length > 0) {
            constructInfoPrasyarat += `<p><strong>Prasyarat:</strong></p>`
            constructInfoPrasyarat += `<ul>`
            for (const prasyarat of matkul.prasyaratMataKuliahSet) {
                constructInfoPrasyarat += `<li>${prasyarat.nama}</li>`
            }
            constructInfoPrasyarat += `</ul>`
        }
        $('#content').append(
            `<div class="matkul-title"><h4> ${matkul.nama} &nbsp</h4>
                <div class="info">
                    <img src="/static/assets/info.svg" alt="info"/>
                    <div class='info-content'>
                        <p><strong>Jumlah SKS: ${matkul.sks}</strong></p>
                        <p><strong>Term: ${matkul.term}</strong></p>
                        ${constructInfoPrasyarat}
                    </div>
                </div>
            </div>
            ${matkul.eligible ? '' : '<h6 class="warning">Belum lulus mata kuliah prasyarat</h6>'}
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 120px">Kelas</th>
                        <th style="width: 180px">Mahasiswa/Kapasitas</th>
                        <th style="width: 120px;">Waktu</th>
                        <th style="width: 120px;">Ruang</th>
                        <th style="width: 120px;">Pengajar</th>
                        <th style="width: 120px;">Pilih</th>
                    </tr>
                </thead>
                <tbody>
                    ${constructedTableBody}
                </tbody>
            </table>
            <br>
            <br>`
        )
    }
}

function showDataMobile(data) {
    $("#content-mobile").html('')
    var matkulMahasiswa = getMatkulMahasiswa(data)
    function constructMatkulItemContentMobile(data, matkul) {
        // Construct Kapasitas
        if (data.kapasitasSaatIni >= data.kapasitasTotal)
            kapasitasDiv = `<span>(<span class="kapasitas-full">${data.kapasitasSaatIni}/${data.kapasitasTotal}</span>)</span>`
        else if (data.kapasitasSaatIni >= data.kapasitasTotal * 0.75)
            kapasitasDiv = `<span>(<span class="kapasitas-sedikit">${data.kapasitasSaatIni}/${data.kapasitasTotal}</span>)</span>`
        else
            kapasitasDiv = `<span>(<span class="kapasitas-banyak">${data.kapasitasSaatIni}/${data.kapasitasTotal}</span>)</span>`

        // Construct Waktu
        jadwalDiv = ``
        if (data.jadwalSet.length == 0) {
            jadwalDiv += `<p class="italic">&nbsp;&nbsp;Jadwal belum ditentukan</p>`;
        } else {
            for (let i = 0; i < data.jadwalSet.length; i++) {
                mulai = new Date(data.jadwalSet[i].waktuMulai).toString();
                selesai = new Date(data.jadwalSet[i].waktuSelesai).toString();
                jadwalDiv += `<p>&nbsp;&nbsp;• ${data.jadwalSet[i].hari}, ${mulai.substring(16, 21)}-${selesai.substring(16, 21)}</p>`;
            }
        }

        // Construct Pengajar
        pengajarDiv = ``
        if (data.dosenSet.length == 0) {
            pengajarDiv += `<p class="italic">&nbsp;&nbsp;Pengajar belum ditentukan</p>`;
        } else {
            for (let i = 0; i < data.dosenSet.length; i++) {
                pengajarDiv += `<p>&nbsp;&nbsp;• ${data.dosenSet[i].namaLengkap}</p>`;
            }
        }

        // Return for body
        return `
            <input type="radio" name="${matkul}" value="${data.id}" id="matkul-${data.id}" ${selectedClass.includes(parseInt(data.id)) ? 'checked' : ''}>
            <h5>${data.nama}&nbsp;&nbsp;${kapasitasDiv}</h5>
            <p class="subtitle">Jadwal</p>
            ${jadwalDiv}
            <p class="subtitle">Pengajar</p>
            ${pengajarDiv}
            <br>
        `
    }
    for (const matkul of data) {
        if (matkulMahasiswa.includes(matkul.id)) {
            continue;
        }
        constructedDiv = ''
        for (let i = 0; i < matkul.kelasSet.length; i++)
            constructedDiv += constructMatkulItemContentMobile(matkul.kelasSet[i], matkul.nama)
        $('#content-mobile').append(
            `<br>
            <div class="matkul-title"><h4> ${matkul.nama} &nbsp</h4><img src="/static/assets/info.svg" alt="info"/></div>
            ${matkul.eligible ? '' : '<h6 class="warning">Belum lulus mata kuliah prasyarat</h6>'}
            <div class="mobile-card">
                <br>
                <div class="mobile-card-content">
                    ${constructedDiv}
                </div>
            </div>
            <br>`
        )
    }
}

function deleteSelectedMatkul(val) {
    selectedClass = selectedClass.filter(function (item) {
        return item != val
    });
    $(`#matkul-${val}`).prop('checked', false);
    $(`#matkul-${val}`).data('waschecked', false);
    renderSummary();
}

function addSelectedMatkul(name, val) {
    for (const matkul of data) {
        if (matkul.nama == name) {
            for (const kelas of matkul.kelasSet) {
                selectedClass = selectedClass.filter(function (item) {
                    return item != kelas.id
                }); // Remove every class associated from selectedClass List
            }
        }
    }
    selectedClass.push(val);
    renderSummary();
}

function buttonFunction(data) {
    for (const matkul of data) {
        $(`input[type='radio'][name='${matkul.nama}']`).click(function () {
            if ($(this).data('waschecked')) {
                deleteSelectedMatkul(parseInt($(this).val()))
            }
            else {
                addSelectedMatkul(matkul.nama, parseInt($(this).val()))
                $(`input[type='radio'][name='${matkul.nama}']`).data('waschecked', false) // Reset other checked value
                $(this).data('waschecked', true)
            }
        })
    }
}

function renderSummary() {
    // Get Selected Classes and Matkul as List
    selectedClassLst = []
    selectedMatkul = []
    for (const id of selectedClass) {
        for (const matkul of data) {
            let hasBeenFound = false;
            for (const kelas of matkul.kelasSet) {
                if (id == kelas.id) {
                    selectedClassLst.push(kelas)
                    hasBeenFound = true
                    break
                }
            }
            if (hasBeenFound) {
                selectedMatkul.push({ 'id': matkul.id, 'eligible': matkul.eligible, 'nama': matkul.nama })
                break
            }
        }
    }

    // Render Table Summary
    renderedTableBody = ``
    for (const kelas of selectedClassLst) {
        // Construct Waktu
        jadwalDiv = `<td>`
        for (let i = 0; i < kelas.jadwalSet.length; i++) {
            mulai = new Date(kelas.jadwalSet[i].waktuMulai).toString();
            selesai = new Date(kelas.jadwalSet[i].waktuSelesai).toString();
            jadwalDiv += `<nobr>• ${kelas.jadwalSet[i].hari}, ${mulai.substring(16, 21)}-${selesai.substring(16, 21)}</nobr><br>`;
        }
        jadwalDiv += `</td>`

        // Construct Pengajar
        pengajarDiv = `<td class="responsive-dosen-table">`
        for (let i = 0; i < kelas.dosenSet.length; i++) {
            pengajarDiv += `• ${kelas.dosenSet[i].namaLengkap}<br>`;
        }
        pengajarDiv += `</td>`

        if (irsMahasiswa.includes(parseInt(kelas.id))) {
            renderedTableBody += `
                        <tr id="class-table-${kelas.id}">
                            <td>${kelas.nama}</td>
                            ${jadwalDiv}
                            ${pengajarDiv}
                        </tr>
                    `
        }
        else {
            renderedTableBody += `
                            <tr id="class-table-${kelas.id}">
                                <td>${kelas.nama}</td>
                                ${jadwalDiv}
                                ${pengajarDiv}
                                <td><button onclick="deleteSelectedMatkul(${kelas.id})"><i class="fa fa-trash" aria-hidden="true"></i></button></td>
                            </tr>
                        `
        }
    }
    document.getElementById("summary-class-table-body").innerHTML = renderedTableBody
    if (renderedTableBody == ``) {
        document.getElementById("jadwal-kosong").classList.remove("invisible")
    } else {
        document.getElementById("jadwal-kosong").classList.add("invisible")
    }

    // Render Pengecekan
    isSafe = true
    renederedPengecekan = ``

    // Render Pengecekan Daya Tampung
    for (const kelas of selectedClassLst) {
        if (kelas.kapasitasSaatIni >= kelas.kapasitasTotal) {
            isSafe = false
            renederedPengecekan += `<p>• Posisi di kelas <strong>${kelas.nama}</strong> melebihi kapasitas kelas.</p>`
        }
    }

    // Render Pengecekan SKS
    jumlahSks = 0
    for (const kelas of selectedClassLst) {
        jumlahSks += kelas.sks
        if (jumlahSks > sksMax) {
            isSafe = false
            renederedPengecekan += `<p>• SKS melebihi batas maksimal sks! Batas maksimal: <strong>${sksMax}</strong> SKS.</p>`
        }
    }
    document.getElementById("jumlah-sks").innerHTML = jumlahSks
    document.getElementById("jumlah-sks-mobile").innerHTML = jumlahSks
    document.getElementById("jumlah-matkul-mobile").innerHTML = selectedClassLst.length

    // Render Pengecekan Prasyarat
    for (const matkul of selectedMatkul) {
        if (matkul.eligible == 0) {
            isSafe = false
            renederedPengecekan += `<p>• Prasyarat untuk mata kuliah <strong>${matkul.nama}</strong> belum terpenuhi.</p>`
        }
    }

    // Render Pengecekan Jadwal
    let kelasByDay = { 0: [], 1: [], 2: [], 3: [], 4: [], 5: [] }
    function insertToKelasByDay(nama, jadwal, index) {
        kelasByDay[index].push({ 'nama': nama, 'waktuMulai': jadwal.waktuMulai, 'waktuSelesai': jadwal.waktuSelesai })
    }
    for (const kelas of selectedClassLst) {
        // Check for the same day
        for (const jadwal of kelas.jadwalSet) {
            if (jadwal.hari == 'Senin')
                insertToKelasByDay(kelas.nama, jadwal, 0)
            else if (jadwal.hari == 'Selasa')
                insertToKelasByDay(kelas.nama, jadwal, 1)
            else if (jadwal.hari == 'Rabu')
                insertToKelasByDay(kelas.nama, jadwal, 2)
            else if (jadwal.hari == 'Kamis')
                insertToKelasByDay(kelas.nama, jadwal, 3)
            else if (jadwal.hari == 'Jumat')
                insertToKelasByDay(kelas.nama, jadwal, 4)
            else if (jadwal.hari == 'Sabtu')
                insertToKelasByDay(kelas.nama, jadwal, 5)
        }
    }
    // Check conflict
    conflictedStr = []
    for (const [key, value] of Object.entries(kelasByDay)) {
        for (let i = 0; i < value.length; i++) {
            mulai = new Date(value[i].waktuMulai);
            selesai = new Date(value[i].waktuSelesai);
            startSec1 = mulai.getHours() * 3600 + mulai.getMinutes() * 60 + mulai.getSeconds()
            endSec1 = selesai.getHours() * 3600 + selesai.getMinutes() * 60 + selesai.getSeconds()
            for (let j = 0; j < value.length; j++) {
                if (value[i].nama != value[j].nama) {
                    mulai = new Date(value[j].waktuMulai);
                    selesai = new Date(value[j].waktuSelesai);
                    startSec2 = mulai.getHours() * 3600 + mulai.getMinutes() * 60 + mulai.getSeconds()
                    endSec2 = selesai.getHours() * 3600 + selesai.getMinutes() * 60 + selesai.getSeconds()
                    if (((startSec1 <= startSec2) & !(endSec1 <= startSec2) & (endSec1 <= endSec2)) | ((startSec2 <= startSec1) & !(endSec2 <= startSec1) & (endSec2 <= endSec1))) {
                        isSafe = false
                        isWritten = false
                        strError1 = `Terdapat konflik: <strong>${value[i].nama}</strong> dengan <strong>${value[j].nama}</strong>`
                        strError2 = `Terdapat konflik: <strong>${value[j].nama}</strong> dengan <strong>${value[i].nama}</strong>`
                        for (const strError of conflictedStr) {
                            if ((strError == strError1) | (strError == strError2)) {
                                isWritten = true
                            }
                        }
                        if (!isWritten) {
                            conflictedStr.push(strError1)
                        }
                    }
                }
            }
        }
    }
    for (const str of conflictedStr)
        renederedPengecekan += `<p>• ${str}</p>`

    // Check for mobile
    let statusMobile
    if (!isSafe) {
        statusMobile = `Pengecekan: Terdapat masalah`
        $('#mobile-summary').css('background-color', '#DB3737')
    } else {
        statusMobile = `Pengecekan: Tidak terdapat masalah`
        $('#mobile-summary').css('background-color', '#3D5A80')
    }
    document.getElementById("status-pengecekan-mobile").innerHTML = statusMobile

    // renederedPengecekan += `</ul>`

    if (isSafe) {
        // Add mobile status
        renederedPengecekan = `Tidak terdapat masalah dalam pemilihan IRS`
    } else {
        // Add mobile status
    }
    document.getElementById("summary-pengecekan").innerHTML = renederedPengecekan

    // Check height of summary
    const summaryHeight = Math.max(document.getElementById("class").offsetHeight, document.getElementById("pengecekan").offsetHeight);

    // Render for modal confirmation
    document.getElementById("modal-table").innerHTML = `<table>${document.getElementById("summary-class-table").innerHTML}</table>`
    document.getElementById("modal-pengecekan").innerHTML = `
        <h4>Pengecekan:</h4>
        ${document.getElementById("summary-pengecekan").innerHTML}
    `
    document.getElementById("simpan").removeAttribute("disabled")
    document.getElementById("confirm-simpan").removeAttribute("disabled")
    document.getElementById("batal-simpan").removeAttribute("disabled")
    document.getElementById("simpan-mobile").removeAttribute("disabled")
    document.getElementById("simpan-mobile").innerHTML = `<img src="/static/assets/arrow.svg" alt="expand" />`
}

$(document).ready(function () {
    if (data) {
        $("#showIsiIrs").show();

        // Save button
        document.getElementById("simpan").onclick = function () {
            if (selectedClass.length == 0) {
                alert('Pilih mata kuliah untuk mengisi IRS!');
                return
            }

            document.getElementById("myModal").style.display = 'block'
        }

        document.getElementById("simpan-mobile").onclick = function () {
            if (selectedClass.length == 0) {
                alert('Pilih mata kuliah untuk mengisi IRS!');
                return
            }

            document.getElementById("myModal").style.display = 'block'
        }

        document.getElementById("batal-simpan").onclick = function () {
            document.getElementById("myModal").style.display = "none";
        }

        document.getElementById("confirm-simpan").onclick = function () {
            dataPost = []
            let iterateTimes = selectedClass.length
            for (const id of selectedClass) {
                for (const matkul of data) {
                    breakMatkulItr = false
                    for (const kelas of matkul.kelasSet) {
                        if (id == kelas.id) {
                            dataPost.push(kelas)
                            breakMatkulItr = true
                            break
                        }
                    }
                    if (breakMatkulItr)
                        break
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
                    window.location = redirectUrl + "/main/CoursePlan/CoursePlanViewSummary";
                },
            });
        }

        var toggledSummary = false;
        document.getElementById("toggle-summary").onclick = function () {
            if (!toggledSummary) {
                toggledSummary = true
                $('#summary').css('max-height', 'none')
                $('#summary').css('overflow-y', 'scroll')
                $('#class-content').css('max-height', 'none')
                $('#summary-pengecekan').css('max-height', 'none')
                $('#toggle-summary svg').css('transform', 'rotate(0deg)')
            } else {
                toggledSummary = false
                $('#summary').css('max-height', '300px')
                $('#summary').css('overflow-y', 'hidden')
                $('#class-content').css('max-height', '240px')
                $('#summary-pengecekan').css('max-height', '200px')
                $('#toggle-summary-svg').css('transform', 'rotate(180deg)')
            }
        }

        window.onclick = function (e) {
            if (e.target == document.getElementById("myModal")) {
                document.getElementById("myModal").style.display = "none";
            }
        }

        // Responsive view change (desktop/mobile)
        function changeResolution(screenWidth) {
            if (screenWidth.matches) { // If media query matches
                document.getElementById("content").innerHTML = ``
                showDataMobile(data);
                buttonFunction(data);

            } else {
                document.getElementById("content-mobile").innerHTML = ``
                showData(data);
                buttonFunction(data);
            }
            renderSummary();
        }
        var screenWidth = window.matchMedia("(max-width: 920px)")
        changeResolution(screenWidth) // Call listener function at run time
        screenWidth.addListener(changeResolution) // Attach listener function on state changes
    }
});
