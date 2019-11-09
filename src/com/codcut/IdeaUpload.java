package com.codcut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import okhttp3.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdeaUpload extends AnAction {
    final private String notificationGroup = "codcut";
    final private String plainFileExtension = "txt";
    final private String uploadTitle = "Codcut";
    final private String errorText = "Something went wrong. Please try again later";
    final private String noFileExtWarn = "Could not find file extension, uploading as plain";

    final private String codcutUrl = "https://resource.codcut.com/api/posts";
    final private OkHttpClient client = new OkHttpClient();

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CommentDialog dialog = new CommentDialog();

        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final CodcutSettings settings = ServiceManager.getService(project, CodcutSettings.class);

        Boolean result = dialog.showAndGet();
        String comment = dialog.commentArea.getText();

        Document document = editor.getDocument();
        String path = FileDocumentManager.getInstance().getFile(document).getPath();
        Matcher matcher = Pattern.compile(".*\\.(.*)$").matcher(path);

        String extension;

        if (matcher.matches())
            extension = matcher.group(1);
        else {
            warn(noFileExtWarn);

            extension = plainFileExtension;
        }

        SelectionModel selectionModel = editor.getSelectionModel();
        String code = selectionModel.getSelectedText();

        ObjectNode requestJson = mapper.createObjectNode();
        requestJson.put("code", code);
        requestJson.put("body", comment);
        requestJson.put("language", extension);

        RequestBody body =
            RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestJson.toString());

        Request request = new Request.Builder()
          .header("Authorization", "Bearer " + settings.getToken())
          .url(codcutUrl)
          .post(body)
          .build();

        try (Response response = client.newCall(request).execute()) {
            String text = response.body().string();

            // info(text);

            CodcutResponse codcutResponse = mapper.readValue(text, CodcutResponse.class);
            String postUrl = "https://codcut.com/posts/" + codcutResponse.getId();

            info("Snippet uploaded to <a href=\"" + postUrl + "\">" + postUrl + "</a>");
        } catch (Exception err) {
            error(errorText);
        }
    }

    private void info(String text) {
        Notifications.Bus.notify(
            new Notification(notificationGroup, uploadTitle, text, NotificationType.INFORMATION)
        );
    }

    private void warn(String text) {
        Notifications.Bus.notify(
            new Notification(notificationGroup, uploadTitle, text, NotificationType.WARNING)
        );
    }

    private void error(String text) {
        Notifications.Bus.notify(
            new Notification(notificationGroup, uploadTitle, text, NotificationType.ERROR)
        );
    }
}
