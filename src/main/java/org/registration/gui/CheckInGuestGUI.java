package org.registration.gui;

import org.registration.service.BookingService;
import org.registration.service.GuestService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CheckInGuestGUI {

    private final GuestService guestService = new GuestService();
    private final BookingService bookingService = new BookingService();
    private final JDialog dialog;
    private final DefaultTableModel tableModel;

    public CheckInGuestGUI(JFrame parentFrame) {
        dialog = new JDialog(parentFrame, "Check-In Guest", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JPanel headingPanel = new JPanel();
        JLabel headingLabel = new JLabel("Check-In Guest");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingPanel.add(headingLabel);

        tableModel = new DefaultTableModel(new Object[]{"First Name", "Last Name"}, 0);
        JTable guestsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(guestsTable);

        JButton cancelButton = new JButton("Cancel");
        JButton checkInButton = new JButton("Check-In");

        cancelButton.addActionListener(e -> dialog.dispose());

        checkInButton.addActionListener(e -> {
            int selectedRow = guestsTable.getSelectedRow();
            if (selectedRow != -1) {
                String lastName = (String) tableModel.getValueAt(selectedRow, 1);
                try {
                    bookingService.checkin(lastName);
                    JOptionPane.showMessageDialog(dialog, "Guest checked in successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(checkInButton);

        dialog.add(headingPanel, BorderLayout.NORTH);
        dialog.add(tableScrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        populateGuestsTable();
        dialog.setVisible(true);
    }

    private void populateGuestsTable() {
        try {
            guestService.getAllGuest().forEach(guest -> tableModel.addRow(new Object[]{guest.getFirstName(), guest.getLastName()}));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error retrieving guest list: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
