FROM debian:9.5-slim
MAINTAINER David Maignan davidmaignan@gmail.com

ARG JAVA_VERSION="11.0.10.fx-librca"
ARG GRADLE_VERSION="6.3"

# Defining default non-root user UID, GID, and name
#ARG USER_UID="1000"
#ARG USER_GID="1000"
#ARG USER_NAME="vifeco"

# Creating default non-user
#RUN groupadd -g $USER_GID $USER_NAME && \
#	useradd -m -g $USER_GID -u $USER_UID $USER_NAME
#
#WORKDIR /home/$USER_NAME

SHELL ["/bin/bash", "-c"]

RUN apt-get update && \
	apt-get install -y zip unzip curl && \
	rm -rf /var/lib/apt/lists/* && \
	rm -rf /tmp/*

#USER $USER_UID:$USER_GID

RUN curl -s "https://get.sdkman.io" | bash
#
RUN bash -c "source /github/home/.sdkman/bin/sdkman-init.sh && \
    yes | sdk install java $JAVA_VERSION && \
    yes | sdk install gradle $GRADLE_VERSION && \
    rm -rf /github/home/.sdkman/archives/* && \
    rm -rf /github/home/.sdkman/tmp/*"

ENV GRADLE_HOME="/github/home/.sdkman/candidates/gradle/current"
ENV JAVA_HOME="/github/home/.sdkman/candidates/java/current"
ENV PATH="$GRADLE_HOME/bin:$JAVA_HOME/bin:$PATH"

ADD entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
#ENTRYPOINT bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && $0 $@"