FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"


SRC_URI_append = " file://weston.sh"

PACKAGECONFIG_append  = " imxgpu"

#PACKAGECONFIG_append  = " cairo-glesv2"
PACKAGECONFIG[kms] = "--enable-drm-compositor,--disable-drm-compositor,drm udev virtual/egl virtual/libgles2 virtual/libgbm mtdev"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,virtual/egl virtual/libgles2"

do_install_append() {
     # install default weston.sh
     install -Dm755 ${WORKDIR}/weston.sh ${D}${sysconfdir}/xdg/weston/weston.sh

}

COMPATIBLE_MACHINE = "(imxfbdev|imxgpu|qoriq-arm64)"


