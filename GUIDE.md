## Guide

Vifeco is a JavaFX application written in Java 11 with the JavaFX UI toolkit, 

The latest \<version\> is **3.0.0**.

## Upgrade
Don’t be afraid to remove previous vifeco version – your settings and database won’t be lost. All settings are kept in the separate folder.

- Mac/linux: ~/vifeco.

- Windows: C:\Users\\<user\>\vifeco

--- 

### Windows
To install vifeo, download the binaries [vifeco-\<version\>.win-setup.exe](https://github.com/LAEQ/vifeco/releases) and follow the installer instructions.
To uninstall vifeco, go to Add/Remove program in system settings, search for vifeco and click uninstall.

---

### Mac
Download the binaries [vifeco-macos-installer-x64-\<version\>.pkg](https://github.com/LAEQ/vifeco/releases) and follow the installer instructions.
To start vifeco, open a terminal and run the following command
```bash
vifeco-<version>
```
  
To uninstall vifeco. 
Run the following command
```bash
sudo bash /Library/vifeco/<version>/uninstall.sh
```

---

### Linux (Debian distribution)
Download the binaries [vifeco_\<version\>_amd64.deb](https://github.com/LAEQ/vifeco/releases) and run the following commands to install, run and uninstall.
  
```bash
#Install
sudo dpkg -i vifeco_<version>_amd64.deb
vifeco &

#Uninstall
sudo dpkg -r vifeco

```

### Jar only
You must install Java 11 with JavaFX prior to run the jar only. We recommand Bellsoft Liberica [FullJRE 11](https://bell-sw.com/pages/downloads/#/java-11-lts). 

```bash
java -jar vifeco-<version>.jar
```


### Database
The H2 SQL database is a file located at ~/vifeco/db/vifeco.mv. You can use [DBeaver](https://dbeaver.io/download/) to connect to it.
