# CMS Demo for Android

##### A repository showing off basic CMS capability with KudanAR.

*N.B. This project was tested to work with Android Studio 2.0*

The aim of this project is to show that the KudanAR framework is completely CMS agnostic. As such we have written the app to read a simple JSON file from a server.  

The app can be easily modified to work with parsing raw JSON responses from your own CMS without it affecting the way in which your AR works.

This sample implementation shows off displaying video content on top of markers that can be found in the `Markers` folder as well as recognising the top 50 of [RollingStone's Top 500 Albums of All Time](http://www.rollingstone.com/music/lists/500-greatest-albums-of-all-time-20120531/little-richard-heres-little-richard-20120531).  

Multiple marker sets can be loaded by KudanAR. A marker set can be updated independently to the other marker sets.

In order for this sample project to work you will need to:

1. Clone the repo
2. Download the appropriate KudanAR framework from the [Kudan website](https://www.kudan.eu/download/)
3. Place `kudanar.jar, cardboard.jar` and `libkudan.so` in the appropriate directories (`jniLibs` and `jniLibs/armeabi-v7a`, respectively).
4. Upload the assets contained within the `CMS Content` to your own server and alter the assets as you wish.
5. Open `CMSContentManagement.java` and on line `42` enter the address of your server.
6. Alternatively you can test the app with content from Kudan's server, just open `CMSContentManagement.java` and change the boolean value of `useOwnServer` (line `17`) to `false`.
7. The app looks for `test.json` on the server as to where it should find the rest of its data. You can change this to your hearts content. Details on `test.json` are below.

## JSON Details

**test.json**  

	{
		"lastUpdated": "23-01-2016",
		"results": [{
			"id": 0,
			"marker": "https://YOURSITE/CMS/Media/space.KARMarker",
			"augmentation": "https://YOURSITE/CMS/Media/space.mp4",
			"markerFileName": "space.KARMarker",
			"augmentationFileName": "space.mp4",
			"lastUpdated": "15-12-2015",
			"augmentationType": "video",
			"displayFade": 1.0,
			"resetTime": 5,
			"augmentationRotation": 90,
			"fillMarker": 1
		}, {
			"id": 1,
			"marker": "https://YOURSITE/CMS/Media/waves.KARMarker",
			"augmentation": "https://YOURSITE/CMS/Media/waves.mp4",
			"markerFileName": "waves.KARMarker",
			"augmentationFileName": "waves.mp4",
			"lastUpdated": "15-12-2015",
			"augmentationType": "video",
			"displayFade": 1.0,
			"resetTime": 5,
			"augmentationRotation": 0,
			"fillMarker": 1
		}, {
			"id": 2,
			"marker": "https://YOURSITE/CMS/Media/musicMarker.KARMarker",
			"augmentation": "",
			"markerFileName": "musicMarker.KARMarker",
			"augmentationFileName": "",
			"lastUpdated": "01-01-2016",
			"augmentationType": "text",
			"displayFade": 1.0,
			"resetTime": 5,
			"augmentationRotation": 0,
			"fillMarker": 1
		}]
	}
	

* _**lastUpdated**_ - This needs to be updated (dd-MM-yyyy format) if any of the child items in the `results` array have been updated.
* **id** - The primary numerical key used to identify each trackable.
* **marker** - This is the URL of the marker file to download.
* **augmentation** - This is the URL of the augmentation file to download. 
* **markerFileName** - This is the filename used when storing your marker to the device's local storage.
* **augmentationFileName** - This is the filename used when storing your augmentation file to the device's local storage.
* **lastUpdated** - The date the trackable was last updated (dd-MM-yyyy).
* **augmentationType** - The type of augmentation to be performed on a marker. The current demo supports `video` and `text`.   
When `video` is used, the video in `augmentation` will be overlaid over the markers contained in the `marker`.  
With `text`, the name of the marker will be drawn in a label in the centre of the screen.
* **displayFade** - The time in seconds it takes for the augmentation to fade into focus.
* **resetTime** - The time in seconds it takes for the augmentation to reset to its initial state.
* **augmentationRotation** - The rotation to be performed on an augmentation.
* **fillMarker** - Boolean (0/1) value for if the augmentation should fill the marker.
