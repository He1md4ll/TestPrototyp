package edu.hsb.proto.test;

import android.content.SharedPreferences;

import com.google.android.gms.tasks.Task;
import com.google.common.truth.Truth;

import org.junit.Test;

import javax.inject.Inject;

import edu.hsb.proto.test.base.BaseUnitTest;
import edu.hsb.proto.test.base.Real;
import edu.hsb.proto.test.base.UnitTestComponent;

public class PreferenceManagerTest extends BaseUnitTest {

    @Real
    @Inject
    PreferenceManager classUnderTest;

    @Real
    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void inject(UnitTestComponent component) {
        component.inject(this);
    }

    @Test
    public void testGetPreference() {
        // Given
        final String username = "testUser";
        sharedPreferences.edit().putString(PreferenceManager.PREF_USERNAME, username).commit();

        // When
        final String result = classUnderTest.getUsername();

        // Then
        Truth.assertThat(result).isEqualTo(username);
    }

    @Test
    public void testSetPreference() {
        // Given
        final String username = "testUser";

        // When
        final Task task = classUnderTest.setUsername(username);

        // Then
        task.addOnCompleteListener(task1 -> {
            Truth.assertThat(task1.isSuccessful()).isTrue();
            Truth.assertThat(sharedPreferences.getAll())
                    .containsEntry(PreferenceManager.PREF_USERNAME, username);
        });
    }

    @Test
    public void testSetPreferenceNull() {
        // When
        final Task task = classUnderTest.setUsername(null);

        // Then
        task.addOnCompleteListener(task1 -> {
            Truth.assertThat(task1.isSuccessful()).isTrue();
            Truth.assertThat(sharedPreferences.getAll())
                    .containsEntry(PreferenceManager.PREF_USERNAME, null);
        });
    }
}