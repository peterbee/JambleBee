//
//  VideoViewController.swift
//  jambley
//
//  Created by David Gerrells on 2/26/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import UIKit
import AVFoundation
import MobileCoreServices

class VideoViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    var avplayer = AVPlayer();
    var mediaTypes = [kUTTypeMovie]
    var avplayerLayer = AVPlayerLayer()
    @IBOutlet weak var videoView: UIView!
    @IBOutlet weak var controlButtons: UISegmentedControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //AVPlayer player = AVPlayer(URL: <#NSURL!#>);
    }
    
    override func viewWillDisappear(animated: Bool) {
        avplayer.pause()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of a
    }
    
    @IBAction func loadVideo(sender: AnyObject) {
        var picker = UIImagePickerController();
        picker.mediaTypes = mediaTypes;
        picker.delegate = self;
        picker.allowsEditing = true;
        picker.sourceType = UIImagePickerControllerSourceType.PhotoLibrary;
        self.presentViewController(picker, animated: true, completion: nil);
    }

    @IBAction func changeItem(sender: UISegmentedControl) {
        switch sender.selectedSegmentIndex
        {
        case 0:
            if(avplayer.currentItem != nil && avplayer.currentTime().value >= avplayer.currentItem.duration.value) {
                avplayer.seekToTime(kCMTimeZero)
            }
            avplayer.play()
        case 1:
            avplayer.pause()
        case 2:
            avplayer.pause()
            avplayer.seekToTime(kCMTimeZero)
        default:
            break; 
        }
    }
    
    func imagePickerController(picker: UIImagePickerController,
        didFinishPickingMediaWithInfo info: [NSObject : AnyObject]){
            var url = info[UIImagePickerControllerMediaURL] as? NSURL;
            avplayer = AVPlayer(URL: url)
            avplayerLayer.removeFromSuperlayer()
            avplayerLayer = AVPlayerLayer(player: avplayer)
            avplayerLayer.frame = videoView.layer.frame;
            avplayerLayer.position = videoView.layer.position;
            //avplayerLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
            self.view.layer.addSublayer(avplayerLayer);
            
            println(avplayer.status == AVPlayerStatus.Unknown);
            println(avplayer.status == AVPlayerStatus.ReadyToPlay);
            
            //avplayer.play()
            
            picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(picker: UIImagePickerController){
        picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
}
