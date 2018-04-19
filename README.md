# FunAmp

## Why FunAmp?
I love music! I have too many songs in my phone and I want to be able to see lyrics in my app itself instead of browsing in google!
I want to take this basic idea as a base and develop a scalable, testable, robust application. I developed this application for self learning purposes only. Not for commercial use. 

## Implementation
* Used MVP design pattern to make this app scalable considering future enhancements to this idea. In this implementation I implemented one presenter(Music Presenter) with multiple views (MusicView, LyricView) for convenience.
* Used Dagger2 dependency injection to make this app testable.
* Used Mockito, JUnit and Hamcrest frameworks for unit testing.
* Used Espresso for instrumentation testing.

## Requirements
Android SDK 17+

## Libraries
* [JSoup](https://mvnrepository.com/artifact/org.jsoup/jsoup)     - For crawling lyrics from azlyrics.com
* [Gson](https://github.com/google/gson)      - For Serialization nd Deserialization
* [Dagger2](https://google.github.io/dagger/)   - For dependency injection
* [Espresso](https://github.com/googlesamples/android-testing/tree/master/ui/espresso)  - For instrumentaion testing
* [JUnit](https://mvnrepository.com/artifact/junit/junit), [Mockito](http://site.mockito.org/), [Hamcrest](http://hamcrest.org/JavaHamcrest/), [Guava](https://github.com/google/guava) - For Unit testing

## TO-DO
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

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
