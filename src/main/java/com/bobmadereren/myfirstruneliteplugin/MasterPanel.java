package com.bobmadereren.myfirstruneliteplugin;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MasterPanel extends PluginPanel {

    private final Client client;
    private final MyPlugin plugin;

    @Inject
    MasterPanel(Client client, MyPlugin plugin){
        super();
        this.client = client;
        this.plugin = plugin;
        createPanel();
    }

    private void createPanel(){
        setBackground(Color.orange);

        JButton button = new JButton("I am the button");
        button.addActionListener(plugin.setButtonClicked);

        JButton widgetButton = new JButton("Widget");
        widgetButton.addActionListener(e -> {
            Widget historyWidget = client.getWidget(383, 3);
            if(historyWidget != null) {
                Widget[] widgets = historyWidget.getDynamicChildren();
                System.out.println("Number of widgets: " + widgets.length);
                if (widgets.length >= 6) {
                    Widget priceWidget = widgets[5];
                    String text = priceWidget.getText();
                    System.out.println(text);
                }
            } else {
                System.out.println("History Widget is null");
            }
        });

        JButton historyButton = new JButton("History button");
        historyButton.addActionListener(e -> {
            Widget widget = client.getWidget(465, 3);
            if(widget != null){
                Object[] objects = widget.getOnOpListener();
                if(objects != null) {
                    for (Object object : objects) {
                        if (object != null) {
                            System.out.println("Type: " + object.getClass() + ", To string: " + object.toString());
                        } else {
                            System.out.println("Object (script / argument) is null");
                        }

                    }
                } else {
                    System.out.println("Object[] is null (has no script)");
                }
            } else {
                System.out.println("History button is null");
            }
        });

        JButton changeCollectScript = new JButton("Change collect script");
        changeCollectScript.addActionListener(e -> {
            Widget history = client.getWidget(465, 3);
            Widget collectHead = client.getWidget(465, 6);
            if(history == null || collectHead == null){
                System.out.println("History or Collect-Head is null");
            } else {
                Object[] historyScript = history.getOnOpListener();
                collectHead.setOnOpListener(historyScript);
                System.out.println("New script added to Collect Head: " + Arrays.deepToString(historyScript));
            }
        });

        add(button);
        add(widgetButton);
        add(historyButton);
        add(changeCollectScript);
    }

}
