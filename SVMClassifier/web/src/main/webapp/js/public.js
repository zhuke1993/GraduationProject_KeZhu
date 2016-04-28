var server_host = 'http://zhuke1993.vicp.cc:8081';
//var server_host = 'http://127.0.0.1:8081';
function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null;
}