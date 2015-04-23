//
//  VideoCollectionViewController.swift
//  jambley
//
//  Created by David Gerrells on 3/5/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import UIKit
import MobileCoreServices
import AVFoundation
let reuseIdentifier = "Cell"
let userActionList: NSMutableArray = []
class VideoCollectionViewController: UICollectionViewController,UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    @IBOutlet var videoCollection: UICollectionView!
    var cells: NSArray =  NSArray()
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.whiteColor()
        self.collectionView!.registerClass(UICollectionViewCell.self, forCellWithReuseIdentifier: reuseIdentifier)
        
        var tap = UITapGestureRecognizer(target: self, action: Selector("handleTap:"))
        self.view.addGestureRecognizer(tap)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int {
        //#warning Incomplete method implementation -- Return the number of sections
        return 1
    }


    override func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        //#warning Incomplete method implementation -- Return the number of items in the section
        return cells.count;
    } 

    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! UICollectionViewCell

        var data: VideoCellData = cells[indexPath.row] as! VideoCellData
        data.cell = cell;
        data.reset()
        cell.backgroundColor = UIColor(white: 0.9, alpha: 0.8)
        return cell
    }

    
    func handleTap(sender: UITapGestureRecognizer) {
        var picker = UIImagePickerController();
        picker.mediaTypes = [kUTTypeMovie];
        picker.delegate = self;
        picker.allowsEditing = true;
        picker.sourceType = UIImagePickerControllerSourceType.SavedPhotosAlbum;
        self.presentViewController(picker, animated: true, completion: nil);
    }

    func imagePickerController(picker: UIImagePickerController,
        didFinishPickingMediaWithInfo info: [NSObject : AnyObject]){
            var url = info[UIImagePickerControllerMediaURL] as! NSURL!;
            addVideoCell(url)
            picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(picker: UIImagePickerController){
        picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func addVideoCell(vidUrl: NSURL ) {
        //create cell data
        var cellData = VideoCellData()
        cellData.avplayer = AVPlayer(URL: vidUrl)
        cellData.avlayer = AVPlayerLayer(player: cellData.avplayer)
        cellData.url  = vidUrl
        cellData.createActionListeners()
        //add to list of cells
        var videoGrid = kRecordViewControllerState.recordViewController.childViewControllers[0] as! VideoCollectionViewController
        var newData = videoGrid.cells.arrayByAddingObject(cellData)
        videoGrid.cells = newData;
        //add to collection
        var row = videoGrid.collectionView?.numberOfItemsInSection(0)
        var indexPath = [NSIndexPath(forRow: row!, inSection: 0)]
        videoGrid.collectionView?.insertItemsAtIndexPaths(indexPath)
        //if the other view is loaded, set the items and reload the data.
        if(kVideoEditorControllerState.videoEditorViewController.childViewControllers.count > 0) {
            var videoGrid2 = kVideoEditorControllerState.videoEditorViewController.childViewControllers[0] as! VideoCollectionViewController
            videoGrid2.cells = newData;
            videoGrid2.collectionView?.reloadData()
        }
    }
    
    func removeVideoCell(videoCell: VideoCellData) {
        //popup alert for confirmation of removal. This does not delete the video
        var alert = UIAlertController(title: "", message: "Remove Video from Project?", preferredStyle: UIAlertControllerStyle.Alert)
        alert.addAction(UIAlertAction(title: "Confirm", style: UIAlertActionStyle.Default, handler: { (alert) -> Void in
            self.deleteVideoCell(videoCell)
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.Default, handler: nil))
        self.presentViewController(alert, animated: true, completion: nil)
    }
    
    func deleteVideoCell(videoCell: VideoCellData) {
        videoCell.avlayer.removeFromSuperlayer()
        var videoGrid = kRecordViewControllerState.recordViewController.childViewControllers[0] as! VideoCollectionViewController
        var newData = NSMutableArray()
        newData.addObjectsFromArray(videoGrid.cells as [AnyObject])
        newData.removeObject(videoCell)
        videoGrid.cells = newData
        videoGrid.collectionView?.reloadData()
        if(kVideoEditorControllerState.videoEditorViewController.childViewControllers.count > 0) {
            var videoGrid2 = kVideoEditorControllerState.videoEditorViewController.childViewControllers[0] as! VideoCollectionViewController
            videoGrid2.cells = newData;
            videoGrid2.collectionView?.reloadData()
        }
    }
}
