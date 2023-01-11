var notRead = 0;
var today = "Zzz Zzz 99 99:99:99 ICT 9999";
var listToday = today.split(" ");
var open = 0;

$('.text-box').keyup(function(event) {
    if (event.key === 'Enter') {
        $('#img-send').click();
    }
});

function toggle() {
    $('.main-box').toggleClass('toggle');
    $('.msg-box').toggleClass('toggle');

    if (open == 0) {
        $('.text-box').focus();
    }
    open = 1 - open;

    var conv = document.querySelector('.conv-box');
    conv.scrollTop = conv.scrollHeight;

    if ($("#unread-cnt").css("display") == 'none') {
        $(".unread-msg").parent().remove();
    }
    $("#unread-cnt").css("display", "none");

    $.ajax({
        type: "POST",
        url: url + "/chat/mahasiswa/read",
        contentType: "application/json",
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        }, success: function (res) {

        }
    })
    
}

function send() {
    var text = document.querySelector('.text-box');

    const chat = {
        isiChat : text.textContent
    };
    text.innerHTML = '';

    if ($("#unread-cnt").css("display") == 'none') {
        $(".unread-msg").parent().remove();
    }
    $.ajax({
        type: "POST",
        url: url + "/chat/post/mahasiswa",
        contentType: "application/json",
        data: JSON.stringify(chat),
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        success: function (response) {
            if ($(".chat-empty").length) {
                $(".chat-empty").remove();
            }
            if (listToday[1] != response.result.createdAt.split(" ")[1] || listToday[2] != response.result.createdAt.split(" ")[2]) {
                today = response.result.createdAt;
                listToday = today.split(" ");
                $(".conv-table").append(
                    `
                    <tr>
                    <td class="date">
                        <div>` + listToday[1] + ` ` + listToday[2] + `</div>
                    </td>
                    </tr>
                    `
                )
            }
            $(".conv-table").append(
                `
                <tr>
                <td class="td-from">
                    <div class="chat-time">` + response.result.createdAt.split(" ")[3].substring(0,5)+`</div>
                    <div class="chat-from">`+response.result.isiChat +`</div>
                </td>
                </tr>
                `
            )
            var conv = document.querySelector('.conv-box');
            conv.scrollTop = conv.scrollHeight;
        }
    })
    
    
}

$(document).ready(function () {
    $(".conv-table").html(
        `
        <tr>
            <td class="date">
            <div class="placeholder-glow"><span class="placeholder" style="width: 40px"></span></div>
            </td>
        </tr>
        <tr>
            <td class="td-from">
            <div class="chat-time placeholder-glow"><span class="placeholder" style="width: 30px"></span></div>
            <div class="chat-from placeholder-glow">
                <span class="placeholder" style="width: 45px"></span>
                <span class="placeholder" style="width: 25px"></span>
                <span class="placeholder" style="width: 35px"></span>
                <span class="placeholder" style="width: 55px"></span>
            </div>
            </td>
        </tr>
        <tr>
            <td class="td-to">
            <div class="chat-to placeholder-glow">
                <span class="placeholder" style="width: 25px"></span>
                <span class="placeholder" style="width: 45px"></span>
                <span class="placeholder" style="width: 55px"></span>
                <span class="placeholder" style="width: 35px"></span>
            </div>
            <div class="chat-time placeholder-glow"><span class="placeholder" style="width: 30px"></span></div>
            </td>
        </tr>
        `
    );
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Authorization": "Bearer " + window.localStorage.getItem("tokenRes"),
        },
        contentType: 'application/json',
        url: url + "/mahasiswa/detail",
        success: function (response) {
            $('#dosen-box').append(
                `<p id="dosen">`+ response.result.pembimbingAkademik.namaLengkap +` </p>`
            );
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
        url: url + "/chat/mahasiswa",
        success: function (response) {
            $(".conv-table").html('');
            if (response.result.length == 0) {
                $(".conv-table").append(
                    `
                    <tr>
                        <td class="chat-empty"> 
                        <div class="date">Belum ada komunikasi dengan PA</div>
                        </td>
                    </tr>
                    `
                )
                return;
            }
            else $("#unread-cnt").css("display", "block");

            today = response.result[0].createdAt;
            listToday = today.split(" ");
            $(".conv-table").append(
                `
                <tr>
                    <td class="date">
                        <div>`+ listToday[1] + ` ` + listToday[2] +`</div>
                    </td>
                </tr>
                <tr>
                `
            )
            for (var i = 0 ; i < response.result.length ; i++) {
                if (listToday[1] != response.result[i].createdAt.split(" ")[1] || listToday[2] != response.result[i].createdAt.split(" ")[2]) {
                    today = response.result[i].createdAt;
                    listToday = today.split(" ");
                    $(".conv-table").append(
                        `
                        <tr>
                            <td class="date"> 
                               <div>`+ listToday[1] + ` ` + listToday[2] +`</div>
                            </td>
                        </tr>
                        `
                    )
                }
                if (response.result[i].sender === "MAHASISWA") {
                    $(".conv-table").append(
                        `
                        <tr>
                        <td class="td-from">
                            <div class="chat-time">`+ response.result[i].createdAt.split(" ")[3].substring(0, 5) + `</div>
                            <div class="chat-from">`+response.result[i].isiChat +`</div>
                        </td>
                        </tr>
                        `
                    )
                } else if (response.result[i].sender === "DOSEN") {
                    if (!response.result[i].readByMahasiswa) {
                        notRead = notRead + 1;
                    }
                    if (notRead == 1) {
                        $(".conv-table").append(
                            `
                            <tr>
                                <td class="unread-msg">
                                    <div>--- Pesan belum dibaca ---</div>
                                </td>
                            </tr>
                            `
                        );
                    }
                    $(".conv-table").append(
                        `
                        <tr>
                        <td class="td-to">
                            <div class="chat-to">`+response.result[i].isiChat +`</div>
                            <div class="chat-time">`+response.result[i].createdAt.split(" ")[3].substring(0,5)+`</div>
                        </td>
                        </tr>
                        `
                    )
                }
                $("#unread-cnt").empty();
                $("#unread-cnt").append(notRead);
                if (notRead == 0) {
                    $("#unread-cnt").css("display", "none");
                } else {
                    $("#unread-cnt").css("display", "block");
                }
            }
        },
    });
});


