clean:
	gradle clean
shadowJar:
	gradle shadowJar
jpackage:
	jpackage --type deb --verbose --input build/libs --dest build/libs --name Vifeco --main-jar vifeco-2.0.0.jar --main-class org.laeq.Launcher --module-path /home/david/.sdkman/candidates/java/15.0.2.fx-librca/jmods --add-modules java.base,java.logging,javafx.controls,javafx.web,javafx.graphics,javafx.media,javafx.fxml
install:
	sudo dpkg -i build/libs/vifeco_1.0-1_amd64.deb
remove:
	sudo dpkg -r vifeco

all: clean shadowJar jpackage install