package com.bobmadereren.myfirstruneliteplugin.ui;

import com.bobmadereren.myfirstruneliteplugin.offer.Offer;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProgressBar extends JPanel {

    private final JLabel indexLabel;

    private final JLabel imageLabel;

    private final JProgressBar progressBar;

    private final JProgressBar collectedBar;

    public ProgressBar(int index) {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(new EmptyBorder(7, 7, 7, 7));

        indexLabel = new JLabel("Slot " + (index + 1));

        imageLabel = new JLabel(new ImageIcon());
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(30, 30));

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

        add(indexLabel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.CENTER);
        add(dualProgressBar, BorderLayout.SOUTH);
    }

    public void update(Offer offer, ItemManager itemManager) {
        if(offer == null) {
            setVisible(false);
            return;
        }

        ItemComposition item = offer.getItem();
        boolean shouldStack = item.isStackable() || offer.getProgressTotal() > 1;
        BufferedImage itemImage = itemManager.getImage(item.getId(), offer.getProgressTotal(), shouldStack);
        imageLabel.setIcon(new ImageIcon(itemImage));

        collectedBar.setMaximum(offer.getProgressTotal());
        collectedBar.setValue(offer.getProgressCollected());

        progressBar.setMaximum(offer.getProgressTotal());
        progressBar.setValue(offer.getProgressSold());
        progressBar.setString(offer.getProgressSold() + " / " + offer.getProgressTotal());

        setVisible(true);
    }
}