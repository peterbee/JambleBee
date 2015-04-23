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
        
        UIView.animateWithDuration(0.13, animations: {
            who.view.layer.position = endPosition;
            }, completion: { (finished) in
                NSLog("Animation completed.")
        })
        
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
