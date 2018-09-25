package edu.hsb.proto.test;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import edu.hsb.proto.test.service.ILoginService;

public class LoginFragment extends Fragment {

    @Inject
    ILoginService loginService;

    private EditText usernameText;
    private EditText passwordText;
    private TextView roundsLabel;
    private SeekBar roundsSeekBar;
    private CheckBox encryptionCheckBox;
    private Button loginButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameText = view.findViewById(R.id.login_username);
        passwordText = view.findViewById(R.id.login_password);
        roundsLabel = view.findViewById(R.id.login_rounds_label);
        roundsSeekBar = view.findViewById(R.id.login_rounds);
        encryptionCheckBox = view.findViewById(R.id.login_encryption);
        loginButton = view.findViewById(R.id.login_button);

        roundsLabel.setText(getString(R.string.login_title_rounds, roundsSeekBar.getProgress()));
        roundsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                roundsLabel.setText(getString(R.string.login_title_rounds, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        loginButton.setOnClickListener(v -> {
            final String passwordHash = loginService.hash(passwordText.getText().toString(),
                    roundsSeekBar.getProgress(),
                    encryptionCheckBox.isChecked());
            final boolean result = loginService.login(usernameText.getText().toString(),
                    passwordHash,
                    roundsSeekBar.getProgress(),
                    encryptionCheckBox.isChecked());
            Toast.makeText(LoginFragment.this.getContext(), "Result: " + result, Toast.LENGTH_LONG).show();
        });
    }
}