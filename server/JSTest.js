/**
 * Created by kim on 2/15/15.
 */
var request  = require('request')
var fs = require('fs')

request('http://guygrigsby.com:3000/videos/download/catVideo')
//request('http://localhost:3000/videos/download/catVideo')
    .on('error',function(err){
        console.log("GET ERROR : " + err)
    })
    .pipe(fs.createWriteStream('doodle.mp4'))


///post test
postUrl = 'http://guygrigsby.com:3000/videos/upload'
//postUrl = 'http://localhost:3000/videos/upload'

var req = request.post(postUrl, function (err, resp, body) {
    if (err) {
        console.log('Error!');
    } else {
        console.log('URL: ' + body);
    }
});
var form = req.form();
form.append('file',fs.createReadStream('doodle.mp4'))