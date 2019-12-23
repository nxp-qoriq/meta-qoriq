#set DISTRO_FEATURES in local.conf
# DISTRO_FEATURES_append = " wayland vulkan alsa"
# DISTRO_FEATURES_remove = " x11"
# LICENSE_FLAGS_WHITELIST_append = " commercial"
# PREFERRED_VERSION_weston = "7.0.0.imx"
# PREFERRED_VERSION_wayland-protocols = "1.18.imx"
# PREFERRED_VERSION_libdrm = "2.4.99.imx"
# PREFERRED_PROVIDER_virtual/libgl  = "imx-gpu-viv"
# PREFERRED_PROVIDER_virtual/libgles1 = "imx-gpu-viv"
# PREFERRED_PROVIDER_virtual/libgles2 = "imx-gpu-viv"
# PREFERRED_PROVIDER_virtual/egl      = "imx-gpu-viv"



PACKAGE_IMX_TO_REMOVE_qoriq-arm64 = "clutter-1.0-examples"
CORE_IMAGE_EXTRA_INSTALL += " \
    packagegroup-core-ssh-openssh \
    packagegroup-fsl-mfgtools \
    libopencl-imx \
    imx-gpu-viv-demos \
"
