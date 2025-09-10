package com.davidecaroselli.bump;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "major")
public class MajorVersionMojo extends VersionMojo {

    @Override
    protected Version bump(Version version) {
        return new Version(version.major() + 1, 0, 0);
    }

}
