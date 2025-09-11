package com.davidecaroselli.bump.git;

import org.apache.maven.plugin.MojoExecutionException;

public class GitError extends MojoExecutionException {

    public GitError(int existCode, String stderr) {
        super("Git command failed with exit code " + existCode + ": " + (stderr == null || stderr.isEmpty() ? "<no output>" : stderr));
    }

}
