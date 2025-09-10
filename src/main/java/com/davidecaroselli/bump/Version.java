package com.davidecaroselli.bump;

import java.util.Objects;

public final class Version implements Comparable<Version> {

    public static Version of(String version) throws IllegalArgumentException {
        if (version == null) throw new NullPointerException();

        String[] parts = version.split("\\.");
        if (parts.length == 0 || parts.length > 3)
            throw new IllegalArgumentException("Invalid version format: " + version);

        int[] parsed = new int[parts.length];

        try {
            for (int i = 0; i < parts.length; i++) {
                parsed[i] = Integer.parseInt(parts[i]);
                if (parsed[i] < 0) throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version format: " + version, e);
        }

        return new Version(parsed[0],
                parts.length > 1 ? parsed[1] : 0,
                parts.length > 2 ? parsed[2] : 0);
    }

    private final int major;
    private final int minor;
    private final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int major() {
        return major;
    }

    public int minor() {
        return minor;
    }

    public int patch() {
        return patch;
    }

    @Override
    public int compareTo(Version o) {
        if (o == null) throw new NullPointerException();

        if (this.major != o.major)
            return Integer.compare(this.major, o.major);
        if (this.minor != o.minor)
            return Integer.compare(this.minor, o.minor);
        if (this.patch != o.patch)
            return Integer.compare(this.patch, o.patch);

        return 0;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Version)) return false;

        Version version = (Version) o;
        return major == version.major && minor == version.minor && patch == version.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

}
