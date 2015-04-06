iOS Client Description
----------------------
iOS client is written in swift using Xcode. It uses default libs except for an open source range slider. The range slider is a simple .m/.h file that gives the functionality of a range slider. 

iOS Client Build instructions
----------------------
* Xcode Version 6.1.1 (6A2008a) 
* Xcode simulator or configured device

An ealier or later version may or may not work

1. Navegate to the jambley folder where one would see a 'jambley.xcodeproj' file along with some other folders.
2. Double click on the 'jambley.xcodeproj' file to open the project in Xcode
3. I the top left corner you will see what looks like a play and stop buttons. To the left of them is the name of the project 'jambley' with an arrow pointing to the current device that will be run. Click on the device name to open a drop down menu that has simulator device options along with any device currently connected. Select the device you wish to run on. 

4. Click on the run button in the top left corner. It looks visual like a play button. 



iOS Test instructions
--------------------

Right now there are few/no functional tests. This is in part due to inexperience in writing functional tests for visual related tests. That is, how to test whether the AVPlayerLayers are being set up properly or that the views are positioned correctly. 

To run what functional tests do exist (if any) 

1. Click and hold on the run button at the top left corner. A drop down menu will appear. Click on the 'Test' option that has a wrench icon next to hit.

Even though there are not functional test there is still a test plan/process. The following are manuel tests that will later need to be automated. 


TestCase: Single video functional test
Functions: Load video, video playback, video pause/play, video switch to main view

1. Start app
2. Click on add video cell button
3. Select video to load
4. Visualy validate a new video cell has been added to the video cell view
5. Visualy validate that a thumb nail of the video is visible
6. Tap on the thumbnail to start playback and to switch the video to the main view. 
7. Visualy validate that the video is playing in the top main view
8. Tap the video cell area again to pause playback
9. Visualy validate that the video is paused
10. Tap the video cell area again to start playback
11. Visualy validate that the video has once again started playback. 
