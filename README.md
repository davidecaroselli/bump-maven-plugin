# Bump! Maven Plugin

[![Maven Central](https://img.shields.io/maven-central/v/com.davidecaroselli/bump-maven-plugin.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/com.davidecaroselli/bump-maven-plugin)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

A simple Maven plugin to bump your project version just like `npm version patch|minor|major`.

It updates the version in your root **and submodule POMs**, commits the change, and tags it in Git.

---

## Features

* Increment **patch**, **minor**, or **major** version
* Handles **multi-module projects** (updates child POMs)
* Creates a Git **commit** and **tag** for the new version

---

## Usage

Add the plugin to your build:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.davidecaroselli</groupId>
      <artifactId>bump-maven-plugin</artifactId>
      <version>1.0.0</version>
    </plugin>
  </plugins>
</build>
```

Then run one of the following commands:

```bash
# bump patch version (1.2.3 → 1.2.4)
mvn version:patch

# bump minor version (1.2.3 → 1.3.0)
mvn version:minor

# bump major version (1.2.3 → 2.0.0)
mvn version:major
```

This will:
* update `pom.xml` (including submodules)
* commit the change (`vX.Y.Z`)
* create a Git tag (`vX.Y.Z`)

---

## Installation

The plugin is available from [Maven Central](https://central.sonatype.com/).
You can use it directly without extra setup:

```bash
mvn com.davidecaroselli:bump-maven-plugin:1.0.0:patch
```

or via the short alias:

```bash
mvn version:patch
```

---

## Requirements

* Java 8+
* Maven 3.8+

---

## License

Licensed under the [Apache 2.0 License](LICENSE).
