package com.davidecaroselli.bump;

import com.davidecaroselli.bump.git.Git;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

abstract class VersionMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // only run for root project
        if (project.getParent() != null) return;

        Log log = getLog();

        Version version = getVersion();
        Version bumpedVersion = bump(version);
        log.info("Bumping version: " + version + " >> " + bumpedVersion);

        Git git = Git.open(project.getBasedir());

        // Check Git status
        if (git != null && !git.isClean())
            throw new MojoFailureException("Git working directory is not clean: " + git);
        if (git != null && git.hasHeadTag())
            throw new MojoFailureException("HEAD is already tagged: " + git);
        if (git != null && !git.isMainBranch())
            throw new MojoFailureException("Not on main branch: " + git);

        // Updating root POM
        PomFile pom = PomFile.read(project.getFile());
        if (setVersion(pom, version, bumpedVersion))
            pom.save();

        // Updating submodules' parent (if any)
        List<String> modules = project.getModules();
        if (modules != null) {
            for (String module : modules) {
                File moduleDir = new File(project.getBasedir(), module);
                PomFile modulePom = PomFile.read(new File(moduleDir, "pom.xml"));
                if (setParentVersion(modulePom, version, bumpedVersion) ||
                        setVersion(modulePom, version, bumpedVersion))
                    modulePom.save();
            }
        }

        // Tag the new version
        if (git != null)
            git.tag(bumpedVersion.toString());
    }

    protected abstract Version bump(Version version);

    private Version getVersion() throws MojoFailureException {
        try {
            return Version.of(project.getVersion());
        } catch (IllegalArgumentException e) {
            throw new MojoFailureException("Unsupported version format: " + project.getVersion());
        }
    }

    private boolean setVersion(PomFile pom, Version fromVersion, Version toVersion) {
        String pomVersion = pom.getVersion();
        if (fromVersion.toString().equals(pomVersion)) {
            pom.setVersion(toVersion);
            return true;
        } else {
            getLog().warn("Skipping POM " + pom.file() + " because of unexpected version " + pomVersion);
            return false;
        }
    }

    private boolean setParentVersion(PomFile pom, Version fromVersion, Version toVersion) {
        String pomVersion = pom.getParentVersion();
        if (fromVersion.toString().equals(pomVersion)) {
            pom.setParentVersion(toVersion);
            return true;
        } else {
            getLog().warn("Skipping POM " + pom.file() + " because of unexpected parent version " + pomVersion);
            return false;
        }
    }

}
