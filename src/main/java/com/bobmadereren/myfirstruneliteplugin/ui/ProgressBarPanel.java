package com.bobmadereren.myfirstruneliteplugin.ui;

import javax.inject.Inject;
import javax.swing.*;

public class ProgressBarPanel extends JPanel {

    private final ProgressBar[] progressBars;

    @Inject
    public ProgressBarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        progressBars = new ProgressBar[8];
        for (int i = 0; i < 8; i++) {
            ProgressBar progressBar = new ProgressBar(i);
            add(progressBar);

            progressBars[i] = progressBar;
        }
    }

    public ProgressBar getProgressBar(int index) {
        return progressBars[index];
    }
}