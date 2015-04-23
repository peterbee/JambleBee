//
//  VideoStreamViewController.swift
//  jambley
//
//  Created by David Gerrells on 4/22/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//
import UIKit
import MobileCoreServices
import AVFoundation
import QuartzCore
import MediaPlayer

class VideoStreamViewController: UIViewController {
    
    var player: MPMoviePlayerController?
    var avlayer: AVPlayerLayer?
    
    @IBOutlet var videoFrame: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        //        avplayer = AVPlayer(URL: NSURL(string: "http://jamble.guygrigsby.com/videos/download/catVideo31424910376328"))
        player = MPMoviePlayerController(contentURL: NSURL(string: "http://jamble.guygrigsby.com/videos/download/catVideo31424910376328"))
        player!.prepareToPlay()
        player?.view.frame = videoFrame.frame
        //player?.backgroundView.frame.size = videoFrame.frame.size
        videoFrame.superview!.addSubview(player!.view!)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        println(player?.isPreparedToPlay)
        player?.play()
//        


    }

}
