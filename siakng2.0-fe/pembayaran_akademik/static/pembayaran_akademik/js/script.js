String.prototype.toTitleCase = function(){
	let newStrings = this.toLowerCase().split('_');
	newStrings = newStrings.filter(str => str.length > 0);
	newStrings = newStrings.map(str => str[0].toUpperCase() + str.slice(1));
	return newStrings.join(' ');
}

function getStatusStyle(status){
	let statusStyle;
	switch(status){
		case "Lunas":
			statusStyle = "ok";
			break;
		case "Belum Lunas":
			statusStyle = "warning";
			break;
		default:
			statusStyle = "unknown";
	}
	return statusStyle;
}

function initializeLatestStatus(data){
	const table = document.querySelector(".payment .latest-status-table");
	const tagihan = new Intl.NumberFormat("id-ID", { style: "currency", currency: data.mataUang, maximumFractionDigits: 0 }).format(data.tagihan);

	table.innerHTML = 
	`
	<div class="latest-status-table-row">
		<div class="key">Nama:</div>
		<div class="value">${data.nama}</div>
	</div>
	<div class="latest-status-table-row">
		<div class="key">NPM:</div>
		<div class="value">${data.npm}</div>
	</div>
	<div class="latest-status-table-row">
		<div class="key">Periode:</div>
		<div class="value">${data.periode}</div>
	</div>
	<div class="latest-status-table-row">
		<div class="key">Tagihan:</div>
		<div class="value">${tagihan}</div>
	</div>
	<div class="latest-status-table-row">
		<div class="key">Status:</div>
		<div class="value"><div class="status ${getStatusStyle(data.status)}">${data.status}</div></div>
	</div>
	`
	
	const values = document.getElementsByClassName("value");
	let w = 0;
	for(const value of values) w = Math.max(w, value.clientWidth + 1);
	w = Math.min(w, 240);
	for(const value of values) value.style.width = `${w}px`;
}

function initializeDeadline(data){
	const deadlineNode = document.querySelector(".payment > .deadline");
	const sep = (document.body.clientWidth > 690 ? ': ' : '<br>');
	if(data.isDeadlineActive){
		deadlineNode.innerHTML = `Hari Terakhir Pembayaran${sep}${data.tanggal}`;
	}
	else{
		deadlineNode.classList.add("d-none");
	}
}

function populatePaymentHistory(data){
	const tbody = document.querySelector(".payment > .history tbody");
	tbody.innerHTML = '';
	const accordion = document.getElementById("history-accordion");
	accordion.innerHTML = '';
	
	if(data.length <= 0){
		tbody.innerHTML = `<tr><td class="fw-normal fst-italic status unknown" colspan="9">Tidak ada riwayat pembayaran</td></tr>`;
		accordion.innerHTML = `<span class="fw-normal fst-italic status unknown" colspan="9">Tidak ada riwayat pembayaran</span>`;
	}
	else{
		const formatter = new Intl.NumberFormat('id-ID');
		for(const paymentData of data){
			tbody.innerHTML +=
			`
			<tr>
				<td>${paymentData.periode}</td>
				<td>${paymentData.mataUang}</td>
				<td>${formatter.format(paymentData.tagihanPeriodeIni)}</td>
				<td>${formatter.format(paymentData.tunggakan)}</td>
				<td>${formatter.format(paymentData.denda)}</td>
				<td>${formatter.format(paymentData.totalTagihan)}</td>
				<td>${formatter.format(paymentData.totalPembayaran)}</td>
				<td>${formatter.format(paymentData.totalSisaTagihan)}</td>
				<td><div class="status ${getStatusStyle(paymentData.status)}">${paymentData.status}</div></td>
			</tr>
			`
		}
			
		let it = 0;
		for(const paymentData of data){
			const formatter = new Intl.NumberFormat("id-ID", { style: "currency", currency: paymentData.mataUang, maximumFractionDigits: 0 });
			it++;
			accordion.innerHTML +=
			`
			<div class="accordion-item">
				<h2 class="accordion-header" id="heading-${it}">
					<button class="accordion-button${it > 1 ? ' collapsed' : ''}" data-bs-toggle="collapse" data-bs-target="#collapse-${it}"
							aria-expanded="${it > 1 ? 'false' : 'true'}" aria-controls="collapse-${it}">
					Periode ${paymentData.periode}
					</button>
				</h2>
				<div id="collapse-${it}" class="accordion-collapse collapse${it > 1 ? '' : ' show'}"
					 aria-labelledby="heading-${it}" data-bs-parent="#history-accordion">
					<div class="accordion-body">
						<div class="table-wrapper">
							<table>
								<tbody>
									<tr>
										<td class="key">Mata Uang</td>
										<td>${paymentData.mataUang}</td>
									</tr>
									<tr>
										<td class="key">Tagihan Periode Ini</td>
										<td>${formatter.format(paymentData.tagihanPeriodeIni)}</td>
									</tr>
									<tr>
										<td class="key">Tunggakan</td>
										<td>${formatter.format(paymentData.tunggakan)}</td>
									</tr>
									<tr>
										<td class="key">Denda</td>
										<td>${formatter.format(paymentData.denda)}</td>
									</tr>
									<tr>
										<td class="key">Total Tagihan</td>
										<td>${formatter.format(paymentData.totalTagihan)}</td>
									</tr>
									<tr>
										<td class="key">Total Pembayaran</td>
										<td>${formatter.format(paymentData.totalPembayaran)}</td>
									</tr>
									<tr>
										<td class="key">Total Sisa Tagihan</td>
										<td>${formatter.format(paymentData.totalSisaTagihan)}</td>
									</tr>
									<tr>
										<td class="key">Status</td>
										<td><div class="status ${getStatusStyle(paymentData.status)}">${paymentData.status}</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			`
		}
	}
}

function getDataMahasiswa(){
	return new Promise((resolve, reject) => {
		$.ajax({
			type: "GET",
			url: url + '/mahasiswa/detail',
			headers: {
				"Access-Control-Allow-Origin": "*",
				"Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
			},
			contentType: 'application/json',
			success: function(data){
				resolve(data);
			},
			error: function(error){
				reject(error);
			}
		});
	});
}

function getDataPembayaran(){
	return new Promise((resolve, reject) => {
		$.ajax({
			type: "GET",
			url: url + '/pembayaran/getPembayaran',
			headers: {
				"Access-Control-Allow-Origin": "*",
				"Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
			},
			contentType: 'application/json',
			success: function(data){
				data.result.sort((a, b) => {
					const [tahunA, termA] = [a["Tahun Ajaran"].split('/')[0], a["Term"]];
					const [tahunB, termB] = [b["Tahun Ajaran"].split('/')[0], b["Term"]];
					if(tahunA < tahunB || tahunA === tahunB && termA < termB) return 1;
					if(tahunA > tahunB || tahunA === tahunB && termA > termB) return -1;
					return 0;
				});
				resolve(data);
			},
			error: function(error){
				reject(error);
			}
		});
	});
}

function init(){
	getDataPembayaran().then(response => {
		const dataPembayaran = response.result[0];
		getDataMahasiswa().then(response => {
			const dataMahasiswa = response.result;
			const latestStatusData = {
				npm: dataMahasiswa["npm"],
				nama: dataMahasiswa["namaLengkap"],
				periode: `${dataPembayaran["Tahun Ajaran"]} - ${dataPembayaran["Term"]}`,
				mataUang: dataPembayaran["Mata Uang"],
				tagihan: dataPembayaran["Total Tagihan"],
				status: dataPembayaran["Status"].toTitleCase()
			}
			initializeLatestStatus(latestStatusData);
		});
		
		const deadline = new Date(dataPembayaran["Deadline"]);
		const deadlineData = {
			isDeadlineActive: true,
			tanggal: new Intl.DateTimeFormat('id-ID', {day: "numeric", month: "long", year: "numeric"}).format(deadline)
		}
		initializeDeadline(deadlineData);
		
		const dataRiwayatPembayaran = response.result;
		let paymentHistoryData = [];
		for(const data of dataRiwayatPembayaran){
			paymentHistoryData.push({
				periode: `${data["Tahun Ajaran"]} - ${data["Term"]}`,
				mataUang: data["Mata Uang"],
				tagihanPeriodeIni: data["Tagihan"],
				tunggakan: data["Tunggakan"],
				denda: data["Denda"],
				totalTagihan: data["Total Tagihan"],
				totalPembayaran: data["Total Pembayaran"],
				totalSisaTagihan: data["Sisa Tagihan"],
				status: data["Status"].toTitleCase()
			});
		}
		populatePaymentHistory(paymentHistoryData);
	});
}

init();