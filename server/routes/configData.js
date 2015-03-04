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
        var result = projectData.find({"id":ProjectId},function(result){
           return res.json(result)
        })
}

//Post projectInfo
exports.postProjectInfo = function(req,res){

}

    // Get video config information
exports.getVideoInfo = function(req,res){
            var videoId  = req.params.videoId
            videosData.find({"id":videoId}).then(function(result){
                return res.json(result)
            })

        }

exports.postVideoInfo = function(req,res){

}
