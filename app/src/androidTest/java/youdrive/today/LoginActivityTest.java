package youdrive.today;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import youdrive.today.login.activities.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by psuhoterin on 26.05.15.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    public LoginActivityTest(Class<LoginActivity> activityClass) {
        super(activityClass);
    }

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        getActivity();
    }

    public void testListGoesOverTheFold() {
        onSuccessTest();
    }

    @Test
    public void onSuccessTest(){
        onView(withId(R.id.etLogin)).perform(typeText("testmail@example.com"));
        onView(withId(R.id.etPassword)).perform(typeText("test1234"));
        onView(withId(R.id.btnLogin)).perform(click());
    }

}
