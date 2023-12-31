package org.registration.gui;

import org.registration.service.BookingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;

public class BookingHistoryGUI {

    public BookingHistoryGUI(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Booking History", true);
        dialog.setSize(800, 600);
        dialog.setLayout(new BorderLayout());

        JLabel headingLabel = new JLabel("Booking History");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Guest Name", "Room Number", "Room Status", "Checked In", "Checked Out"}, 0);
        JTable historyTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(historyTable);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(closeButton);

        dialog.add(headingLabel, BorderLayout.NORTH);
        dialog.add(tableScrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        populateHistoryTable(tableModel);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private static void populateHistoryTable(DefaultTableModel tableModel) {
        try {
            BookingService bookingService = new BookingService();
            var sortedByName = bookingService.getHistory().stream().sorted(Comparator.comparing(booking -> booking.getGuestName().split("\\s+")[1])).toList();
            sortedByName.forEach(booking -> tableModel.addRow(new Object[]{booking.getGuestName(), booking.getRoomNumber(), booking.isAvailability() ? "Available" : "Booked", booking.getCheckedIn(), booking.getCheckedOut()}));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving booking history: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
