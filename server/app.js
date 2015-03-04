var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var multer = require('multer'); //for file uploading
var done =false
var filePath = ''
var DB_PATH = 'localhost/jamble'
var monk = require('monk')
var db = monk(DB_PATH)



var routes = require('./routes/index');
var users = require('./routes/users');
var videos = require('./routes/videos');
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'html');
app.engine('html', require('ejs').renderFile);


// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
/**
 * Use multer to perform file uploading to certain file path
 */
app.use(multer({ dest: './videos/',
    rename: function (fieldname, filename) {
        return filename;
    },
    onFileUploadStart: function (file) {
        console.log("MIME TYPE : " + file.mimetype)
        if(file.mimetype!="video/mp4"){ //error if not mp4
            done = false
            return false;
        }
        console.log(file.originalname + ' is starting ...')
    },
    onError: function(err){
      console.log(err);
        done = false;
        return false;
    },
    onFileUploadComplete: function (file) {
        //console.log(file)
        console.log(file.fieldname + ' uploaded to  ' + file.path)
        filePath=file.path
        done=true;
    }
}));

//put the post here because we need the boolean done to check
app.post('/videos/upload',function(req,res){
    if(done==true) {
        //console.log(res.files)
        res.send("<h3>File uploaded  </h3>" )
       // "<video width='300' height='300' controls><source src =\"" +filePath +  "\" type=\"video/mp4\"></video> ");
        //Meant to show the video just uploaded but not playing
    }
    else { //not done or error
        res.send("<h3>Wrong mime type or error while uploading</h3>")
    }

});

///setup some mock database for testing
app.use(function(req,res,next){
    req.db = db
    next()
})

app.use('/', routes);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});



// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});


module.exports = app;
