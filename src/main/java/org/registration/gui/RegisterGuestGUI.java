package org.registration.gui;

import org.registration.service.GuestService;

import javax.swing.*;
import java.awt.*;
public class RegisterGuestGUI {

    private final GuestService guestService = new GuestService();

    public RegisterGuestGUI(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Register Guest", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new FlowLayout());

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField(15);

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField(15);

        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save");

        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();


            if (!firstName.isEmpty() || !lastName.isEmpty()) {
                try {
                    guestService.createGuest(firstName, lastName);
                    JOptionPane.showMessageDialog(dialog, "Guest registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please fill in both first name and last name", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(firstNameLabel);
        dialog.add(firstNameField);
        dialog.add(lastNameLabel);
        dialog.add(lastNameField);
        dialog.add(cancelButton);
        dialog.add(saveButton);

        dialog.setVisible(true);
    }
}

