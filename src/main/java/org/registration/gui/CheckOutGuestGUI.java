package org.registration.gui;

import org.registration.service.BookingService;
import org.registration.service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CheckOutGuestGUI {

    private final RoomService roomService = new RoomService();
    private final BookingService bookingService = new BookingService();
    private final JDialog dialog;
    private final DefaultTableModel tableModel;

    public CheckOutGuestGUI(JFrame parentFrame) {
        dialog = new JDialog(parentFrame, "Room Info", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JPanel headingPanel = new JPanel();
        JLabel headingLabel = new JLabel("Booked rooms");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingPanel.add(headingLabel);

        tableModel = new DefaultTableModel(new Object[]{"Room Number", "Full Name"}, 0);
        JTable guestsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(guestsTable);

        JButton cancelButton = new JButton("Cancel");
        JButton checkInButton = new JButton("Check-Out");

        cancelButton.addActionListener(e -> dialog.dispose());

        checkInButton.addActionListener(e -> {
            int selectedRow = guestsTable.getSelectedRow();
            if (selectedRow != -1) {
                String lastName = (String) tableModel.getValueAt(selectedRow, 1);
                try {
                    bookingService.checkout(lastName);
                    JOptionPane.showMessageDialog(dialog, "Guest checked out successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
            roomService.getAllBookedRooms().forEach(room -> tableModel.addRow(new Object[]{room.getId(), room.getGuestName()}));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error retrieving guest list: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
