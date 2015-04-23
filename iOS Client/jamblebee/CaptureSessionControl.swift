//
//  CaptureSessionInfo.swift
//  jambley
//
//  Created by David Gerrells on 4/16/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import Foundation
import UIKit
import MobileCoreServices
import AVFoundation
import QuartzCore

class CaptureSessionControl: NSObject, AVCaptureFileOutputRecordingDelegate {
    
    let captureSession = AVCaptureSession()
    var captureDeviceInput : AVCaptureDeviceInput?
    var audioDeviceInput : AVCaptureDeviceInput?
    
    var previewLayer : AVCaptureVideoPreviewLayer?
    var connectionsSetup = false
    var sessionRunning = false
    var outputFragmentInterval = CMTimeMake(600,1)
    
    func startSession() {
        if !connectionsSetup {
            return
        }
        previewLayer?.opacity = 1.0
        captureSession.startRunning()
        captureSession.outputs[0].startRecordingToOutputFileURL(getVideoFileNameFromDate(), recordingDelegate: self)
        sessionRunning = true;
    }
    
    func endSession() {
        if !connectionsSetup {
            return
        }
        previewLayer?.opacity = 0.0
        captureSession.stopRunning()
        captureSession.outputs[0].stopRecording()
        sessionRunning = false;
    }
    
    //recordign delegate functions
    func captureOutput(captureOutput: AVCaptureFileOutput!, didFinishRecordingToOutputFileAtURL outputFileURL: NSURL!, fromConnections connections: [AnyObject]!, error: NSError!){
        println(outputFileURL)
        //UISaveVideoAtPathToSavedPhotosAlbum(outputFileURL.path, self, "addVideo:didFinishSavingWithError:contextInfo:", nil)
        var videoGrid = kRecordViewControllerState.recordViewController.childViewControllers[0] as! VideoCollectionViewController
        videoGrid.addVideoCell(outputFileURL)
    }
    
    //selector for adding a video to the project when it is done saving
    @objc func addVideo(video: NSString, didFinishSavingWithError: NSError, contextInfo: AnyObject ){
         var videoGrid = kRecordViewControllerState.recordViewController.childViewControllers[0] as! VideoCollectionViewController
        videoGrid.addVideoCell(NSURL(fileURLWithPath: video as String)!)
    }
    
    //gets a nsurl based on the current date
    func getVideoFileNameFromDate() -> NSURL{
        var date = NSDate().timeIntervalSince1970 * 1000
        let fileManager = NSFileManager.defaultManager()
        let urls = fileManager.URLsForDirectory(.DocumentDirectory, inDomains: .UserDomainMask)
        var documents: NSURL = urls[0] as! NSURL
        var outputUrl = documents.URLByAppendingPathComponent(String(format:"%f", date)+"vid.mp4")
        //println(outputUrl)
        return outputUrl
    }
    
    func setupConnections(previewView: UIView) {
        var err : NSError? = nil
        //add video and audio input
        
        addAudioInput()
        
        //setup preview layer
        var previewLayer = AVCaptureVideoPreviewLayer.layerWithSession(captureSession) as! AVCaptureVideoPreviewLayer
        previewView.layer.insertSublayer(previewLayer, atIndex: 0)
        previewLayer.connection.videoOrientation = AVCaptureVideoOrientation.LandscapeRight
        previewLayer.connection.automaticallyAdjustsVideoMirroring = false
        previewLayer.connection.videoMirrored = false
        previewLayer.frame.size = previewView.bounds.size
        self.previewLayer = previewLayer
        
      
        //setup output connection
        var output = AVCaptureMovieFileOutput()
        output.movieFragmentInterval = outputFragmentInterval
        captureSession.addOutput(output)
        output.connectionWithMediaType(AVMediaTypeVideo).videoOrientation = AVCaptureVideoOrientation.LandscapeRight
        connectionsSetup = true
    }
    
    func addAudioInput() {
        if audioDeviceInput != nil {
            captureSession.removeInput(audioDeviceInput)
        }
        AVAudioSession.sharedInstance().setPreferredIOBufferDuration(0.005, error: nil)
        AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayAndRecord, withOptions: AVAudioSessionCategoryOptions.MixWithOthers, error: nil)
        var err : NSError? = nil
        var audioIn = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeAudio)
        if(audioIn == nil){
            return
        }
        audioDeviceInput = AVCaptureDeviceInput(device: audioIn, error: &err)
        captureSession.addInput(audioDeviceInput)
    }
    
    func requestCamera(requestedPosition: AVCaptureDevicePosition ) {
        if captureSession.running {
            return
        }
        captureSession.sessionPreset = AVCaptureSessionPresetHigh
        if ( captureDeviceInput != nil ) {
            captureSession.removeInput(captureDeviceInput)
        }
        let devices = AVCaptureDevice.devices()
        var camera : AVCaptureDevice?
        
        for device in devices {
            if (device.hasMediaType(AVMediaTypeVideo)) {
                if(device.position == AVCaptureDevicePosition.Front &&
                    requestedPosition == AVCaptureDevicePosition.Front ) {
                        camera = device as? AVCaptureDevice
                } else if (device.position == AVCaptureDevicePosition.Back &&
                    requestedPosition == AVCaptureDevicePosition.Back) {
                        camera = device as? AVCaptureDevice
                }
            }
        }
        if(camera == nil){
            return
        }
        var err : NSError? = nil
        captureDeviceInput = AVCaptureDeviceInput(device: camera, error: &err)
        if err != nil {
            println("error: \(err?.localizedDescription)")
            return
        }
        captureSession.addInput(captureDeviceInput)
        if captureSession.outputs.count <= 0 {
            return
        }
        captureSession.outputs[0].connectionWithMediaType(AVMediaTypeVideo).videoOrientation = AVCaptureVideoOrientation.LandscapeRight
    }
}