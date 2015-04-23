//
//  RecordSettingsViewController.swift
//  jambley
//
//  Created by David Gerrells on 4/15/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import UIKit
import AVKit
import AVFoundation
import MobileCoreServices

class RecordSettingsViewController: UIViewController {

    @IBOutlet var micSensitivityText: UILabel!
    @IBOutlet var recordDelaySlider: UISlider!
    @IBOutlet var recordDelayText: UILabel!
    @IBOutlet var cameraPositionSegment: UISegmentedControl!
    var userDefaults = NSUserDefaults(suiteName: "standardUserDefaults");
    let CAMERA_FRONT = 0
    let CAMERA_BACK = 1
    let kMaxRecordDelay = 30 as Float
    let kSaveRecordDelayValue = "recordingDelay"
    
    override func viewWillAppear(animated: Bool) {
        cameraPositionSegment.selectedSegmentIndex = kRecordViewControllerState.recordViewController.cameraPosition == AVCaptureDevicePosition.Back ? CAMERA_BACK : CAMERA_FRONT
        userDefaults = NSUserDefaults();
        
        if(userDefaults?.valueForKey(kSaveRecordDelayValue) != nil) {
            recordDelaySlider.value = (userDefaults?.valueForKey(kSaveRecordDelayValue) as! Float)
            recordDelayText.text = String(Int(recordDelaySlider.value*kMaxRecordDelay)) + " sec"
        }
        
    }
    
    @IBAction func doneSaveRecordSettings(sender: AnyObject) {
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    @IBAction func changeCamera(sender: UISegmentedControl) {
        switch sender.selectedSegmentIndex {
            case CAMERA_FRONT:
                kRecordViewControllerState.recordViewController.kSessionControl.requestCamera(AVCaptureDevicePosition.Front)
                break
        
            case CAMERA_BACK:
                kRecordViewControllerState.recordViewController.kSessionControl.requestCamera(AVCaptureDevicePosition.Back)
                break
            
            default:
                kRecordViewControllerState.recordViewController.kSessionControl.requestCamera(AVCaptureDevicePosition.Front)
        }
        
    }
    
    @IBAction func recordDelayChange(sender: UISlider) {
        userDefaults?.setValue(sender.value, forKey: kSaveRecordDelayValue)
        userDefaults?.synchronize()
        recordDelayText.text = String(Int(sender.value*kMaxRecordDelay)) + " sec"
    }
    
    @IBAction func micSensitivityChange(sender: UISlider) {
        micSensitivityText.text = String(Int(sender.value*100)) + "%"
    }
        
    override func preferredInterfaceOrientationForPresentation() -> UIInterfaceOrientation {
        return UIInterfaceOrientation.Portrait
    }
    
    override func shouldAutorotate() -> Bool {
        return false
    }
    
}
