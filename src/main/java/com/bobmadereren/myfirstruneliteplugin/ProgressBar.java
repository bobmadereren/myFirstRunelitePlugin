package com.bobmadereren.myfirstruneliteplugin;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProgressBar extends JPanel {

    private final JLabel indexLabel;
    private final JLabel imageLabel;
    private final JProgressBar progressBar;

    public ProgressBar(int index) {
        setLayout(new BorderLayout(10, 10));

        indexLabel = new JLabel("Slot " + (index + 1));
        indexLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Example: Arial, Bold, 14pt

        imageLabel = new JLabel(new ImageIcon());
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(30, 30));

        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 0);
        progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 30));
        progressBar.setStringPainted(true);
        progressBar.setString("");
        progressBar.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Example: SansSerif, Plain, 12pt

        add(indexLabel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.EAST);
    }

    public void update(GrandExchangeOffer offer, ItemManager itemManager) {

        ItemComposition offerItem = itemManager.getItemComposition(offer.getItemId());
        boolean shouldStack = offerItem.isStackable() || offer.getTotalQuantity() > 1;
        BufferedImage itemImage = itemManager.getImage(offer.getItemId(), offer.getTotalQuantity(), shouldStack);

        imageLabel.setIcon(new ImageIcon(itemImage));
        progressBar.setMaximum(offer.getTotalQuantity());
        progressBar.setValue(offer.getQuantitySold());
        progressBar.setString(offer.getQuantitySold() + " / " + offer.getTotalQuantity());
    }
}