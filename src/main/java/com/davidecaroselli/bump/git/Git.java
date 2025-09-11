package com.davidecaroselli.bump.git;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Git {

    public static Git open(File directory) {
        try {
            exec(directory, "git", "status", "--porcelain");
            return new Git(directory);
        } catch (GitError e) {
            return null;
        }
    }

    private final File directory;

    private Git(File directory) {
        this.directory = directory;
    }

    public boolean isClean() throws MojoExecutionException {
        return exec(directory, "git", "status", "--porcelain") == null;
    }

    public boolean hasHeadTag() throws MojoExecutionException {
        return exec(directory, "git", "tag", "--points-at", "HEAD") != null;
    }

    public boolean isMainBranch() throws MojoExecutionException {
        String branch = exec(directory, "git", "branch", "--show-current");
        return "main".equals(branch) || "master".equals(branch);
    }

    public void tag(String version) throws MojoExecutionException {
        exec(directory, "git", "add", "-A");
        exec(directory, "git", "commit", "-m", "v" + version);
        exec(directory, "git", "tag", "v" + version);
    }

    public static String exec(File dir, String... command) throws GitError {
        Process process;
        try {
            process = new ProcessBuilder(command)
                    .directory(dir)
                    .start();
        } catch (IOException e) {
            throw new GitError(-1, "failed to start process");
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String stderr = null;
                try (InputStream stderrStream = process.getErrorStream()) {
                    stderr = drain(stderrStream);
                } catch (IOException e) {
                    // skip
                }

                throw new GitError(exitCode, stderr);
            }

            String stdout;
            try (InputStream stdoutStream = process.getInputStream()) {
                stdout = drain(stdoutStream);
            } catch (IOException e) {
                stdout = null;
            }

            return stdout;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GitError(-1, "interrupted");
        }
    }

    private static String drain(InputStream stream) throws IOException {
        StringBuilder output = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            output.append(new String(buffer, 0, bytesRead));
        }

        String dump = output.toString().trim();
        return dump.isEmpty() ? null : dump;
    }

    @Override
    public String toString() {
        return "git@" + directory;
    }

}
