clean:
	gradle clean
shadowJar:
	gradle shadowJar
jpackage_rpm:
	jpackage --type rpm --verbose --input build/libs --dest build/libs --name Vifeco --main-jar vifeco-2.0.0.jar --main-class org.laeq.Launcher --module-path /home/david/.sdkman/candidates/java/11.0.10.fx-librca/jmods --add-modules java.base,java.logging,java.sql,javafx.controls,javafx.web,javafx.graphics,javafx.media,javafx.fxml
jpackage_deb:
	jpackage --type deb --verbose --input build/libs --dest build/libs --name Vifeco --main-jar vifeco-2.0.0.jar --main-class org.laeq.Launcher --module-path $PATH_TO_JMODS --add-modules java.base,java.logging,java.sql,javafx.controls,javafx.web,javafx.graphics,javafx.media,javafx.fxml
jpackage_windows:
	jpackage --type deb --verbose --input build/libs --dest build/libs --name Vifeco --main-jar vifeco-2.0.0.jar --main-class org.laeq.Launcher --module-path $PATH_TO_JMODS --add-modules java.base,java.logging,java.sql,javafx.controls,javafx.web,javafx.graphics,javafx.media,javafx.fxml
install_deb:
	sudo dpkg -i build/libs/vifeco_1.0-1_amd64.deb
remove_deb:
	sudo dpkg -r vifeco
install_rpm:
	sudo rpm -ivh build/libs/vifeco-1.0-1.x86_64.rpm
remove_rpm:
	sudo rpm -e vifeco-1.0-1.x86_64


all_rpm: clean shadowJar jpackage_rpm install_rpm
all_deb: clean shadowJar jpackage_deb install_deb
all_windows: clean shadowJar jpackage_windows