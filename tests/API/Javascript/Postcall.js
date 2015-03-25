/**
 * Created by kim on 3/11/15.
 */
var request = require('request')
var videoData = {}
var time = new Date().getTime()
var url = "http://127.0.0.1:3000/data/post_video"
videoData.id = "video1" + time
videoData.name = "video1"
videoData.owner = "Kim"
videoData.createdAt = time
videoData.startTime = time - 300000
videoData.endTime = time
videoData.size = "30"
videoData.length = 300000
videoData.projectList = {}
var param = {}
param.videoData = videoData
var options = {
    method : "POST",
    headers: {'content-type' : 'application/json'},
    url : url,
    body : param,
    json : true
}
request(options,function(err,result,response){
    console.log(err + " | " + JSON.stringify(response))


})

request('http://localhost:3000/videos/download/catVideo',function(err,response,body){
    console.log(response)
})
