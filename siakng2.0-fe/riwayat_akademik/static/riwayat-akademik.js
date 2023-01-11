const token = window.localStorage.getItem("tokenRes");

$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: url + "/riwayat-akademik/irs",
        dataType: "json",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            $("#accordion-irs").html(``);
            data = response.result

            namas = []
            ids = []
            idIrs = []
            akhirs = []
            hurufs = []        
            countIrs = 0
            countKelasIrs = 0
            for (let i in data) {
                irs = data[i]
                countIrs += 1
                kelasIrss = ``
                for (let nama of Object.keys(irs['Mata Kuliah'])) {
                    kelasIrs = irs['Mata Kuliah'][nama]
                    countKelasIrs += 1
                    kelasIrsDetail = ``
                    if (kelasIrs['Nilai Huruf'] == null && kelasIrs['Nilai Akhir'] == 0) {
                        kelasIrsDetail = `
                            <div class="matkul-detail-detail col-sm-3" id="mk-${countKelasIrs}">detail</div>
                            <div class="matkul-detail-kosong col-sm-6">Kosong</div>
                            <div class="matkul-detail-sks col-sm-3">${kelasIrs['SKS']} <sub>sks</sub></div>
                        `
                    } else {
                        kelasIrsDetail = `
                            <div class="matkul-detail-detail col-sm-3" id="mk-${countKelasIrs}">detail</div>
                            <div class="matkul-detail-huruf col-sm-3">${kelasIrs['Nilai Huruf'].length == 1 ? kelasIrs['Nilai Huruf'] + '&nbsp;' : kelasIrs['Nilai Huruf']}</div>
                            <div class="matkul-detail-angka col-sm-3">${(Math.round(kelasIrs['Nilai Akhir'] * 100) / 100).toFixed(2)}</div>
                            <div class="matkul-detail-sks col-sm-3">${kelasIrs['SKS']} <sub>sks</sub></div>
                        `
                    }
                    if (Object.keys(irs['Mata Kuliah']).indexOf(nama) > 0) {
                        kelasIrss += '<hr>'
                    }
                    namas.push(nama)
                    ids.push(kelasIrs['ID Matkul'])
                    idIrs.push(kelasIrs['ID Kelas IRS'])
                    akhirs.push((Math.round(kelasIrs['Nilai Akhir'] * 100) / 100).toFixed(2))
                    hurufs.push(kelasIrs['Nilai Huruf'])
                    kelasIrss += `
                        <div class="matkul row align-items-center">
                            <div class="col-6">
                                <div class="matkul-title">${nama}</div>
                                <div class="matkul-title-code">${kelasIrs['ID Matkul']}</div>
                            </div>
                            <div class="col-6 matkul-detail row">` + 
                                kelasIrsDetail + 
                                `
                            </div>
                        </div>
                    `
                }
                $("#accordion-irs").append(`
                    <div class="accordion-item accordion-item-${countIrs}">
                        <h2 class="accordion-header">
                        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${countIrs}" aria-expanded="true">
                            <div>
                                <div class="button-acc-term">
                                    Semester ${countIrs} | Tahun Ajaran ${irs['Tahun Ajaran']} - ${irs['Term']}
                                </div>
                                <div class="button-acc-sks">
                                    SKS: ${irs['SKSA']} | IP: ${irs['IPS'].toFixed(2)}
                                </div>
                            </div>
                        </button>
                        </h2>
                        <div id="collapse${countIrs}" class="accordion-collapse collapse">
                            <div class="accordion-body">` + 
                                kelasIrss + 
                                `
                            </div>
                        </div>
                    </div>
                `);
            };

            for (let i = 0; i < countKelasIrs; i++) {
                buttons = `
                    <button class="buttons button-mk-${i + 1}" data-bs-toggle="modal" data-bs-target="#modal${i + 1}"></button>
                `
                $(".modals").append(buttons)
                $.ajax({
                    type: "GET",
                    url: url + "/riwayat-akademik/nilai/" + idIrs[i],
                    dataType: "json",
                    contentType: "application/json",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    success: function (response) {
                        data = response.result
                        komponens = ``
                        for (let j in data) {
                            komponens += `
                                <tr>
                                    <td>${j}</td><td>${data[j]['Bobot']}%</td><td>${data[j]['Nilai']}</td>
                                </tr>
                            `
                        }
                        if (komponens == ``) {
                            komponens = `
                                <tr>
                                    <td class="no-component" colspan="3">Belum ada komponen penilaian</td>
                                </tr>
                            `
                        }
                        modals = `
                            <div class="modal fade" id="modal${i + 1}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <div class="modal-header-mk">
                                                ${namas[i]}
                                            </div>
                                            <div class="modal-header-kode">
                                                ${ids[i]}
                                            </div>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-5">
                                                    <table class="table-modal">
                                                        <tr class="table-modal-head"><th>Range</th><th>Nilai</th></tr>
                                                        <tr><td>85-100</td><td>A</td></tr>
                                                        <tr><td>80-85</td><td>A-</td></tr>
                                                        <tr><td>75-80</td><td>B+</td></tr>
                                                        <tr><td>70-75</td><td>B</td></tr>
                                                        <tr><td>65-70</td><td>B-</td></tr>
                                                        <tr><td>60-65</td><td>C+</td></tr>
                                                        <tr><td>55-60</td><td>C</td></tr>
                                                        <tr><td>40-55</td><td>D</td></tr>
                                                        <tr><td>0-40</td><td>E</td></tr>
                                                    </table>
                                                </div>
                                                <div class="col-7">
                                                    <table class="table-modal table-nilai">
                                                        <tr class="table-modal-head">
                                                            <th>Komponen</th><th>Bobot</th><th>Nilai</th>
                                                        </tr> ` + 
                                                        komponens + 
                                                        `
                                                    </table>
                                                    <br>
                                                    <div class="modal-nilai-akhir row">
                                                        <div class="col-6">Nilai Akhir:</div>
                                                        <div class="col-6">${akhirs[i]}</div>
                                                    </div>
                                                    <br>
                                                    <div class="modal-nilai-huruf row">
                                                        <div class="col-6">Nilai Huruf:</div>
                                                        <div class="col-6">${hurufs[i] == null ? '-' : hurufs[i]}</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        `
                        $(".modals").append(modals)
                    }
                });
            }
            $(".matkul-detail-detail").click(function () {
                let mk = $(this).attr("id");
                $(".button-" + mk).click();
            });
        }
    });
});
