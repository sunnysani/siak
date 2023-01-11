if (localStorage.getItem("expiresAt") < Date.now()) {
  localStorage.clear();
}

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

let redirectUrl;
if ($(location).attr("href").includes("localhost")) {
  redirectUrl = "http://localhost:8000";
} else if ($(location).attr("href").includes("127.0.0.1:8000")) {
  redirectUrl = "http://127.0.0.1:8000";
} else {
  if ($(location).attr("href").includes("dev-siakng.herokuapp.com")) {
    redirectUrl = "https://dev-siakng.herokuapp.com";
  } else if ($(location).attr("href").includes("prod-siakng.herokuapp.com")) {
    redirectUrl = "https://prod-siakng.herokuapp.com";
  }
}

function loginTokenGenerator(authData) {
  return new Promise((resolve, reject) => {
    $.ajax({
      type: "POST",
      url: url + "/auth",
      contentType: "application/json",
      data: JSON.stringify(authData),
      dataType: "json",
      success: function (data) {
        var expires = Date.now() + 1800000;
        localStorage.setItem("expiresAt", expires);
        localStorage.setItem("tokenRes", data.token);
        window.location = redirectUrl;
        resolve(data);
      },
      error: function (data) {
        reject(data);
      },
    });
  });
}

$("#loginFormButton").click(function (e) {
  $("#loginFormButton").hide();
  $(".button-login").append(`<div class="loader"></div>`);

  const authData = {
    username: $("#username").val(),
    password: $("#password").val(),
  };

  loginTokenGenerator(authData)
    .then(function () {
      $("#loginBerhasilModal").modal("show");
    })
    .catch(function () {
      $("#loginGagalModal").modal("show");
      $("#loginFormButton").show();
      $(".loader").remove();
    });
});
