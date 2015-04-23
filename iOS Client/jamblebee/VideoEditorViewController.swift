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
    
    var recording = false;
    @IBOutlet weak var MainVideoView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        kVideoEditorControllerState.videoEditorViewController = self;
        startRecordingActions()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func startRecordingActions() {
        recording = !recording
        kVideoCollectionMainView.videoCollectionMainView.switchAble = recording
    }
    
    override func viewWillAppear(animated: Bool) {
        kVideoCollectionMainView.videoCollectionMainView.mainViewController = self
        kVideoCollectionMainView.videoCollectionMainView.switchMainVideoView(self.MainVideoView)
        kVideoCollectionMainView.videoCollectionMainView.switchAble = recording
        getProjectVideos()
    }
    
    //cannot have a viewcontroller with mutiple parents so we need to set our collection to
    //the projects current video list.
    func getProjectVideos() {
        if(kVideoEditorControllerState.videoEditorViewController.childViewControllers.count <= 0) {
            return
        }
        var ourVideoCollecion = self.childViewControllers[0] as! VideoCollectionViewController
        var videoEditorCollection = kRecordViewControllerState.recordViewController.childViewControllers[0] as! VideoCollectionViewController
        ourVideoCollecion.cells = videoEditorCollection.cells
        ourVideoCollecion.collectionView?.reloadData()
    }
}

class VideoEditorViewControllerState {
    var videoEditorViewController = VideoEditorViewController()
    func recordingActions() -> Bool { return videoEditorViewController.recording }
}
