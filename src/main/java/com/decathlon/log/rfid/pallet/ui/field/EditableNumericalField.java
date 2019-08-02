package com.decathlon.log.rfid.pallet.ui.field;

import com.decathlon.log.rfid.keyboard.ui.textfield.EditableTextField;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;

public class EditableNumericalField extends EditableTextField {

    private static final Color FOCUS_BG_COLOR = Color.RED;

    public EditableNumericalField(final int maxCharacters) {
        super(maxCharacters);
        setCaretColor(Color.BLACK);
    }

    @Override
    public void focusGained(final FocusEvent e) {
        changeBackgroundColor(FOCUS_BG_COLOR);
        RFIDPalletApp.getApplication().showVirtualKeyBoard(this);
        selectAll();
    }

    @Override
    public void focusLost(final FocusEvent e) {
    }

    @Override
    public void addCharacter(final String text) {
        if (hasSelectedText()) {
            setText("");
        }
        super.addCharacter(text);
    }

    @Override
    public boolean isOnlyNumerical() {
        return true;
    }

    @Override
    public boolean hasAllowedByKeyBoard(char c) {
        return Character.isDigit(c);
    }

    private void changeBackgroundColor(final Color color) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setBackground(color);
            }
        });
    }

    private boolean hasSelectedText() {
        return getSelectedText() != null && !getSelectedText().isEmpty();
    }
}
