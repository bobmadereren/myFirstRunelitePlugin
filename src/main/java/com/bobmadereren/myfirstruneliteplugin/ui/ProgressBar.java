package com.bobmadereren.myfirstruneliteplugin.ui;

import com.bobmadereren.myfirstruneliteplugin.offer.Offer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProgressBar extends JPanel {

    private final JLabel nameLabel;

    private final JLabel detailsLabel;

    private final JLabel imageLabel;

    private final JProgressBar progressBar;

    private final JProgressBar collectedBar;

    public ProgressBar(int index) {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(new EmptyBorder(7, 7, 7, 7));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);

        nameLabel = new JLabel("Name");
        nameLabel.setFont(FontManager.getRunescapeSmallFont());

        detailsLabel = new JLabel("Details");
        detailsLabel.setFont(FontManager.getRunescapeSmallFont());

        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);

        imageLabel = new JLabel(new ImageIcon());
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel dualProgressBar = new JPanel();
        dualProgressBar.setLayout(new OverlayLayout(dualProgressBar));

        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressBar.setValue(70);
        progressBar.setForeground(new Color(70, 167, 43));

        collectedBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        collectedBar.setValue(30);
        collectedBar.setForeground(new Color(0, 255, 0));
        collectedBar.setBackground(new Color(0, 0, 0, 0));
        collectedBar.setOpaque(false);

        dualProgressBar.add(collectedBar);
        dualProgressBar.add(progressBar);

        add(infoPanel, BorderLayout.CENTER);
        add(imageLabel, BorderLayout.WEST);
        add(dualProgressBar, BorderLayout.SOUTH);
    }

    public void update(Offer offer, ItemManager itemManager) {
        if(offer == null) {
            setVisible(false);
            return;
        }

        BufferedImage itemImage = itemManager.getImage(offer.getItem().getId());
        imageLabel.setIcon(new ImageIcon(itemImage));

        nameLabel.setText(offer.getItem().getName());

        detailsLabel.setText("Collected / Sold / Total: " + offer.getProgressCollected() + " / " + offer.getProgressSold() + " / " + offer.getProgressTotal());

        collectedBar.setMaximum(offer.getProgressTotal());
        collectedBar.setValue(offer.getProgressCollected());

        progressBar.setMaximum(offer.getProgressTotal());
        progressBar.setValue(offer.getProgressSold());

        setVisible(true);
    }
}