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
            cell.gestureRecognizers?.removeAll(keepCapacity: true)
        }
    }
    
    func reset(){
        if(isInMainView()) {
            println(1)
            return;
        }
        forceResetLayer()
    }
    
    func forceResetLayer() {
        removeTapGesture()
        avlayer.removeFromSuperlayer()
        cell.layer.addSublayer(avlayer)
        avlayer.frame.size = cell.layer.frame.size
        avlayer.position = CGPoint(x: avlayer.frame.width/2, y: avlayer.frame.height/2)
        //have to reset the gesture stuff due to collection reusing cells. 
        //Basically, gestures respont to the wrong cells if we do not reset. 
        var tap = UITapGestureRecognizer(target: self, action: Selector("handleTap:"))
        cell.addGestureRecognizer(tap)
        var swipe = UISwipeGestureRecognizer(target: self, action: Selector("handleSwipe:"))
        swipe.direction = UISwipeGestureRecognizerDirection.Up
        cell.addGestureRecognizer(swipe)
    }
    
    func handleTap(sender:UITapGestureRecognizer) {
        if(avplayer.currentItem != nil) {
            togglePlayback()
            return;
        } else {
            avplayer.replaceCurrentItemWithPlayerItem(AVPlayerItem(URL: url))
        }
    }
    
    func togglePlayback(){
        playing = !playing;
        if(playing && !kVideoEditorControllerState.recordingActions() && false) {
            avplayer.pause()
        } else if (kVideoCollectionMainView.videoCollectionMainView.mainVideoCellData.avplayer.currentItem == nil){
            playVideo()
        }
        if(kVideoCollectionMainView.videoCollectionMainView.switchAble) {
            switchMainVideo()
        }
    }
    
    func isInMainView() -> Bool {
        return kVideoCollectionMainView.videoCollectionMainView.isInMainVideoView(avplayer)
    }
    
    func switchMainVideo() {
        if(isInMainView()) {
            kVideoCollectionMainView.videoCollectionMainView.setMainVideo(VideoCellData())
            kRecordViewControllerState.pauseAllVideos();
            return
        }
        if(kVideoCollectionMainView.videoCollectionMainView.mainVideoCellData.avplayer.currentItem == nil) {
            kRecordViewControllerState.playAllVideos()
        }
        kVideoCollectionMainView.videoCollectionMainView.setMainVideo(self)
    }
    
    func handleSwipe(sender:UISwipeGestureRecognizer) {
        if isInMainView() {
            return
        }
         var videoGrid = kRecordViewControllerState.recordViewController.childViewControllers[0] as! VideoCollectionViewController
        videoGrid.removeVideoCell(self)
    }
    
    func playVideo(){
        avplayer.seekToTime(startTime)
        avplayer.play()
    }
    
    func pauseVideo(){
        avplayer.pause()
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