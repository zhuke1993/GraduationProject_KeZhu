/**
 * Created by ZHUKE on 2016/4/10.
 */
$.ajax({
    url: "http://zhuke1993.vicp.cc:8080/security/profileList",
    data:{
        accessToken: sessionStorage.getItem("accessToken")
    },
    success: function (msg) {
        if (msg.code == '401' || msg.code == '403') {
            alert(msg.msg);
            window.location.href = 'login.html';
        } else if (msg.code == 'OK') {
            var json = JSON.parse(msg.msg);
            for (var i = 0; i < json.length; i++) {
                $("#content").append("<tr>\n" +
                    "                            <td  style=\"width: 12%\">" + json[i].userName + "</td>\n" +
                    "                            <td  style=\"width: 6%\">" + json[i].realName + "</td>\n" +
                    "                            <td  style=\"width: 17%\">" + json[i].idCardNo + "</td>\n" +
                    "                            <td  style=\"width: 12%\">" + json[i].schoolName + "</td>\n" +
                    "                            <td style=\"width: 17%\">" + json[i].address + "</td>\n" +
                    "                            <td style=\"width: 15%\"><a href='http://zhuke1993.vicp.cc:8080" + json[i].schoolCertification + "'>" + json[i].schoolCertification + "</a>" + "</td>\n" +
                    "                            <td style=\"width: 15%\"><a href='http://zhuke1993.vicp.cc:8080" + json[i].idCertification + "'>" + json[i].idCertification + "</a>" + "</td>\n" +
                    "                            <td style=\"width: 6%\"><button onclick='audit(\"" + json[i].userName + "\")'>通过审核</button></td>\n" +
                    "                        </tr>");
            }
        }
    }
});


function audit(userName) {
    $.ajax({
        url: "http://zhuke1993.vicp.cc:8080/security/audit_user",
        data: {
            accessToken: sessionStorage.getItem("accessToken"),
            userName: userName
        },
        success: function (msg) {
            if (msg.code == '401' || msg.code == '403') {
                alert(msg.msg);
                window.location.href = 'login.html';
            } else if (msg.code == 'OK') {
                alert(msg.msg);
            }
        }
    });
}