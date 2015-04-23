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
    var url = NSURL();
    var playing = true;
    var loop = false;
    var startTime = kCMTimeZero
    var endTime = CMTimeMake(300, 1);
    
    func removeTapGesture(){
        if(cell.gestureRecognizers == nil) {
            return
        }
        if(cell.gestureRecognizers?.count < 1) {
            var tap = cell.gestureRecognizers![0] as UITapGestureRecognizer;
            cell.removeGestureRecognizer(tap)
        }
    }
    
    func resetLayer(){
        if(isInMainView()) {
            return;
        }
        removeTapGesture()
        avlayer.removeFromSuperlayer()
        cell.layer.addSublayer(avlayer)
        avlayer.frame.size = cell.layer.frame.size
        avlayer.position = CGPoint(x: avlayer.frame.width/2, y: avlayer.frame.height/2)
        var tap = UITapGestureRecognizer(target: self, action: Selector("handleTap:"))
        cell.addGestureRecognizer(tap)
    }
    
    func handleTap(sender:UITapGestureRecognizer) {
        if(avplayer.currentItem != nil) {
            togglePlayback()
            return;
        }
    }
    
    func togglePlayback(){
        playing = !playing;
        if(playing && !kVideoEditorControllerState.recordingActions() ) {
            avplayer.pause()
        } else {
            avplayer.play()
            if(kVideoEditorControllerState.videoEditorViewController.recording) {
                switchMainVideo()
            }
        }
    }
    
    func isInMainView() -> Bool {
        return kVideoEditorControllerState.videoEditorViewController.isInMainVideoView(avplayer)
    }
    
    func switchMainVideo() {
        if(isInMainView()) {
            return
        }
        kVideoEditorControllerState.videoEditorViewController.setMainVideo(self)
    }
    
    //setup listern for when the avplayer finishs playing
    func createActionListeners() {
        avplayer.actionAtItemEnd = .None
        NSNotificationCenter.defaultCenter().addObserver(self,
            selector: "endVideoPlayback",
            name: AVPlayerItemDidPlayToEndTimeNotification,
            object: avplayer.currentItem)
    }
    
    //actual function called when the avplayer is done playing
    func endVideoPlayback(){
        avplayer.seekToTime(startTime)
        avplayer.pause()
        if(!loop) {
            return
        }
        avplayer.play()
    }
}