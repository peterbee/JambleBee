//
//  TabController.swift
//  jambley
//
//  Created by David Gerrells on 2/28/15.
//  Copyright (c) 2015 David Gerrells. All rights reserved.
//

import UIKit

class TabController: UITabBarController, UITabBarControllerDelegate {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        var leftSwipe = UISwipeGestureRecognizer(target: self, action: Selector("handleSwipe:"))
        var rightSwipe = UISwipeGestureRecognizer(target: self, action: Selector("handleSwipe:"))
        
        leftSwipe.direction = .Left
        rightSwipe.direction = .Right
        
        view.addGestureRecognizer(leftSwipe)
        view.addGestureRecognizer(rightSwipe)
        view.backgroundColor = UIColor.whiteColor()
        
        NSNotificationCenter.defaultCenter().addObserver(self,
            selector: "orientationChanged",
            name: UIDeviceOrientationDidChangeNotification,
            object: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func handleSwipe(sender:UISwipeGestureRecognizer)
    {
        if (sender.direction == .Left) {
            if ( self.selectedIndex+1 >=  self.viewControllers?.count ){
                self.selectedIndex = 0;
            }
            else {
                self.selectedIndex++;
            }
            animate(self.selectedViewController!,direction: UIScreen.mainScreen().bounds.width)
            
        }
        if (sender.direction == .Right) {
            if ( self.selectedIndex <=  0 ){
                var d = self.viewControllers?.count;
                self.selectedIndex = d!-1;
            }
            else {
                self.selectedIndex--;
            }
            animate(self.selectedViewController!,direction: -UIScreen.mainScreen().bounds.width)
        }
    }
    
    func animate(who: UIViewController, direction: CGFloat)
    {
        var endPosition = who.view.layer.position;
        var startPosition = who.view.layer.position;
        startPosition.x += direction;
        who.view.layer.position = startPosition;
         println(selectedIndex)
        UIView.animateWithDuration(0.13, animations: {
            who.view.layer.position = endPosition;
            }, completion: { (finished) in
                NSLog("Animation completed.")
                self.updateOrientationBasedOnTab()
        })
        
    }

//    used when the tab bar was visible
//    override func tabBar(tabBar: UITabBar, didSelectItem item: UITabBarItem!) {
//        updateOrientationBasedOnTab()
//    }
    
    override func shouldAutorotate() -> Bool {
        return true
    }
    
    
    override func viewWillAppear(animated: Bool) {
    
    }
    //we want to start in landscape when the app first starts
    //we cannot simple change things in viewDidAppear or viewDidload
    //because they happen first and then the change selector is called
    var justStarted = true
    
    func orientationChanged(){
        if(justStarted){
            justStarted = false
            return
        }
        var orientation = UIDevice.currentDevice().orientation
        if(orientation == UIDeviceOrientation.Portrait || orientation == UIDeviceOrientation.PortraitUpsideDown) {
            self.selectedIndex = 1
        } else if(orientation == UIDeviceOrientation.LandscapeRight ||
            orientation == UIDeviceOrientation.LandscapeLeft) {
            self.selectedIndex = 0
        }
        updateOrientationBasedOnTab()
    }
    
    func updateOrientationBasedOnTab(){
        if(self.selectedIndex == 0){
            let value = UIInterfaceOrientation.LandscapeRight.rawValue
            UIDevice.currentDevice().setValue(value, forKey: "orientation")
        } else {
            let value = UIInterfaceOrientation.Portrait.rawValue
            UIDevice.currentDevice().setValue(value, forKey: "orientation")
        }
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
