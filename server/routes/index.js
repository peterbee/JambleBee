var express = require('express');
var router = express.Router();
var request = require('request')

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/video', function(req,res){
res.render('video',{title:'Retrieve video'})
    })
router.get('/stream',function(req,response){
    var outStream = require('fs').createWriteStream("./data/catVideo.mp4");
// Add this to ensure that the out.txt's file descriptor is closed in case of error.
    response.on('error', function(err) {
        outStream.end();
    });
// Pipe the input to the output, which writes the file.
    response.pipe(outStream);
})
router.get('/getvideo',function(req,res){
    request('localhost:3000/stream',function(err,response,body){
        console.log(body + response + err)
        res.send(body)
    })
})
module.exports = router;
