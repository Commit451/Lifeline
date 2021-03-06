# Lifeline
Easy access to important Android lifecycle information

[![Build Status](https://travis-ci.org/Commit451/Lifeline.svg?branch=master)](https://travis-ci.org/Commit451/Lifeline)
[![](https://jitpack.io/v/Commit451/Lifeline.svg)](https://jitpack.io/#Commit451/Lifeline)

# Basic Usage
In your `Application` class
```kotlin
Lifeline.init(this)
```
If you want to check to see if the app is in the foreground:
```kotlin
val inForeground = Lifeline.isInForeground()
```
If you want to track time the app has been paused:
```kotlin
//ideally in your onResume
val time = Lifeline.timeSpentOutsideApp()
```
If you want to track when the app is backgrounded:
```kotlin
Lifeline.registerOnBackgroundedListener {
    Toast.makeText(this, "On backgrounded", Toast.LENGTH_SHORT)
            .show()
}
```
If you want to track when the app is foregrounded:
```kotlin
Lifeline.registerOnForegroundedListener {
    Toast.makeText(this, "On foregrounded", Toast.LENGTH_SHORT)
            .show()
}
```
Make sure to unregister these when appropriate

License
--------

    Copyright 2017 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
