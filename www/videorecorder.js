cordova.define("ding-plugin-videorecorder.videorecorder", function(require, exports, module) {
var exec = require('cordova/exec');

exports.recordVideo = function(userid, jd, wd) {
    exec(null, null, "videorecorder", "recordVideo", [userid, jd, wd]);
};

});
