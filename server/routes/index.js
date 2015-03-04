var express = require('express');
var router = express.Router();
var request = require('request')
var videos = require('./videos');
var configData = require('./configData.js')
    
    
    /* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/videos/download/:id', videos.downloadById);
router.get('/videos/upload',videos.upload)

router.get('/data/project/:projectId', configData.getProjectInfo)
router.get('data/video/:videoId',configData.getVideoInfo)
router.get('/test',function(req,res){
    var db = req.db
    var projectInfo = db.get("projectInfo")
    projectInfo.update({"id":"1234"},{"id":"1234","name":"testProject"},{upsert:true})
    var videoInfo = db.get("videoInfo")
    videoInfo.update({"id":"1234"},{"id":"1234","name":"testVideo"},{upsert:true})
     videoInfo.find({"id":"1234"}).then(function(result){
         res.json(result)
     })
})



module.exports = router;
