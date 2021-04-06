## Guide

### Windows
Download the binaries vifeco-<version>.win-setup.exe. Double click and follow the instructions.
To uninstall vifeco, search for Add/Remove program in the system settings. 

### Mac
Download the binaries vifeco-macos-installer-x64-<version>.pkg. Double click and follow the instructions.
Run vifeco, open a terminal and runt the following command
```bash
vifeco-2.0.0
```
  
Uninstall vifeco. 
Run the following command
```bash
sudo bash /Library/vifeco/<version>/uninstall.sh
```

You must delete manually the folder ~/vifeco created during the installation.

### Linux (Debian distribution)
Download the binaries vifeco_<version>_amd64.deb.
  
```bash
#Install
sudo dpkg -i vifeco_<version>_amd64.deb

#Uninstall
sudo dpkg -r vifeco

```

### Jar only
You must install Java 11 with JavaFX prior to run the jar.

### Important
You must delete manually the folder ~/vifeco (C:\Users\<user>\vifeco) created during the installation.

