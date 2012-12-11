//Taken from https://developer.blackberry.com/html5/apis/blackberry.push.html#.openBISPushListener

function say(msg) {
    var resultDiv = document.getElementById("message_list");
    resultDiv.innerHTML += "<li>"+ msg;
}

function openBISPushListener() {
    try {
        say('Restarting push ports');
        blackberry.push.closePushListener();
        say('Registering on BBPush API');
        blackberry.push.openBISPushListener(ops, onData, onRegister, onSimChange);
        say('openBISPushListener Called');
    } catch (err) {
        say('Error calling openBISPushListener!');
        alert('Exception: '+String(err));
    }
}

function onRegister(status) {
    say('Blackberry server Response.');
    if (status === 0) {
        say('Registered on BBPush API, Registering on the Django server.');

        var xmlhttp = new XMLHttpRequest();

        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState==4) {
                if(xmlhttp.status==200) {
                say('Successful Registered on Django Server');
                } else {
                    say(
                        'ERROR registered on Django Server ('+
                        xmlhttp.status+
                        ') '+
                        xmlhttp.statusText+
                        ':\n'+
                        xmlhttp.responseText
                        );
                }
            }
        };
        
        xmlhttp.open("POST", register_url, true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xmlhttp.send("type=BO&token="+blackberry.identity.PIN);

    } else if (status == 1) {
        alert("network error");
    } else if (status == 2) {
        alert("rejected by server");
    } else if (status == 3) {
        alert("invalid parameters");
    } else if (status == -1) {
        alert("general error");
    } else {
        alert("unknown status");
    }
}

function onData(data) {
    try {
        say( 'Got Push [' + blackberry.utils.blobToString(data.payload) + ']' );

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState==4) {
                if(xmlhttp.status==200) {
                    say('Sync result: '+xmlhttp.responseText);
                    if(!blackberry.app.isForeground()){
                        blackberry.app.showBannerIndicator('icons/icon.png', 1);
                    }
                } else {
                    say(
                        'ERROR registered on Django Server ('+
                        xmlhttp.status+
                        ') '+
                        xmlhttp.statusText+
                        ':\n'+
                        xmlhttp.responseText
                        );
                }
            }
        };
        say("Attempting to sync with Django Server");
        xmlhttp.open("POST", sync_url, true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send("type=BO&token="+blackberry.identity.PIN);

        return 0; //indicate acceptance of payload for reliable push
    } catch (err) {
        alert('Exception: '+String(err));
    }
}

function onSimChange() {
    say("SIM card is changed!");
}

function main() {
    say('Initializing BISPushListener.');
    openBISPushListener();

    //On Exit:
    blackberry.app.event.onExit(function() {
        blackberry.app.requestBackground();
    });
}


