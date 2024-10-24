# Resourceful Lib
<hr>

The library mod behind team resourceful mods and more.

### Wiki

You can find our wiki [here](https://lib.wiki.teamresourceful.com/).

### License and Availability

The mod is licensed under MIT and is available on [Curseforge](https://www.curseforge.com/minecraft/mc-mods/resourceful-lib) and [Modrinth](https://modrinth.com/mod/resourceful-lib).

### Contributions

If you would like to contribute to the mod feel free to submit a PR.
<br>TODO: Add more info about importing the project in IntelliJ and any additional setup required.

## For Mod Developers
<hr>

Be sure to add our maven to your `build.gradle`:
```gradle
repositories {
    maven { url = "https://maven.resourcefulbees.com/repository/maven-public/" }
    <--- other repositories here --->
}
```
You can then add our mod as a dependency:

### Forge:
```gradle
dependencies {
    <--- Other dependencies here --->
    implementation fg.deobf("com.teamresourceful.resourcefullib:resourcefullib-forge-1.21.3:3.3.0")
}
```

### Fabric:
```gradle
dependencies {
    <--- Other dependencies here --->
    implementation "com.teamresourceful.resourcefullib:resourcefullib-fabric-1.21.3:3.3.0"
}
```

### Architectury:

#### Common `build.gradle`
```gradle
dependencies {
    <--- Other dependencies here --->
    modImplementation "com.teamresourceful.resourcefullib:resourcefullib-common-1.21.3:3.3.0"
}
```

#### Fabric `build.gradle`
```gradle
dependencies {
    <--- Other dependencies here --->
    modImplementation "com.teamresourceful.resourcefullib:resourcefullib-fabric-1.21.3:3.3.0"
}
```

#### Forge `build.gradle`
```gradle
dependencies {
    <--- Other dependencies here --->
    modImplementation "com.teamresourceful.resourcefullib:resourcefullib-forge-1.21.3:3.3.0"
}
```

TODO: Add Jar-in-Jar syntax