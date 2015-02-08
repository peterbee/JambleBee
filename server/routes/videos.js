 
exports.downloadById = function(req, res) {
  var id = req.params.id;
  console.log('Getting video: ' + id);
  var file = './videos/' + id + '.mp4';
  res.download(file);
};

//work with broswer now
exports.upload = function(req,res) {
    res.render('uploadTest')
}