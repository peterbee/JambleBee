//
//  VideoCollectionContainer.swift
//  jambley
//
//  Created by David Gerrells on 3/26/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//  Class that can contain a VideoCollectionController

import UIKit
import MobileCoreServices
import AVFoundation
import QuartzCore

let kVideoCollectionMainView = VideoCollectionMainViewState()

class VideoCollectionMainView: NSObject {
 
    var animationSpeed = 0.25;
    
    var mainViewController: UIViewController?
    var avlayer: AVPlayerLayer = AVPlayerLayer()
    var mainVideoCellData = VideoCellData()
    var mainVideoView = UIView()
    
    func setMainVideo(cell : VideoCellData) {
        restoreCell(mainVideoCellData)
        setAVLayer(cell)
        mainVideoCellData = cell
    }
    
    func setAVLayer(cellData : VideoCellData) {
        self.avlayer = cellData.avlayer
        //get start and end for animation
        var start = mainViewController?.view.convertPoint(avlayer.position,  fromView: cellData.cell)
        var end = mainVideoView.layer.position
        //create holder to contain the animation
        //holder will be remove from the main layer later to prevent memory leak
        var vidHolder = UIView()
        vidHolder.layer.addSublayer(avlayer)
        vidHolder.frame.size = avlayer.frame.size
        //add holder to main layer
        mainViewController?.view.layer.addSublayer(vidHolder.layer)
        //set the start postion for the animation
        vidHolder.layer.position = start!
        UIView.animateWithDuration(animationSpeed, animations: {
            vidHolder.layer.position = end
            //set avlayer size and postion
            self.avlayer.frame.size = self.mainVideoView.layer.frame.size
            self.avlayer.position = vidHolder.convertPoint(end, fromView: self.mainViewController?.view)
            }, completion: { (finished) in})
    }
    
    func restoreCell(cellData : VideoCellData) {
        if(avlayer.superlayer == nil) {
            return
        }
        //end position
        var end = mainViewController?.view.convertPoint(cellData.cell.layer.position, fromView: cellData.cell.superview)
        //reference so we can remove it from super layer at end of animation
        var oldHolder = avlayer.superlayer
        avlayer.superlayer.position = end!
        
        //hack for an animation
        //needs to refactor to allow for better animations
        var vidHolder = UIView()
        mainViewController?.view.addSubview(vidHolder)
        
        //the animation
        UIView.animateWithDuration(animationSpeed, animations: {
            vidHolder.layer.position = end!
            }, completion: { (finished) in
                vidHolder.removeFromSuperview()
                cellData.resetLayer()
                oldHolder.removeFromSuperlayer()
            }
        )
    }
    
    //check to see if the given player is the current AVPlayer
    func isInMainVideoView(otherPlayer : AVPlayer) -> Bool {
        if let avplayer = avlayer.player {
            return avlayer.player.isEqual(otherPlayer)
        }
        return false
    }
}

class VideoCollectionMainViewState {
    var videoCollectionMainView = VideoCollectionMainView()
}