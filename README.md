# FunAmp

## Why FunAmp?
I love music! I have too many songs in my phone and I want to be able to see lyrics in my app itself instead of browsing in google!
I want to take this not so impressive idea as a base and develop a scalable, testable, robust application. I developed this application for self learning purposes only. Not for commercial use. 

## Implementation
* Used MVP design pattern to make this app scalable considering future enhancements to this idea. In this implementation I implemented one presenter(Music Presenter) with multiple views (MusicView, LyricView) for convenience.
* Used Dagger2 dependency injection to make this app testable.
* Used Mockito, JUnit and Hamcrest frameworks for unit testing.
* Used Espresso for instrumentation testing.

## Requirements
Android SDK 17+

## Libraries
* JSoup     - For crawling lyrics from azlyrics.com
* Gson      - For Serialization nd Deserialization
* Dagger2   - For dependency injection
* Espresso  - For instrumentaion testing
* JUnit, Mockito, Hamcrest, Guava - For Unit testing

## TODO
* Implement caching for music loaded from device
* Implement caching for lyrics crawled once to avoid repeat calls. Possibly map lyrics to song.
* Further enhance media player by implementing auto play and shuffle
* Beautify UI design overall
* Play with Exo player library and map the crawled lyrics into exo player 

## Author
Sarath

## Bug report?
Please open an issue. Screenshots are also a huge help if the problem is visual.

If you're fixing a bug, Send a pull request!
