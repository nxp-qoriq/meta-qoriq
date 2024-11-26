require recipes-core/images/core-image-minimal.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

CORE_IMAGE_EXTRA_INSTALL += "udev-extraconf lsb-release"
CORE_IMAGE_EXTRA_INSTALL:append:qoriq = " udev-rules-qoriq"
CORE_IMAGE_EXTRA_INSTALL:append:ls1028ardb = " \
    libopencl-imx \
    imx-gpu-viv-demos \
    packagegroup-fsl-tools-gpu \
    packagegroup-fsl-gstreamer1.0 \
    packagegroup-imx-tools-audio \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', \
                         'weston weston-init weston-examples \
                          gtk+3-demo', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', \
                         'weston-xwayland xterm', '', d)} \
    packagegroup-fsl-gstreamer1.0-full \
"
CORE_IMAGE_EXTRA_INSTALL:remove:ls1028ardb = "${PACKAGE_IMX_TO_REMOVE}"
PACKAGE_IMX_TO_REMOVE = ""
PACKAGE_IMX_TO_REMOVE:imxgpu2d = "gtk+3-demo"
PACKAGE_IMX_TO_REMOVE:imxgpu3d = ""
PACKAGE_IMX_TO_REMOVE:qoriq-arm64 = "clutter-1.0-examples"

IMAGE_FSTYPES = "tar.gz cpio.gz cpio.gz.u-boot"

SUMMARY = "Small image to be used for evaluating the Freescale socs"
DESCRIPTION = "Small image which includes some helpful tools and \
Freescale-specific packages. It is much more embedded-oriented \
than fsl-image-networking-full to evaluate the Freescale socs."

LICENSE = "MIT"

IMAGE_INSTALL:append = " \
    packagegroup-core-ssh-openssh \
    packagegroup-fsl-mfgtools \
    packagegroup-fsl-tools-core \
    packagegroup-fsl-benchmark-core \
    packagegroup-fsl-networking-core \
"

IMAGE_FEATURES:append:ls1028ardb = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston', \
       bb.utils.contains('DISTRO_FEATURES',     'x11', 'x11-base', \
                                                       '', d), d)} \
"
