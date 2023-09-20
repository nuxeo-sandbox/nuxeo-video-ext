# vim:set ft=dockerfile:
ARG BASE_IMAGE

FROM ${BASE_IMAGE}

ARG VERSION
ARG SCM_REF
ARG BUILD_TAG
ARG CLID
ARG PACKAGES

LABEL com.customer.sample.version=${VERSION}
LABEL com.customer.sample.scm-ref=${SCM_REF}
LABEL com.customer.sample.build-tag=${BUILD_TAG}

# We need to be root to run yum commands
USER 0

# Set UTF8 default locale
ENV LANG="en_US.UTF-8" \
    LANGUAGE="en_US:en" \
    LC_ALL="en_US.UTF-8"

# Install FFmpeg, mediainfo and codecs
RUN dnf config-manager --set-enabled crb && dnf -y install epel-release && \
    dnf -y install https://mirrors.rpmfusion.org/free/el/rpmfusion-free-release-9.noarch.rpm && \
    dnf -y install mediainfo ffmpeg x264 x265 opus flac libvpx && dnf --enablerepo=crb -y install libvorbis-devel

USER 900

COPY --chown=nuxeo:0 target/nuxeo-video-ext-package-*.zip /home/nuxeo/local-packages/nuxeo-video-ext-package.zip

RUN /install-packages.sh --offline /home/nuxeo/local-packages/nuxeo-video-ext-package.zip
