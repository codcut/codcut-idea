package com.codcut;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

// Huge thanks to: https://github.com/scalameta/scalafmt/blob/master/scalafmt-intellij/src/main/scala/org/scalafmt/intellij/SettingsGui.scala
public class CodcutSettingsGUI implements SearchableConfigurable {
    private JTextField tokenField = new JTextField(50);
    private Project project;

    public CodcutSettingsGUI(Project _project) {
        project = _project;
    }

    @NotNull
    @Override
    public String getId() {
        return "preferences.CodcutPlugin";
    }

    @Override
    public String getDisplayName() {
        return "preferences.CodcutPlugin";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel pathToConfigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pathToConfigPanel.add(new JLabel("Codcut token: "));
        pathToConfigPanel.add(tokenField);
        pathToConfigPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(pathToConfigPanel);

        return panel;
    }

    @Override
    public boolean isModified() {
        CodcutSettings settings = ServiceManager.getService(project, CodcutSettings.class);

        return !tokenField.getText().equals(settings.getToken());
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Override
    public void reset() {
        CodcutSettings settings = ServiceManager.getService(project, CodcutSettings.class);

        tokenField.setText(settings.getToken());
    }

    @Override
    public void apply() {
        CodcutSettings settings = ServiceManager.getService(project, CodcutSettings.class);

        settings.setToken(tokenField.getText());
    }
}
