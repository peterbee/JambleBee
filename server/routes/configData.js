/**
 * Created by kim on 2/28/15.
 */
var monk = require('monk')
var DB_PATH = 'localhost/jamble'
var db = monk(DB_PATH)
var projectData = db.get('projectInfo')
var videosData = db.get('videoInfo')


//Get project config information
exports.getProjectInfo = function(req,res){
        var ProjectId  = req.params.projectId
         projectData.find({"id":ProjectId}).then(function(result){
               return res.json(result)
        })
}

//Post projectInfo
exports.postProjectInfo = function(req,res){
    var object={}
    var body = req.body.projectData
    object.id = body.id
    object.name = body.name
    object.videoList = body.videoList //JSON object of videos and their start and end time
    object.owner = body.owner
    object.createdAt = body.createdAt
    object.origId = body.origId
    object.parentId = body.parentId
    projectData.update({"id":object.id},object,{upsert:true})
        .then(function(err,result){
            if(err) {
                res.json(err)
            }
            else{
                res.json(result)
            }
        })




}

    // Get video config information
exports.getVideoInfo = function(req,res){
            var videoId  = req.params.videoId
                console.log(videoId)
            videosData.find({"id":videoId}).then(function(result){
                 return res.json(result)
            })

        }

exports.postVideoInfo = function(req,res){

    var object={}
    var body = req.body.videoData
    object.id = body.id
    object.name = body.name
    object.projectList = body.projectList //JSON object of projects the video belong to
    object.owner = body.owner
    object.startTime = body.startTime // in Unix time
    object.endTime = body.endTime
    object.size = body.size
    object.length = body.length
    object.createdAt = req.createdAt


    videosData.update({"id":object.id},object,{upsert:true}).then(function(err,result){
        if(err){
            res.json(err)
        }
        else {
            console.log("succeed")
            res.json({status : "succeed"})
        }
    })



}
