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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Hotel Reservation System");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel headingPanel = new JPanel();
        JLabel headingLabel = new JLabel("Hotel Reservation System");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingPanel.add(headingLabel);

        JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton registerButton = new JButton("Register Guest");
        JButton checkInButton = new JButton("Check-In");
        JButton checkOutButton = new JButton("Check-Out");
        JButton historyButton = new JButton("Booking History");
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

        registerButton.setBackground(new Color(52, 152, 219));
        checkInButton.setBackground(new Color(46, 204, 113));
        checkOutButton.setBackground(new Color(231, 76, 60));
        historyButton.setBackground(new Color(155, 89, 182));
        exitButton.setBackground(new Color(149, 165, 166));

        buttonsPanel.add(registerButton);
        buttonsPanel.add(checkInButton);
        buttonsPanel.add(checkOutButton);
        buttonsPanel.add(historyButton);
        buttonsPanel.add(exitButton);

        frame.add(headingPanel, BorderLayout.NORTH);
        frame.add(buttonsPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
