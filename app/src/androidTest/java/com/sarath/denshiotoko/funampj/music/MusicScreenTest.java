package com.sarath.denshiotoko.funampj.music;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.sarath.denshiotoko.funampj.MyMusicApplication;
import com.sarath.denshiotoko.funampj.TestUtils;
import com.sarath.denshiotoko.funampj.data.Song;
import com.sarath.denshiotoko.funampj.data.source.MusicRepository;
import com.sarath.denshiotoko.funampj.util.EspressoIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static com.sarath.denshiotoko.funampj.TestUtils.getCurrentActivity;
import static org.hamcrest.Matchers.allOf;

/**
 * Tests for the tasks screen, the main screen which contains a list of all tasks.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MusicScreenTest {

    private final static String TITLE1 = "TITLE1";

    private final static String DESCRIPTION = "DESCR";

    private final static String TITLE2 = "TITLE2";

    List<Song> songsList;

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MusicActivity> mTasksActivityTestRule =
            new ActivityTestRule<MusicActivity>(MusicActivity.class) {

                /**
                 * Collect the song list before each test.
                 */
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    // Doing this in @Before generates a race condition.
                    ((MyMusicApplication) InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).getTasksRepository().getSongs(new MusicRepository.LoadMusicCallback() {
                        @Override
                        public void onMusicLoaded(List<Song> songs) {
                            songsList = songs;
                        }

                        @Override
                        public void onDataNotAvailable() {
                            songsList = new ArrayList<>();
                        }
                    });
                }
            };

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests
     * significantly more reliable.
     */
    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * `
     * A custom {@link Matcher} which matches an item in a {@link ListView} by its text.
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link ListView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(ListView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA LV with text " + itemText);
            }
        };
    }

    @Test
    public void showAllSongs() {

        for(Song s: songsList)
            onView(withItemText(s.getSongTitle())).check(matches(isDisplayed()));
    }

    @Test
    public void onViewClicked_PlaySong(){
        onView(withText("Gatsu")).perform(click());
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Gatsu")).perform(click());
    }

/*    @Test
    public void orientationChanged_showAllSongs(){

        onView(withText("Gatsu")).check(matches(isDisplayed()));
        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

        *//*for(Song s: songsList)
            onView(withItemText(s.getSongTitle())).check(matches(isDisplayed()));*//*
        onView(withText("Gatsu")).check(matches(isDisplayed()));
    }

    @Test
    public void orientationChanged_playSongs(){
        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

        onView(withText("Gatsu")).perform(click());
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Gatsu")).perform(click());
    }*/
}
