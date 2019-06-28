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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdeaUpload extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        SampleDialogWrapper dialog = new SampleDialogWrapper();

        Boolean result = dialog.showAndGet();
        String comment = dialog.commentArea.getText();
        String extension = getFileExtension(editor);
        String code = getSelection(editor);

        Notifications.Bus.notify(
                new Notification("codcut", dialog.commentArea.getText(),
                        new StringBuilder().append(result.toString())
                                .append(" <a href=\"http://www.google.it\">test</a> ").append(extension).append(" ").append(code).toString(),
                        NotificationType.INFORMATION)
        );

    }

    private String getFileExtension(Editor editor) {
        Document document = editor.getDocument();
        String path = FileDocumentManager.getInstance().getFile(document).getPath();
        Matcher matcher = Pattern.compile(".*\\.(.*)$").matcher(path);

        matcher.matches();

        return matcher.group(1);
    }

    private String getSelection(Editor editor) {
        SelectionModel selectionModel = editor.getSelectionModel();
        return selectionModel.getSelectedText();
    }
}
