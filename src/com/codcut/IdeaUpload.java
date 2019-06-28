package com.codcut;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.util.regex.Pattern;

public class IdeaUpload extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        SampleDialogWrapper dialog = new SampleDialogWrapper();

        Boolean result = dialog.showAndGet();
        String comment = dialog.commentArea.getText();

        //Get all the required data from data keys
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();

        String path = FileDocumentManager.getInstance().getFile(document).getPath();

        Notifications.Bus.notify(
                new Notification("codcut", dialog.commentArea.getText(), path, NotificationType.INFORMATION));

        String extension = Pattern.compile(".*\\.(.*)$").matcher(path).group(1);
        String code = selectionModel.getSelectedText();

        Notifications.Bus.notify(
                new Notification("codcut", dialog.commentArea.getText(),
                        new StringBuilder().append(result.toString())
                                .append(" <a href=\"http://www.google.it\">test</a> ").append(extension).append(" ").append(code).toString(),
                        NotificationType.INFORMATION)
        );

    }
}
