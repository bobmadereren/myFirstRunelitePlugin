package com.bobmadereren.myfirstruneliteplugin;

import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class MasterPanel extends PluginPanel {

    @Inject
    private MyPlugin plugin;

    MasterPanel(){
        super();
        createPanel();
    }

    private void createPanel(){
        setBackground(Color.orange);
        JButton button = new JButton("I am the button");
        button.addActionListener(e -> {
            System.out.println("Clicking button!");
            plugin.clickButton();
        });
        add(button);
    }

}
