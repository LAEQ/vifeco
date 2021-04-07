# vifeco
Vifeco (video features counter) is a stand-alone, multiplatform (Windows, Mac and Linux) application that makes it possible to manually identify features on a video. It allows you to create users, categories (feature) and organize them in collections. While reading a video, you can identify and manually features by positioning your mouse above it and pressing a predefined shortcut. Vifeco comes with tools to compare and analyze the counting concordance between two sessions. Written in Java 11 with the JavaFX UI toolkit, The software is available under Apache Licence

<a href="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/player_final.png"><img src="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/player_final.png" width="400"/></a>
<a href="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/category_list_final.png"><img src="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/category_list_final.png" width="350"/></a>
<a href="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/statistic_final.png"><img src="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/statistic_final.png" width="300" /></a>
<a href="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/collection_list_final.png"><img src="https://github.com/LAEQ/vifeco/raw/master/documentation/screenshots/collection_list_final.png" width="450" /></a>

### Build status

| Branch | status | DOI |
| ------------- | ------------- | ---- |
| Master  |  ![build status](https://travis-ci.org/LAEQ/vifeco.svg?branch=master)| [![DOI](https://zenodo.org/badge/165725219.svg)](https://zenodo.org/badge/latestdoi/165725219)


## Download
You can download prebuilt binaries from the [release section](https://github.com/LAEQ/vifeco/releases). You can find more information by reading the [instruction guide](https://github.com/LAEQ/vifeco/GUIDE.md).

## Requirements

| # | version |
| --- | --- |
| Gradle | 6.3 |
| Java | 11 |
| JavaFX | 11.0 |


## Our recommendation

For gradle installation please check https://gradle.org/install/

To compile and run Vifeco we recommand to use Liberica JDK 11 LTS from BellSoft. It is available for all the major platforms and comes bundled with Javafx. Linux and MacOS we recommand to use SDKMAN to install and manage multiple JDK/JRE. Windows users must download it directly from Bellsoft website and configure their environement manually (PATH, JAVA_HOME). With OpenJDK, you must install JavaFX separately and configure it at compile and runtime.

SDKMAN: https://sdkman.io/

JDK 11: https://bell-sw.com/pages/downloads/

Install OpenJFX: https://openjfx.io/openjfx-docs/#install-javafx

## Compile and test

```jshelllanguage
git clone https://github.com/LAEQ/vifeco.git
gradle test run
```

## JavaFX settings



```jshelllanguage
-Dprism.order=d3d   # Accelerated graphics pipeline Windows
-Dprism.order=es2    # Accelerated graphics pipeline Mac / OpenGL context
-Dprism.order=sw    # Software graphics pipeline
```

```bash
jvmArgs '-Dprism.vsync=false'
jvmArgs '-Djavafx.pulseLogger=true'
```

> The project will create a folder named **Vifeco** (~/vifeco) and will save all the necessary files to run the project (database, preferences, exports, imports, images, etc.).


### How to cite VIFECO?
VIFECO is a free software. Please cite the reference below, if you publish results based in part on the use of VIFECO:
             
Maignan, David, Philippe Apparicio and Jérémy Gelb (2019).
Video Feature Counter Software (version 1.1). 
Montreal, Laboratoire d’équité environnementale (LAEQ), INRS Urbanisation Culture Société.
