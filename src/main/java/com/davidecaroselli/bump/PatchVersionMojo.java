package com.davidecaroselli.bump;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "patch")
public class PatchVersionMojo extends VersionMojo {

    @Override
    protected Version bump(Version version) {
        return new Version(version.major(), version.minor(), version.patch() + 1);
    }

}
