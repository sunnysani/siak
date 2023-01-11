if (localStorage.getItem("expiresAt") < Date.now()) {
  localStorage.clear();
}

function BuildPostDiv(title, info, content) {
  return (
    `
    <div class="post">
        <p class="post-title">` +
    title +
    `</p>
        <p class="post-info">` +
    info +
    `</p>
        <p class="post-content">` +
    content +
    `</p>
    </div>`
  );
}

const dateTimeFormatter = new Intl.DateTimeFormat('id-ID', {
  weekday: "long",
  year: "numeric",
  month: "long",
  day: "numeric",
  hour: "2-digit",
  minute: "2-digit",
  hour12: true
});
function GetPostByIndex(index) {
  let tmpLoop = 2 * index;
  var retDiv = "";
  for (let i = tmpLoop; i < tmpLoop + 2; i++) {
    if (pengumumanArr[i] != undefined) {
      const date = new Date(pengumumanArr[i]["waktu"]);
      const parts = dateTimeFormatter.formatToParts(date);
      parts.splice(7, 0, {type: "literal", value: ", "});
      info = "by " + pengumumanArr[i]["penulis"] + " at " + parts.map(({type, value}) => value).join('');
      retDiv += BuildPostDiv(
        pengumumanArr[i]["judul"],
        info,
        pengumumanArr[i]["isi"]
      )
    }
  }

  return retDiv;
}

function changeIndex(newIndex) {
  index_post = newIndex;
}

function addPages() {
  var retDiv = "";

  retDiv += `
    <ul>
        <li id="left-arrow-pengumuman">
            ←
        </li>`;
  for (let i = 1; i <= Math.ceil(pengumumanArr.length / 2); i++) {
    retDiv += `<li>` + i + `</li>`;
  }
  retDiv += `
        <li id="right-arrow-pengumuman">
            →
        </li>
    </ul>`;

  return retDiv;
}

function DecrementIndexPost() {
  if (index_post <= 0) {
    return;
  } else {
    $("#halaman-pengumuman > ul")
      .children()
    [index_post + 1].classList.toggle("bold");
    index_post -= 1;
    $("#halaman-pengumuman > ul")
      .children()
    [index_post + 1].classList.toggle("bold");
    document.getElementById("pengumuman").innerHTML = "<h1>Pengumuman</h1>" + GetPostByIndex(index_post);
  }
}

function IncrementIndexPost() {
  if (index_post >= Math.ceil(pengumumanArr.length / 2) - 1) {
    return;
  } else {
    $("#halaman-pengumuman > ul")
      .children()
    [index_post + 1].classList.toggle("bold");
    index_post += 1;
    $("#halaman-pengumuman > ul")
      .children()
    [index_post + 1].classList.toggle("bold");
    document.getElementById("pengumuman").innerHTML = "<h1>Pengumuman</h1>" + GetPostByIndex(index_post);
  }
}

var pengumumanArr = []

// This is the page user's seeing
let index_post = 0;
$(document).ready(function () {

  // Fetch Data From API
  $.ajax({
    type: "GET",
    dataType: "json",
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
    },
    contentType: 'application/json',
    url: url + "/pengumuman",
    success: function (response) {
      pengumumanArr = pengumumanArr.concat(response.result)

      document.getElementById("pengumuman").innerHTML = "<h1>Pengumuman</h1>" + GetPostByIndex(index_post);

      document.getElementById("halaman-pengumuman").innerHTML += addPages();
      // Bold current page (Page no. 1)
      $("#halaman-pengumuman > ul")
        .children()
      [index_post + 1].classList.toggle("bold");

      window.onclick = function (e) {
        if (e.target.matches("#left-arrow-pengumuman")) {
          DecrementIndexPost();
        }
        if (e.target.matches("#right-arrow-pengumuman")) {
          IncrementIndexPost();
        }
      };
    },
  });

  let moreDetailsExpanded = false;
  $('.more-details-btn').click(() => {
    $(".more-details-btn span").text(moreDetailsExpanded ? "More details" : "Less details");
    moreDetailsExpanded = !moreDetailsExpanded;
  });

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
      const photo = $(`<img class="profile_avatar" src="${sourceImage}"></img>`);
      photo.on("load", () => $('#photo').html(photo));
	  
      $('#nama-lengkap').html("<strong>" + response.result.namaLengkap + "</strong>");
    }
  });
  
  $.ajax({
    type: "GET",
    url: url + '/ringkasan-akademik/mahasiswa',
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
    },
    contentType: 'application/json',
    success: function (response) {
	  $('.data-diri').html(
        `
        <strong>NPM: </strong>${response.result['NPM']}<br>
        <strong>Prodi: </strong>${response.result['Program Studi']}<br>
        <strong>Semester: </strong>${response.result['Semester']} (${response.result['Status Akademik']})
	    	`
      );
	  
	  $('.nilai').html(
		`
		<div class="item">
			<strong>IPS</strong>
			<div>${response.result['IPS'].toFixed(2)}</div>
		</div>
		<div class="item">
			<strong>IPK</strong>
			<div>${response.result['IPK'].toFixed(2)}</div>
		</div>
		<div class="item">
			<strong>SKS Lulus</strong>
			<div>${response.result['SKS Lulus']}</div>
		</div>
		<div class="item">
			<strong>Total Mutu</strong>
			<div>${response.result['Total Mutu'].toFixed(2)}</div>
		</div>
		`
	  );
    }
  });

  // Ongoing and upcoming

  $.ajax({
    type: "GET",
    url: url + '/tahunAjaran/',
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
    },
    contentType: 'application/json',
    success: function (response) {

      var cardsOn = ''
      var cardsUp = ''
      var now = new Date()
      now = new Date(now - now.getTimezoneOffset() * 60 * 1000)
      
      // Pembayaran
      bayarStart = new Date(response.result.pembayaranStart)
      bayarEnd = new Date(response.result.pembayaranEnd)
      listStart = bayarStart.toString().split(" ")
      listEnd = bayarEnd.toString().split(" ")
      bayarRange = `${listStart[1]} ${listStart[2]} - ${listEnd[1]} ${listEnd[2]}`
    
      if (bayarStart < now && now < bayarEnd) {
        cardsOn += buildCard('bayar', 'up', Math.ceil((bayarEnd - now) / (1000 * 60 * 60 * 24)), bayarRange)
      }
    
      // Pengisian IRS
      var isi = 0
      isiStart = new Date(response.result.isiIRSStart)
      isiEnd = new Date(response.result.isiIRSEnd)
      listStart = isiStart.toString().split(" ")
      listEnd = isiEnd.toString().split(" ")
      isiRange = `${listStart[1]} ${listStart[2]} - ${listEnd[1]} ${listEnd[2]}`
    
      if (now < isiStart) {
        isi = 0
        cardsUp += buildCard('isiIRS', 'up', Math.ceil((isiStart - now) / (1000 * 60 * 60 * 24)), isiRange)
      } else if (now < isiEnd) {
        isi = 1
        cardsOn += buildCard('isiIRS', 'on', 0, isiRange)
      } else {
        isi = 2
      }
    
      // Add Drop IRS
      addDropStart = new Date(response.result.addDropIRSStart)
      addDropEnd = new Date(response.result.addDropIRSEnd)
      listStart = addDropStart.toString().split(" ")
      listEnd = addDropEnd.toString().split(" ")
      addDropRange = `${listStart[1]} ${listStart[2]} - ${listEnd[1]} ${listEnd[2]}`
    
      if (isi == 2) {
        if (now < addDropStart) {
          cardsUp += buildCard('addIRS', 'up', Math.ceil((addDropStart - now) / (1000 * 60 * 60 * 24)), addDropRange)
          cardsUp += buildCard('dropIRS', 'up', Math.ceil((addDropStart - now) / (1000 * 60 * 60 * 24)), addDropRange)
        } else if (now < addDropEnd) {
          cardsOn += buildCard('addIRS', 'on', 0, addDropRange)
          cardsOn += buildCard('dropIRS', 'on', 0, addDropRange)
        }
      }
    
      // Perkuliahan
      kuliahStart = new Date(response.result.perkuliahanStart)
      kuliahEnd = new Date(response.result.perkuliahanEnd)
      listStart = kuliahStart.toString().split(" ")
      listEnd = kuliahEnd.toString().split(" ")
      kuliahRange = `${listStart[1]} ${listStart[2]} - ${listEnd[1]} ${listEnd[2]}`
    
      if (isi > 0) {
        if (now < kuliahStart) {
          cardsUp += buildCard('kuliah', 'up', Math.ceil((kuliahStart - now) / (1000 * 60 * 60 * 24)), kuliahRange)
        } else if (now < kuliahEnd) {
          cardsOn += buildCard('kuliah', 'on', 0, kuliahRange)
        }
      }
    
      // Nilai
      nilaiEnd = new Date(response.result.isiNilaiDosenEnd)
      listStart = kuliahStart.toString().split(" ")
      listEnd = nilaiEnd.toString().split(" ")
      nilaiRange = `${listStart[1]} ${listStart[2]} - ${listEnd[1]} ${listEnd[2]}`
    
      if (kuliahEnd < now && now < nilaiEnd) {
        cardsOn += buildCard('nilai', 'up', Math.ceil((nilaiEnd - now) / (1000 * 60 * 60 * 24)), nilaiRange)
      }

      $(".spinner-container").remove();
      $('#list-cards').append(cardsOn)
      $('#list-cards').append(cardsUp)

    }
    
  });

});

function buildCard(event, type, day, range) {

  dataDay = {
    'up': `H-${day}`,
    'on': 'Masa'
  }

  dataTitle = {
    'bayar': 'Batas Pembayaran',
    'isiIRS': 'Pengisian IRS',
    'addIRS': 'Add IRS',
    'dropIRS': 'Drop IRS',
    'kuliah': 'Perkuliahan',
    'nilai': 'Batas Pengisian Nilai'
  }

  dataUrl = {
    'bayar': {
      'up': '/main/Academic/Payment',
      'on': '/main/Academic/Payment'
    },
    'isiIRS': {
      'up': "/#",
      'on': "/main/CoursePlan/CoursePlanEdit"
    },
    'addIRS': {
      'up': "/#",
      'on': "/main/CoursePlan/CoursePlanAdd"
    },
    'dropIRS': {
      'up': "/#",
      'on': "/main/CoursePlan/CoursePlanDrop"
    },
    'kuliah': {
      'up': "/main/CoursePlan/CoursePlanViewSummary",
      'on': "/main/CoursePlan/CoursePlanViewSummary"
    },
    'nilai': {
      'up': "main/Academic/HistoryByTerm",
      'on': "main/Academic/HistoryByTerm"
    }
  }

  dataDetail = {
    'bayar': {
      'up': "Lihat status pembayaran",
      'on': "Lihat status pembayaran"
    },
    'isiIRS': {
      'up': "Lihat kelas tersedia",
      'on': "Ke halaman Isi IRS"
    },
    'addIRS': {
      'up': "Lihat kelas tersedia",
      'on': "Ke halaman Add IRS"
    },
    'dropIRS': {
      'up': "Lihat kelas tersedia",
      'on': "Ke halaman Drop IRS"
    },
    'kuliah': {
      'up': "Lihat jadwal kuliah",
      'on': "Lihat jadwal kuliah"
    },
    'nilai': {
      'up': "Lihat riwayat nilai",
      'on': "Lihat riwayat nilai"
    }
  }

  return `
    <div class="card-event">
			<div id="card-title">
				${dataDay[type]} ${dataTitle[event]}
			</div>
			<div id="card-date">
				${range}
			</div>
			<div id="card-detail">
				<a href="${dataUrl[event][type]}">
					${dataDetail[event][type]}
				</a>
			</div>
		</div>
  `
}