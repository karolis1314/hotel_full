package org.registration;

import org.registration.gui.BookingHistoryGUI;
import org.registration.gui.CheckInGuestGUI;
import org.registration.gui.CheckOutGuestGUI;
import org.registration.gui.RegisterGuestGUI;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hotel Reservation System");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel headingPanel = new JPanel();
        JLabel headingLabel = new JLabel("Hotel Reservation System");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingPanel.add(headingLabel);

        JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton registerButton = new JButton("Register Guest");
        JButton checkInButton = new JButton("Check-In");
        JButton checkOutButton = new JButton("Room Info");
        JButton historyButton = new JButton("History");
        JButton exitButton = new JButton("Exit");

        registerButton.addActionListener(e -> {
            new RegisterGuestGUI(frame);
        });

        checkInButton.addActionListener(e -> {
            new CheckInGuestGUI(frame);
        });

        checkOutButton.addActionListener(e -> {
           new CheckOutGuestGUI(frame);
        });

        historyButton.addActionListener(e -> {
            new BookingHistoryGUI(frame);
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonsPanel.add(registerButton);
        buttonsPanel.add(checkInButton);
        buttonsPanel.add(checkOutButton);
        buttonsPanel.add(historyButton);
        buttonsPanel.add(exitButton);

        frame.add(headingPanel, BorderLayout.NORTH);
        frame.add(buttonsPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
