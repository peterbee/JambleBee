Project Jamblebee
=================
---

The Vision
----------
The end results of the project would be an application that helps bridge the
gaps between cultures through video collaboration with an emphasis on musical
collaboration. As music is universal in all cultures, it is an ideal vehicle to
help bring a togetherness between cultures.

Repository Description
----------------------
This is the repository for Team Contrivance and project Jamblebee, which is a
multi-platform application currently being developed in Android with a server
component.

Android
=======
---

All android applications are build with android studio, the official Android
development environment.


Android Development Requirements
--------------------------------
* Android Studio 1.1.0 or greater

For Server Requiremetns see "server/README.md"

Android Base Project
--------------------
* Jamblebee

Android Prototype Projects
--------------------------
|                            |            |                 |                  |
| :------------------------: | :--------: | :-------------: | :--------------: |
|CameraTest                  |Jamstagram  |LoadVideos       | MultipleActivies |
|TestMultipleActivities2     |VideoPlayer |VideoProjectModel| EditView         |

---

Server Side
===========
---

For server side development information see:

```
server/README.md
```

Server Side Tests
-----------------
See "test" directory, which contains test video data and tests developed both
in java and java script.

The java test code contains an ant file, use the following command for build
instructions.

```
ant -f tests/API/Java/build.xml -p
```

Server Side Test Requirements
-----------------------------
All dependencies are contained in the following directory.

```
test/API/Java/lib/
```

---

History (Prototypes)
===================
---
Build many prototypes to assist in the development of this product and start an
Android Base Project. There is a server component which will be used to store
video project meta data and video data, .mp4 files.
