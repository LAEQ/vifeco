FROM debian:9.5-slim
MAINTAINER David Maignan davidmaignan@gmail.com

ARG JAVA_VERSION="11.0.10.fx-librca"
ARG GRADLE_VERSION="6.3"

#ARG USER_UID="1000"
#ARG USER_GID="1000"
#ARG USER_NAME="actions"
#RUN groupadd -g $USER_GID $USER_NAME && \
#	useradd -m -g $USER_GID -u $USER_UID $USER_NAME

WORKDIR /home

SHELL ["/bin/bash", "-c"]

RUN apt-get update && \
	apt-get install -y zip unzip curl && \
	rm -rf /var/lib/apt/lists/* && \
	rm -rf /tmp/*

USER $USER_UID:$USER_GID

RUN curl -s "https://get.sdkman.io" | bash

RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    yes | sdk install java $JAVA_VERSION && \
    yes | sdk install gradle $GRADLE_VERSION && \
    rm -rf $HOME/.sdkman/archives/* && \
    rm -rf $HOME/.sdkman/tmp/*"

#ADD entrypoint.sh /entrypoint.sh
#RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
#ENTRYPOINT bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && $0 $@"