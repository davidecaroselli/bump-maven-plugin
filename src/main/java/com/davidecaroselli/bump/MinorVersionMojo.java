package com.davidecaroselli.bump;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "minor")
public class MinorVersionMojo extends VersionMojo {

    @Override
    protected Version bump(Version version) {
        return new Version(version.major(), version.minor() + 1, 0);
    }

}
