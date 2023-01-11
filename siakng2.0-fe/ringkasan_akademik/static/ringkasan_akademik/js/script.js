const NILAI_HURUF = ['A', 'A-', 'B+', 'B', 'B-', 'C+', 'C', 'D', 'E'];

let sep = ' : ';

function configureDomElements(){
    const dataNode = document.querySelector(".data-mahasiswa");

    if (document.body.clientWidth <= 360) {
        sep = '';
        dataNode.querySelector(".detail").classList.add("d-flex", "flex-column");
        dataNode.querySelector(".nama-pa").classList.add("d-flex", "flex-column");
        dataNode.querySelector(".nip-pa").classList.add("d-flex", "flex-column");
    }

    dataNode.querySelector(".detail").innerHTML = 
        `<span>NPM${sep}</span>` + dataNode.querySelector(".detail").innerHTML;
    dataNode.querySelector(".nama-pa").innerHTML =
        `<span><i class="fas fa-user"></i> Pembimbing Akademik${sep}</span>` + dataNode.querySelector(".nama-pa").innerHTML;
    dataNode.querySelector(".nip-pa").innerHTML =
        `<span>NIP${sep}</span>` + dataNode.querySelector(".nip-pa").innerHTML;
}

function populatedataSection(data) {
    const dataNode = document.querySelector(".data-mahasiswa");

    dataNode.querySelector(".name").innerText = data.nama;
    dataNode.querySelector(".detail").innerHTML =
        `<span>NPM${sep}</span><span>${data.npm} | ${data.programStudi}, ${data.programPendidikan}</span>`;

    dataNode.querySelector(".ips").innerHTML = `<span>IPS</span><span>${data.ips}</span>`;
    dataNode.querySelector(".ipk").innerHTML = `<span>IPK</span><span>${data.ipk}</span>`;
    dataNode.querySelector(".sks-lulus").innerHTML = `<span>SKS Lulus</span><span>${data.sksL}</span>`;
    dataNode.querySelector(".total-mutu").innerHTML = `<span>Total Mutu</span><span>${data.totalMutu}</span>`;

    dataNode.querySelector(".nama-pa").innerHTML =
        `<span><i class="fas fa-user"></i> Pembimbing Akademik${sep}</span><span>${data.pembimbingAkademik.nama}</span>`
    dataNode.querySelector(".nip-pa").innerHTML = `<span>NIP${sep}</span><span>${data.pembimbingAkademik.nip}</span>`

    dataNode.querySelector(".tahun-ajaran").innerHTML = `<span>Tahun Ajaran</span><span>${data.tahunAjaran}</span>`;
    dataNode.querySelector(".semester").innerHTML = `<span>Semester</span><span>${data.semester}</span>`;
    dataNode.querySelector(".status-akademik").innerHTML =`<span>Status Akademik</span><span>${data.statusAkademik}</span>`;
}

function populateRiwayatAkademikTable(irsList) {
    const tbody = document.querySelector(".riwayat-table tbody");
    tbody.innerHTML = '';
    let colored = true;

    for (const irs of irsList) {
        const tdRest = (!irs.nilaiDefined ?
            `<td colspan="11" class="message">Tidak ada kelas atau IRS belum disetujui (status akademis: Kosong)</td>` :
            `<td>${irs.mk}</td>
            <td>${irs.semester.sksA}</td>
            <td>${irs.semester.sksL}</td>
            <td>${irs.semester.mutu}</td>
            <td>${irs.semester.ip}</td>
            <td>${irs.kumulatif.sksA}</td>
            <td>${irs.kumulatif.sksL}</td>
            <td>${irs.kumulatif.mutu}</td>
            <td>${irs.kumulatif.ipt}</td>
            <td>${irs.kumulatif.ipk}</td>
            <td>${irs.kumulatif.sksDpo}</td>`
        )
        const tr = `<tr ${colored ? 'class="colored"' : ''}>
            <td>${irs.tahun}</td>
            <td>${irs.term}</td>
            ${tdRest}
        </tr>`
        tbody.innerHTML += tr;
        colored = !colored;
    }
}

function generatePrestasiChart(semester, ip, ipk) {
    return new Chart("prestasi-chart", {
        type: "line",
        data: {
            labels: semester,
            datasets: [
                {
                    label: "IPK",
                    data: ipk,
                    borderColor: "orange",
                    backgroundColor: "transparent",
                    pointBackgroundColor: "orange"
                },
                {
                    label: "IP",
                    data: ip,
                    borderColor: "green",
                    backgroundColor: "transparent",
                    pointBackgroundColor: "green"
                }
            ]
        },
        options: {
            scales: {
                x: {
                    title: {
                        display: true,
                        text: "Semester",
                        font: {
                            size: 16,
                            weight: 700
                        }
                    },
                    grid: {
                        borderWidth: 2,
                        borderColor: "#304D6C"
                    }
                },
                y: {
                    beginAtZero: true,
                    grid: {
                        borderWidth: 2,
                        borderColor: "#304D6C"
                    }
                }
            },
            plugins: {
                legend: {
                    position: "right",
                    labels: {
                        usePointStyle: true,
                        pointStyle: "circle",
                        boxWidth: 4,
                        font: {
                            weight: 700
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        title: context => `Semester ${context[0].label}`
                    }
                }
            }
        }
    });
}

function getIrsList() {
    // return [{tahun: "2020/2021",term: 1,nilaiDefined: false,message: "Tidak ada kelas atau IRS belum disetujui (status akademis: Kosong)"},{tahun: "2019/2020",term: 2,nilaiDefined: true,mk: 5,semester: {sksA: 24,sksL: 24,mutu: 100,ip: 3.42},kumulatif: {sksA: 44,sksL: 44,mutu: 200,ipt: 3.42,ipk: 3.55,sksDpo: 0}},{tahun: "2019/2020",term: 1,nilaiDefined: true,mk: 6,semester: {sksA: 20,sksL: 20,mutu: 100,ip: 3.68},kumulatif: {sksA: 20,sksL: 20,mutu: 100,ipt: 3.68,ipk: 3.68,sksDpo: 0}}];
    $.ajax({
        url: url + "/ringkasan-akademik/irs",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        type: 'GET',
        cache: false,
        success: function (response) {
            retData = []
            data = response.result
            keyArray = Object.keys(data);
            for (let i = 0; i < keyArray.length; i++) {
                dataTmp = data[keyArray[i]]
                dctTmp = {}
                dctTmp['tahun'] = dataTmp['Periode Tahun']
                dctTmp['term'] = dataTmp['Term']
                dctTmp['nilaiDefined'] = dataTmp['Nilai Defined']
                dctTmp['mk'] = dataTmp['MK']
                dctTmp['semester'] = {
                    sksA: dataTmp['SKSA Semester'],
                    sksL: dataTmp['SKSL Semester'],
                    mutu: dataTmp['Total Mutu'].toFixed(2),
                    ip: dataTmp['IP'].toFixed(2)
                }
                dctTmp['kumulatif'] = {
                    sksA: dataTmp['SKSA Kumulatif'],
                    sksL: dataTmp['SKSL Kumulatif'],
                    mutu: dataTmp['Total Mutu Kumulatif'].toFixed(2),
                    ipt: dataTmp['IPT'].toFixed(2),
                    ipk: dataTmp['IPK'].toFixed(2),
                    sksDpo: dataTmp['SKSL Kumulatif']
                }
                retData.push(dctTmp)
            }
            populateRiwayatAkademikTable(retData)
        },
    });
}

function getDataMahasiswa() {
    // return { nama: "Eren Yeager", npm: "1906272788", programStudi: "Ilmu Komputer", programPendidikan: "S1 Reguler", ips: 3.69, ipk: 3.71, sksL: 102, totalMutu: 500, pembimbingAkademik: { nama: "Grisha Yeager S.Kom", nip: "19610123123112312001" }, tahunAjaran: "2021/2022 Genap", semester: 6, statusAkademik: "AKTIF" };
    $.ajax({
        url: url + "/ringkasan-akademik/mahasiswa",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        type: 'GET',
        cache: false,
        success: function (response) {
            data = response.result
            retData = {
                nama: data['Nama'],
                npm: data['NPM'],
                programStudi: data['Program Studi'].split(',')[0],
                programPendidikan: data['Program Studi'].split(',')[1].substring(1,),
                ips: data['IPS'].toFixed(2),
                ipk: data['IPK'].toFixed(2),
                sksL: data['SKS Lulus'],
                totalMutu: data['Total Mutu'].toFixed(2),
                pembimbingAkademik: {
                    nama: data['Pembimbing Akademik'],
                    nip: data['NIP Pembimbing Akademik']
                },
                tahunAjaran: data['Tahun Ajaran'],
                semester: data['Semester'],
                statusAkademik: data['Status Akademik']
            }
            populatedataSection(retData)
        },
    });
}

function generateNilaiChart(nilaiHurufFreq) {
    return new Chart("nilai-chart", {
        type: "bar",
        data: {
            labels: NILAI_HURUF,
            datasets: [
                {
                    data: nilaiHurufFreq,
                    backgroundColor: "#304D6C",
                    borderRadius: {
                        topLeft: 5,
                        topRight: 5
                    }
                }
            ]
        },
        options: {
            scales: {
                x: {
                    title: {
                        display: true,
                        text: "Nilai Huruf",
                        font: {
                            size: 16,
                            weight: 700
                        }
                    },
                    grid: {
                        display: false,
                        borderWidth: 2,
                        borderColor: "#304D6C"
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: "Jumlah",
                        font: {
                            size: 16,
                            weight: 700
                        }
                    },
                    grid: {
                        borderWidth: 2,
                        borderColor: "#304D6C"
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
}

function getPrestasiData() {
    // return { semester: [1, 2, 3, 4], ip: [4.0, 3.52, 3.1, 3.79], ipk: [4.0, 3.76, 3.54, 3.6] }
    $.ajax({
        url: url + "/ringkasan-akademik/ip",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        type: 'GET',
        success: function (response) {
            const prestasiAkademik = document.querySelector(".prestasi-akademik");
            prestasiAkademik.removeChild(prestasiAkademik.querySelector(".spinner-container"));
            prestasiAkademik.innerHTML += `<canvas id="prestasi-chart" height="340" style="height: 340px"></canvas>`;
            data = response.result
            dctReturn = {}
            dctReturn['semester'] = []
            dctReturn['ip'] = []
            dctReturn['ipk'] = []
            for (let i = 0; i < data['IP'].length; i++) {
                dctReturn['semester'].push(i + 1)
                dctReturn['ip'].push(data['IP'][i].toFixed(2))
                dctReturn['ipk'].push(data['IPK'][i].toFixed(2))
            }
            generatePrestasiChart(dctReturn.semester, dctReturn.ip, dctReturn.ipk)
        },
    });
}

function getNilaiData() {
    // return [10, 5, 8, 1, 0, 0, 2, 3, 0];
    // Nilai index list: ['A', 'A-', 'B+', 'B', 'B-', 'C+', 'C', 'D', 'E']
    $.ajax({
        url: url + "/ringkasan-akademik/nilai",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        type: 'GET',
        success: function (response) {
            const statistikNilai = document.querySelector(".statistik-nilai");
            statistikNilai.removeChild(statistikNilai.querySelector(".spinner-container"));
            statistikNilai.innerHTML += `<canvas id="nilai-chart" height="340" style="height: 340px"></canvas>`;
            data = response.result
            lstReturn = new Array(NILAI_HURUF.length).fill(0);
            lstReturn[0] = data['A']
            lstReturn[1] = data['A-']
            lstReturn[2] = data['B+']
            lstReturn[3] = data['B']
            lstReturn[4] = data['B-']
            lstReturn[5] = data['C+']
            lstReturn[6] = data['C']
            lstReturn[7] = data['D']
            lstReturn[8] = data['E']
            generateNilaiChart(lstReturn)
        },
    });
}

function getPhotoUrlMahasiswa() {
    $.ajax({
        type: "GET",
        url: url + '/mahasiswa/detail',
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        contentType: 'application/json',
        success: function (response) {
            const idImage = response.result.urlFoto.substring(response.result.urlFoto.indexOf("/d/")+3,response.result.urlFoto.indexOf("/view"));
            const sourceImage = "https://drive.google.com/uc?export=view&id="+idImage;
            document.getElementById("photo-profile").setAttribute("src",sourceImage);
            const photo = $(`<img class="profile-picture" src="${sourceImage}"></img>`);
            photo.on("load", () => $('#photo-profile').html(photo));
        }
      });
}


async function init() {
    configureDomElements();

    await new Promise(resolve => setTimeout(resolve, 1000));
    Chart.defaults.color = "#304D6C";
    Chart.defaults.font.family = "Poppins, Roboto, 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif";
    Chart.defaults.font.weight = 700;

    getPhotoUrlMahasiswa();
    getDataMahasiswa();
    getIrsList();
    getPrestasiData();
    getNilaiData();
}

init()