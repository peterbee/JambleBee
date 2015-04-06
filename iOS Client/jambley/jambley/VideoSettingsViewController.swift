//
//  VideoSettingsViewController.swift
//  jambley
//
//  Created by David Gerrells on 3/14/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import UIKit
import AVKit
import AVFoundation
import MobileCoreServices


class VideoSettingsViewController: UIViewController {
    
    @IBOutlet var rangeSlider: NMRangeSlider!
    @IBOutlet weak var switchLoop: UISwitch!
    @IBOutlet weak var avlayerView: UIView!
    var avlayer = AVPlayerLayer()
    var currentCellData = VideoCellData()
    var avplayerController = AVPlayerViewController()

    override func viewDidLoad() {
        super.viewDidLoad()
        //avlayer.frame.size = avlayerView.layer.frame.size
       // self.view.layer.addSublayer(avlayer)
        self.addChildViewController(avplayerController)
        
        avlayerView.addSubview(avplayerController.view)
        avplayerController.view.frame.size = avlayerView.frame.size
        
    }
    
    @IBAction func doneSaveSettings(segue:UIStoryboardSegue) {
        avlayer.player = nil
        avplayerController.player = nil
        videoEditorControllerState.videoEditorViewController.restore()
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    override func viewWillAppear(animated: Bool) {
        currentCellData = videoEditorControllerState.videoEditorViewController.mainVideoCellData
        
        videoEditorControllerState.videoEditorViewController.avlayer.player = nil
        
        //currentCellData.avplayer.pause()
       
        if(currentCellData.avplayer.currentItem != nil) {
            rangeSlider.lowerValue = Float(CMTimeGetSeconds(currentCellData.startTime) / CMTimeGetSeconds(currentCellData.avplayer.currentItem.duration))
            rangeSlider.upperValue = Float(CMTimeGetSeconds(currentCellData.endTime) / CMTimeGetSeconds(currentCellData.avplayer.currentItem.duration))
        }
        
        avlayer.player = currentCellData.avplayer
        avplayerController.player = currentCellData.avplayer
        switchLoop.setOn(currentCellData.loop, animated: true)
    }
    
    @IBAction func valueChanged(sender: NMRangeSlider) {
        
        currentCellData.startTime = CMTimeMake(Int64(Double(sender.lowerValue)*CMTimeGetSeconds(currentCellData.avplayer.currentItem.duration)), 1)

        currentCellData.endTime = CMTimeMake(Int64(Double(sender.upperValue)*CMTimeGetSeconds(currentCellData.avplayer.currentItem.duration)), 1)
        
        currentCellData.avplayer.currentItem.forwardPlaybackEndTime = currentCellData.endTime
    }

    @IBAction func toggleLoop(sender: AnyObject) {
        currentCellData.loop = !currentCellData.loop
    }
}
