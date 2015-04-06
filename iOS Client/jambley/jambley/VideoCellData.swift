//
//  VideoCellData.swift
//  jambley
//
//  Created by David Gerrells on 3/8/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import Foundation
import AVFoundation
import UIKit
import MobileCoreServices

class VideoCellData: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate  {
    
    
    var avplayer: AVPlayer = AVPlayer();
    var avlayer: AVPlayerLayer = AVPlayerLayer();
    var cell: UICollectionViewCell = UICollectionViewCell();
    var parentController: UIViewController = UIViewController();
    var url = NSURL();
    var playing = true;
    var loop = false;
    var startTime = kCMTimeZero
    var endTime = CMTimeMake(300, 1);
    
    func createLayer(){
        var tap = UITapGestureRecognizer(target: self, action: Selector("handleTap:"))
        cell.addGestureRecognizer(tap)
    }
    
    func removeTapGesture(){
        if(cell.gestureRecognizers?.count < 1) {
            var tap = cell.gestureRecognizers![0] as UITapGestureRecognizer;
            cell.removeGestureRecognizer(tap)
        }
    }
    
    func resetLayer(){
        avlayer.removeFromSuperlayer()
        cell.layer.addSublayer(avlayer)
        avlayer.frame.size = CGSize(width: cell.frame.width, height: cell.frame.height)
        createLayer()
    }
    
    func handleTap(sender:UITapGestureRecognizer) {
        if(avplayer.currentItem != nil) {
            togglePlayback()
            return;
        }
        loadVideo()
    }
    
    func isInMainView() -> Bool {
        return videoEditorControllerState.videoEditorViewController.isInMainVideoView(avplayer)
    }
    
    func switchMainVideo() {
        if(isInMainView()) {
            return
        }
        videoEditorControllerState.videoEditorViewController.setMainVideo(avplayer,cell: self)
//        cell.layer.addSublayer(avlayer)
//        avlayer.player = avplayer
    }
    
    func restore() {
        cell.layer.addSublayer(avlayer)
        avlayer.player = avplayer
    }
    
    func togglePlayback(){
        playing = !playing;
        if(playing) {
            avplayer.pause()
        } else {
            avplayer.play()
            if(videoEditorControllerState.videoEditorViewController.recording) {
                switchMainVideo()
            }
        }
    }
    
    func createActionListeners() {
        avplayer.actionAtItemEnd = .None
        NSNotificationCenter.defaultCenter().addObserver(self,
            selector: "endVideoPlayback",
            name: AVPlayerItemDidPlayToEndTimeNotification,
            object: avplayer.currentItem)
    }
    
    func endVideoPlayback(){
        avplayer.seekToTime(startTime)
        avplayer.pause()
        if(!loop) {
            return
        }
        avplayer.play()
    }
    
    func loadVideo() {
        var picker = UIImagePickerController();
        picker.mediaTypes = [kUTTypeMovie];
        picker.delegate = self;
        picker.allowsEditing = true;
        picker.sourceType = UIImagePickerControllerSourceType.PhotoLibrary;
        parentController.presentViewController(picker, animated: true, completion: nil);
    }
    
    func imagePickerController(picker: UIImagePickerController,
        didFinishPickingMediaWithInfo info: [NSObject : AnyObject]){
            var url = info[UIImagePickerControllerMediaURL] as NSURL!;
            
            
            avplayer = AVPlayer(URL: url)
            avlayer.removeFromSuperlayer()
            avlayer = AVPlayerLayer(player: avplayer)
            avlayer.frame.size = CGSize(width: cell.frame.width, height: cell.frame.height);
            
            //            //avplayerLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
            cell.layer.addSublayer(avlayer);
            //
            println(avplayer.status == AVPlayerStatus.Unknown);
            println(avplayer.status == AVPlayerStatus.ReadyToPlay)
            picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(picker: UIImagePickerController){
        picker.dismissViewControllerAnimated(true, completion: nil)
    }

    
}