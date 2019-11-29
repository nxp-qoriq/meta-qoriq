#set DISTRO_FEATURES in local.conf
# DISTRO_FEATURES_append = " wayland vulkan alsa"
# DISTRO_FEATURES_remove = " x11"
# LICENSE_FLAGS_WHITELIST_append = " commercial"


PACKAGE_IMX_TO_REMOVE_qoriq-arm64 = "clutter-1.0-examples"
CORE_IMAGE_EXTRA_INSTALL += " \
    kernel-module-qoriq-gpu \
"
