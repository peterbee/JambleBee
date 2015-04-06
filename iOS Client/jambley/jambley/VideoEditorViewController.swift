//
//  VideoEditorViewController.swift
//  jambley
//
//  Created by David Gerrells on 3/10/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//
import UIKit
import MobileCoreServices
import AVFoundation

let videoEditorControllerState = VideoEditorViewControllerState()

class VideoEditorViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate  {
    
    var recording = false;

    @IBOutlet weak var MainVideoView: UIView!

   
    var avlayer: AVPlayerLayer = AVPlayerLayer()
    var mainVideoCellData = VideoCellData()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        videoEditorControllerState.videoEditorViewController = self;
        
       var rect = UIScreen.mainScreen().bounds
//        avlayer.frame.size = CGSize(width: MainVideoView.frame.width, height: MainVideoView.frame.height)
//        var pos = avlayer.position
//        pos.x = rect.width/2
//        avlayer.position = pos;
                println(rect.width)
        println(MainVideoView.center)
        MainVideoView.center = CGPoint(x: MainVideoView.center.x/2, y: MainVideoView.center.y)
        avlayer.frame.size = MainVideoView.frame.size
        var pos = avlayer.position;
        avlayer.frame.origin = CGPoint.zeroPoint
        pos.x = rect.width/2;
        avlayer.position = pos;
        MainVideoView.layer.addSublayer(avlayer)
        println(avlayer.position)
       // avlayer.videoGravity = AVLayerVideoGravityResizeAspectFill
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func restore() {
        avlayer.player = mainVideoCellData.avplayer
        avlayer.player.play()
    }
    
    func setMainVideo(player: AVPlayer, cell : VideoCellData) {
        cell.avlayer.player = nil
        avlayer.player = player
        mainVideoCellData.restore()
        mainVideoCellData = cell
       
    }
    
    func pauseMainView() {
        //avplayer.pause()
    }
    
    func isInMainVideoView(otherPlayer : AVPlayer) -> Bool {
        if let avplayer = avlayer.player {
            return avlayer.player.isEqual(otherPlayer)
        }
        return false
    }
    
    @IBAction func recordButton(sender: UIButton) {
        recording = !recording
    }
    
    @IBAction func addVideoProject(sender: UIButton) {
        var picker = UIImagePickerController();
        picker.mediaTypes = [kUTTypeMovie];
        picker.delegate = self;
        picker.allowsEditing = true;
        picker.sourceType = UIImagePickerControllerSourceType.PhotoLibrary;
        self.presentViewController(picker, animated: true, completion: nil);
    }
    
    func imagePickerController(picker: UIImagePickerController,
        didFinishPickingMediaWithInfo info: [NSObject : AnyObject]){
            var url = info[UIImagePickerControllerMediaURL] as NSURL!;
            var cellData = VideoCellData()
            
            cellData.avplayer = AVPlayer(URL: url)
            cellData.avlayer.removeFromSuperlayer()
            cellData.avlayer = AVPlayerLayer(player: cellData.avplayer)
            cellData.createActionListeners()
            cellData.url  = url
            //println(self.childViewControllers.count)
            var videoGrid = self.childViewControllers[0] as VideoCollectionViewController
            var newData = videoGrid.cells.arrayByAddingObject(cellData)
            videoGrid.cells = newData;
            videoGrid.collectionView?.reloadData()
            
            picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(picker: UIImagePickerController){
        picker.dismissViewControllerAnimated(true, completion: nil)
    }
}

class VideoEditorViewControllerState {
    var videoEditorViewController = VideoEditorViewController();
}
