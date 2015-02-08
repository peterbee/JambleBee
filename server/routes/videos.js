 
exports.downloadById = function(req, res) {
  var id = req.params.id;
  console.log('Getting video: ' + id);
  var file = './videos/' + id + '.mp4';
  res.download(file);
};
