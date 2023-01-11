// // Routing stuff
let url;
if (
  $(location).attr("href").includes("localhost") ||
  $(location).attr("href").includes("http://127.0.0.1:8000/")
) {
  url = "http://localhost:8080";
} else {
  if ($(location).attr("href").includes("dev-siakng.herokuapp.com")) {
    url = "https://dev-siakng-api.herokuapp.com";
  } else if ($(location).attr("href").includes("prod-siakng.herokuapp.com")) {
    url = "https://prod-siakng-api.herokuapp.com";
  }
}

$("#logoutButton").click(function (e) {
  localStorage.clear();
  window.location = redirectUrl + "/main/authentication";
});

window.onload = () => {
  const header = $("header")[0];
  $.ajax({
    type: "GET",
    url: url + '/mahasiswa/detail',
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
    },
    contentType: 'application/json',
    success: function (response) {
      $('.spinner-border').remove();
      $('#dropdown-user').prop("hidden", false);
      $('#dropdown-user').text(response.result.namaLengkap);
      if(header.scrollWidth > header.clientWidth){
        $('#dropdown-user').html("<div></div>");
        $('#dropdown-user').addClass("overflows");
        $('.dropdown-menu[aria-labelledby="dropdown-user"]').addClass("overflows");
        $('.dropdown-menu[aria-labelledby="dropdown-user"] div').text(response.result.namaLengkap);
      }
    }
  });

  var countNotif = 0;
  $.ajax({
    type: "GET",
    url: url + '/notifikasi-lonceng',
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
    },
    contentType: 'application/json',
    success: function (response) {
      $(".ruang-notifikasi").html('');
      for (var i = 0; i < response.result.length ; i++) {
        if (!response.result[i].read) {
          countNotif += 1;
        }
        $('.ruang-notifikasi').append(
          `
          <div class="d-flex justify-content-between data-notif">
					<div class="p-2">
						<p id="isi_notif">`+response.result[i].isiNotifikasi+`</p>
					</div>
  					<div class="p-2">
						<p id="jam_notif" style="font-size: 12px; margin-left: auto; margin-right: auto;">`+response.result[i].createdAt.substring(0,16)+`</p>
					</div>
				</div>
        <hr>
          `
        );
      }
      if (countNotif > 0) {
        $('.nav-notif').append(
            `
            <div id="notification-count">
              `+countNotif +`
            </div>
            `
        );
      }
    }
  });

  $('.fa-bell').click(function () { 
    $.ajax({
      type: "POST",
      url: url + '/notifikasi-lonceng/read',
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
      },
      contentType: 'application/json',
      success: function (response) {
        if (response.result === true) {
          $('.nav-notif').empty();
        }
      }
    });
  });
}
