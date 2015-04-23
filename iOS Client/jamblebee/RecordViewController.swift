//
//  RecordViewController.swift
//  jambley
//
//  Created by David Gerrells on 3/24/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import UIKit
import AVFoundation
let kRecordViewControllerState = RecordViewControllerState()
class RecordViewController: UIViewController {
    
    @IBOutlet var recordView: UIView!
    let captureSession = AVCaptureSession()
    var captureDevice : AVCaptureDevice?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        kRecordViewControllerState.recordViewController = self
        checkDevices()
        if captureDevice != nil {
            beginSession()
        }
    }
    
    func beginSession() {
        var err : NSError? = nil
        captureSession.addInput(AVCaptureDeviceInput(device: captureDevice, error: &err))
        
        var dataOut = AVCaptureVideoDataOutput()
        captureSession.addOutput(dataOut)
        
        var avConnection: AVCaptureConnection = dataOut.connectionWithMediaType(AVMediaTypeVideo)
        avConnection.videoOrientation = AVCaptureVideoOrientation.LandscapeRight
        if err != nil {
            println("error: \(err?.localizedDescription)")
        }
        
        var previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        
        recordView.layer.addSublayer(previewLayer)
        previewLayer?.frame.size = recordView.layer.frame.size
        previewLayer.position = CGPoint(x: recordView.frame.width/2, y: recordView.frame.height/2)
        captureSession.startRunning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func checkDevices() {
        captureSession.sessionPreset = AVCaptureSessionPresetLow
        let devices = AVCaptureDevice.devices()
        
        // Loop through all the capture devices on this phone
        for device in devices {
            // Make sure this particular device supports video
            if (device.hasMediaType(AVMediaTypeVideo)) {
                // Finally check the position and confirm we've got the back camera
                if(device.position == AVCaptureDevicePosition.Front) {
                    captureDevice = device as? AVCaptureDevice
                }
            }
        }
    }
    
    
    
    @IBOutlet var projectVideoCollection: UIView!
    
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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

//singlton to hold our view controller
class RecordViewControllerState {
    var recordViewController = RecordViewController()
}
