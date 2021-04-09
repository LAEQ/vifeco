## Guide

## Upgrade
Don’t be afraid to remove previous vifeco version – your settings won’t be lost. All settings are kept in the separate folder.

- Mac/linux: ~/vifeco.

- Windows: C:\Users\\<user\>\vifeco

### Windows
To install vifeo, download the binaries vifeco-<version>.win-setup.exe and follow the installer instructions.
To uninstall vifeco, go to Add/Remove program in system settings, search for vifeco and click uninstall.

### Mac
Download the binaries vifeco-macos-installer-x64-\<version\>.pkg and follow the installer instructions.
To start vifeco, open a terminal and run the following command
```bash
vifeco-2.0.0
```
  
To uninstall vifeco. 
Run the following command
```bash
sudo bash /Library/vifeco/<version>/uninstall.sh
```

### Linux (Debian distribution)
Download the binaries vifeco_\<version\>_amd64.deb.
  
```bash
#Install
sudo dpkg -i vifeco_<version>_amd64.deb
vifeco &

#Uninstall
sudo dpkg -r vifeco

```

### Jar only
You must install Java 11 with JavaFX prior to run the jar. We recommand Bellsoft Liberica [FullJRE 11](https://bell-sw.com/pages/downloads/#/java-11-lts). 


### Important
The H2 SQL database is located at ~/vifeco/db/vifeco.mv. You can use [DBeaver](https://dbeaver.io/download/) to connect to it.
