package com.codcut;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;

// Huge thanks to: https://github.com/scalameta/scalafmt/blob/master/scalafmt-intellij/src/main/scala/org/scalafmt/intellij/IdeaSettings.scala
@State(name = "CodcutSettings", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class CodcutSettings implements PersistentStateComponent<CodcutSettings> {
    private String token = "";

    public String getToken() {
        return token;
    }

    public void setToken(String _token) {
        token = _token;
    }

    @Nullable
    @Override
    public CodcutSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CodcutSettings codcutSettings) {
        XmlSerializerUtil.copyBean(codcutSettings, this);
    }
}
