package com.davidecaroselli.bump;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

abstract class VersionMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.error("(" + getClass().getSimpleName() + ") Not yet implemented.");
        throw new MojoFailureException("Not implemented yet");
    }

    protected abstract Version bump(Version version);

}
