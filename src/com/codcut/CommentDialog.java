package com.codcut;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CommentDialog extends DialogWrapper {
    JTextArea commentArea;

    public CommentDialog() {
        super(true); // use current window as parent
        init();
        setTitle("Upload snippet to codcut");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Leave a comment");
        label.setPreferredSize(new Dimension(100, 100));
        dialogPanel.add(label, BorderLayout.PAGE_START);

        commentArea = new JTextArea();
        commentArea.setPreferredSize(new Dimension(200, 200));
        commentArea.grabFocus();
        dialogPanel.add(commentArea, BorderLayout.CENTER);

        return dialogPanel;
    }
}
