package com.bobmadereren.myfirstruneliteplugin;

import lombok.Getter;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class MasterPanel extends PluginPanel {

    private JTabbedPane tabbedPane;

    @Getter
    private ProgressBarPanel progressBarPanel;

    @Inject
    public MasterPanel(ItemManager itemManager) {
        super();

        // Set layout manager
        setLayout(new BorderLayout());

        // Create a JTabbedPane
        tabbedPane = new JTabbedPane();

        // Create three pages (you can customize these pages)
        JPanel page1 = createPage("Page 1 Content");
        JPanel page2 = createPage("Page 2 Content");
        JPanel page3 = createPage("Page 3 Content");

        // Add pages to the tabbedPane
        tabbedPane.addTab("Page 1", null, page1, "Page 1 Tooltip");
        tabbedPane.addTab("Page 2", null, page2, "Page 2 Tooltip");
        tabbedPane.addTab("Page 3", null, page3, "Page 3 Tooltip");

        // Create a ProgressBarPanel with constructor injection and add it to the panel
        progressBarPanel = new ProgressBarPanel();
        tabbedPane.addTab("Progress Bars", null, progressBarPanel, "Progress Bars");

        // Add the tabbedPane to the panel
        add(tabbedPane, BorderLayout.CENTER);

        // Add an event listener to handle tab changes
        tabbedPane.addChangeListener(e -> handleTabChange());
    }

    private JPanel createPage(String content) {
        JPanel pagePanel = new JPanel();
        pagePanel.add(new JLabel(content));
        return pagePanel;
    }

    private void handleTabChange() {
        // Handle tab change event, update the UI or perform other actions
        int selectedIndex = tabbedPane.getSelectedIndex();
        System.out.println("Selected Tab Index: " + selectedIndex);
    }
}