package com.davidecaroselli.bump;

import com.davidecaroselli.bump.xml.Position;
import com.davidecaroselli.bump.xml.XMLFinder;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PomFile {

    public static PomFile read(File file) throws MojoExecutionException {
        String content;

        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read POM file \"" + file + "\"", e);
        }

        return new PomFile(file, content);
    }

    private final File file;
    private String content;

    private PomFile(File file, String content) {
        this.file = file;
        this.content = content;
    }

    public File file() {
        return file;
    }

    public String getVersion() {
        Position versionTag = XMLFinder.find(content, "project", "version");
        return versionTag == null ? null : versionTag.getText(content).trim();
    }

    public String getParentVersion() {
        Position versionTag = XMLFinder.find(content, "project", "parent", "version");
        return versionTag == null ? null : versionTag.getText(content).trim();
    }

    public void setVersion(Version version) {
        setVersion(version.toString());
    }

    public void setVersion(String version) {
        Position versionTag = XMLFinder.find(content, "project", "version");
        if (versionTag == null)
            throw new IllegalStateException("Failed to find <version> tag in POM file \"" + file + "\"");

        content = versionTag.replace(content, version);
    }

    public void setParentVersion(Version version) {
        setParentVersion(version.toString());
    }

    public void setParentVersion(String version) {
        Position versionTag = XMLFinder.find(content, "project", "parent", "version");
        if (versionTag == null)
            throw new IllegalStateException("Failed to find parent <version> tag in POM file \"" + file + "\"");

        content = versionTag.replace(content, version);
    }

    public void save() throws MojoExecutionException {
        try {
            Files.write(file.toPath(), content.getBytes());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write POM file \"" + file + "\"", e);
        }
    }

}
