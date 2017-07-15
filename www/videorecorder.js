var exec = require('cordova/exec');

exports.recordVideo = function(userid, jd, wd) {
    exec(null, null, "videorecorder", "recordVideo", [userid, jd, wd]);
};
