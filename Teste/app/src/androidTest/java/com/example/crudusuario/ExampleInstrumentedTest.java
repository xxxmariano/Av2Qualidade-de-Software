package com.example.crudusuario;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, true);

    @Test
    public void testeComponentes() {
        onView(withId(R.id.btSalvar)).perform(click());
    }

    @Test
    public void testeComponentes2() {
        onView(withId(R.id.txtUsuarioAut)).perform(typeText("Teste"), closeSoftKeyboard());
        onView(withId(R.id.btSalvar)).perform(click());
    }

    @Test
    public void testeIntent() {
        Intents.init();
        onView(withId(R.id.btPesq)).perform(click());
        Intents.release();
    }
}