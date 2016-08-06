# YouTubeExtractor
A helper to extract the streaming URL from a YouTube video

[![Build Status](https://travis-ci.org/Commit451/YouTubeExtractor.svg?branch=master)](https://travis-ci.org/Commit451/YouTubeExtractor)
[![](https://jitpack.io/v/Commit451/YouTubeExtractor.svg)](https://jitpack.io/#Commit451/YouTubeExtractor)

This library was originally found [here](https://github.com/flipstudio/YouTubeExtractor) in a project by [flipstudio](https://github.com/flipstudio). It has since been modified and cleaned up a bit to make it more user friendly.

# Gradle Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Then, add the library to your project `build.gradle`
```gradle
dependencies {
    compile 'com.github.Commit451.YouTubeExtractor:youtubeextractor:2.1.0'
}
```
or, if you want to use the [RxJava](https://github.com/ReactiveX/RxJava) version of the extractor:
```gradle
dependencies {
    compile 'com.github.Commit451.YouTubeExtractor:youtubeextractor:2.1.0'
    //You will still need the dependency above
    compile 'com.github.Commit451.YouTubeExtractor:rxyoutubeextractor:2.1.0'
}
```

# Usage
Under the hood, this library uses [Retrofit](http://square.github.io/retrofit/) to fetch the video metadata. If you are familiar with the Retrofit public API, this library will be a breeze for you.

Typical usage looks like this:
```java
// You probably would want to keep one of these extractors around.
YouTubeExtractor extractor = YouTubeExtractor.create();
mExtractor.extract("9d8wWcJLnFI").enqueue(new Callback<YouTubeExtractionResult>() {
    @Override
    public void onResponse(Call<YouTubeExtractionResult> call, Response<YouTubeExtractionResult> response) {
        Uri hdUri = result.getHd1080VideoUri();
        //See the sample for more
    }

    @Override
    public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {
        t.printStackTrace();
        //Alert your user!
    }
});
```
And for RxJava users:
```java
RxYouTubeExtractor rxYouTubeExtractor = RxYouTubeExtractor.create();
Observable<YouTubeExtractionResult> result = rxYouTubeExtractor.extract("9d8wWcJLnFI");
result.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<YouTubeExtractionResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                //Alert your user!
            }

            @Override
            public void onNext(YouTubeExtractionResult youTubeExtractionResult) {
                Uri hdUri = youTubeExtractionResult.getHd1080VideoUri();
                //See the sample for more
            }
        });
```
Note: the above example also requires [RxAndroid](https://github.com/ReactiveX/RxAndroid) for `AndroidSchedulers`

As you can with Retrofit, you can also extract the result right away:
```java
// this will extract the result on the current thread. Don't use this on the main thread!
Response<YouTubeExtractionResult> response = extractor.extract(GRID_VIDEO_ID).execute();
if (response.isSuccessful()) {
    //do your thing(s)
}
```

# Video Playback
As stated before, this library was only created to extract video stream Urls from YouTube. I recommend using the [ExoMedia](https://github.com/brianwernick/ExoMedia) library to play the video streams to the user. See the sample app for this library for an example.

License
--------

    Copyright 2016 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
