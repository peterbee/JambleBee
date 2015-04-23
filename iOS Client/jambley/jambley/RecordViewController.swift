//
//  RecordViewController.swift
//  jambley
//
//  Created by David Gerrells on 3/24/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import UIKit
import MobileCoreServices
import AVFoundation
import QuartzCore
let kRecordViewControllerState = RecordViewControllerState()
class RecordViewController: UIViewController{
    
    @IBOutlet var playbackView: UIView!
    @IBOutlet var recordView: UIView!
    
    @IBOutlet var recordDurationText: UILabel!
    @IBOutlet var projectVideoCollection: UIView!
    @IBOutlet var startRecordButton: UIButton!
    let kSessionControl = CaptureSessionControl()
    var isRecording = false;
    var sessionStarted = false;
    var currentSessionDuration = 0.0
    var recordDurationTimer : NSTimer?
    var cameraPosition = AVCaptureDevicePosition.Front
    
    override func viewDidLoad() {
        super.viewDidLoad()
        kRecordViewControllerState.recordViewController = self
        recordDurationTimer = NSTimer.scheduledTimerWithTimeInterval(1, target: self, selector: Selector("updateDuration"), userInfo: nil, repeats: true)
        recordDurationText.alpha = 0.0
        kSessionControl.requestCamera(cameraPosition)
        kSessionControl.setupConnections(recordView)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        kVideoCollectionMainView.videoCollectionMainView.mainViewController = self
        kVideoCollectionMainView.videoCollectionMainView.switchMainVideoView(self.playbackView)
        kVideoCollectionMainView.videoCollectionMainView.switchAble = true
        getProjectVideos()
    }
    
    //cannot have a viewcontroller with mutiple parents so we need to set our collection to
    //the projects current video list.
    func getProjectVideos() {
        if(kVideoEditorControllerState.videoEditorViewController.childViewControllers.count <= 0) {
            return
        }
        var ourVideoCollecion = self.childViewControllers[0]as! VideoCollectionViewController
        var videoEditorCollection = kVideoEditorControllerState.videoEditorViewController.childViewControllers[0] as! VideoCollectionViewController
        ourVideoCollecion.cells = videoEditorCollection.cells
        ourVideoCollecion.collectionView?.reloadData()
    }
    
    @IBAction func recordButton(sender: UIButton) {
        startRecordButton.alpha = 0.0
        startRecording()
    }
    
    @IBAction func stopRecording(sender: AnyObject) {
        if !kSessionControl.sessionRunning {
            return
        }
        isRecording = false;
        kSessionControl.endSession()
        startRecordButton.alpha = 1.0
        kRecordViewControllerState.pauseAllVideos()
    }
    
    func startRecording() {
        if kSessionControl.sessionRunning {
            return
        }
        isRecording = true
        kSessionControl.startSession()
        kRecordViewControllerState.playAllVideos()
        startRecordButton.alpha = 0.0

    }
    
    //called every 100ms to update the timer for recording
    func updateDuration( ) {
        if !isRecording {
            currentSessionDuration = 0.0
            recordDurationText.alpha = 0.0
            return
        }
        recordDurationText.alpha = 1.0
        currentSessionDuration += 1;
        recordDurationText.text = String(format:"%.1f", currentSessionDuration)
    }
}

//singlton to hold our view controller
//Note: not real singleton
class RecordViewControllerState {
    var recordViewController = RecordViewController()
    
    func playAllVideos(){
        var videoGrid = recordViewController.childViewControllers[0] as! VideoCollectionViewController
        for cell in videoGrid.cells {
            var cellData = cell as! VideoCellData
            cellData.playVideo()
        }
    }
    func pauseAllVideos(){
        var videoGrid = recordViewController.childViewControllers[0] as! VideoCollectionViewController
        for cell in videoGrid.cells {
            var cellData = cell as! VideoCellData
            cellData.pauseVideo()
        }
    }
}
