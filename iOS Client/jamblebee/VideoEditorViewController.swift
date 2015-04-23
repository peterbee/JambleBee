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
import QuartzCore

let kVideoEditorControllerState = VideoEditorViewControllerState()

class VideoEditorViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate  {
    
    var animationSpeed = 0.25;
    var recording = false;

    @IBOutlet weak var MainVideoView: UIView!

    var avlayer: AVPlayerLayer = AVPlayerLayer()
    var mainVideoCellData = VideoCellData()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        kVideoEditorControllerState.videoEditorViewController = self;
        // avlayer.videoGravity = AVLayerVideoGravityResizeAspectFill
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setAVLayer(cellData : VideoCellData) {
        self.avlayer = cellData.avlayer
        //get start and end for animation
        var start = self.view.convertPoint(avlayer.position,  fromView: cellData.cell)
        var end = MainVideoView.layer.position
        //create holder to contain the animation
        //holder will be remove from the main layer later to prevent memory leak
        var vidHolder = UIView()
        vidHolder.layer.addSublayer(avlayer)
        vidHolder.frame.size = avlayer.frame.size
        //add holder to main layer
        self.view.layer.addSublayer(vidHolder.layer)
        //set the start postion for the animation
        vidHolder.layer.position = start
        UIView.animateWithDuration(animationSpeed, animations: {
            vidHolder.layer.position = end
            //set avlayer size and postion
            self.avlayer.frame.size = self.MainVideoView.layer.frame.size
            self.avlayer.position = vidHolder.convertPoint(end, fromView: self.view)
        }, completion: { (finished) in})
    }
    
    func restoreCell(cellData : VideoCellData) {
        if(avlayer.superlayer == nil) {
            return
        }
        //end position
        var end = self.view.convertPoint(cellData.cell.layer.position, fromView: cellData.cell.superview)
        //reference so we can remove it from super layer at end of animation
        var oldHolder = avlayer.superlayer
        avlayer.superlayer.position = end
        
        //hack for an animation
        //needs to refactor to allow for better animations
        var vidHolder = UIView()
        self.view.addSubview(vidHolder)
        
        //the animation
        UIView.animateWithDuration(animationSpeed, animations: {
            vidHolder.layer.position = end
            }, completion: { (finished) in
                vidHolder.removeFromSuperview()
                cellData.resetLayer()
                oldHolder.removeFromSuperlayer()
            }
        )
    }
    
    //restores the avlayer's player
    // call by the settings view to restore from settings
    func restore() {
        avlayer.player = mainVideoCellData.avplayer
        avlayer.player.play()
    }
    
    func setMainVideo(cell : VideoCellData) {
        restoreCell(mainVideoCellData)
        setAVLayer(cell)
        mainVideoCellData = cell
    }
    
    //check to see if the given player is the current AVPlayer
    func isInMainVideoView(otherPlayer : AVPlayer) -> Bool {
        if let avplayer = avlayer.player {
            return avlayer.player.isEqual(otherPlayer)
        }
        return false
    }
    
    //toggle recording
    @IBAction func recordButton(sender: UIButton) {
        recording = !recording
    }
    
    //add video to project
    @IBAction func addVideoProject(sender: UIButton) {
        var picker = UIImagePickerController();
        picker.mediaTypes = [kUTTypeMovie];
        picker.delegate = self;
        picker.allowsEditing = true;
        picker.sourceType = UIImagePickerControllerSourceType.SavedPhotosAlbum;
        self.presentViewController(picker, animated: true, completion: nil);
    }
    
    func imagePickerController(picker: UIImagePickerController,
        didFinishPickingMediaWithInfo info: [NSObject : AnyObject]){
            var url = info[UIImagePickerControllerMediaURL] as NSURL!;
            var cellData = VideoCellData()
            
            cellData.avplayer = AVPlayer(URL: url)
            cellData.avlayer = AVPlayerLayer(player: cellData.avplayer)
            cellData.url  = url
            
            var videoGrid = self.childViewControllers[0] as VideoCollectionViewController
            var newData = videoGrid.cells.arrayByAddingObject(cellData)
            videoGrid.cells = newData;
            videoGrid.collectionView?.reloadData()
            
            picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(picker: UIImagePickerController){
        picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    override func viewWillAppear(animated: Bool) {
        getProjectVideos()
    }
    
    //cannot have a viewcontroller with mutiple parents so we need to set our collection to
    //the projects current video list.
    func getProjectVideos() {
        if(kVideoEditorControllerState.videoEditorViewController.childViewControllers.count <= 0) {
            return
        }
        var ourVideoCollecion = self.childViewControllers[0] as VideoCollectionViewController
        var videoEditorCollection = kVideoEditorControllerState.videoEditorViewController.childViewControllers[0] as VideoCollectionViewController
        ourVideoCollecion.cells = videoEditorCollection.cells
        ourVideoCollecion.collectionView?.reloadData()
    }
}

class VideoEditorViewControllerState {
    var videoEditorViewController = VideoEditorViewController()
    func recordingActions() -> Bool { return videoEditorViewController.recording }
}
