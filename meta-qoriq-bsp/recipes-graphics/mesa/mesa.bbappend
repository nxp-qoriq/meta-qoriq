PROVIDES:remove:ls1028a = "virtual/egl"
PROVIDES:remove:ls1028a = "virtual/libgles1 virtual/libgles2"

PACKAGECONFIG:class-native ?= "wayland-protocols gbm egl opengl"
PACKAGECONFIG:class-nativesdk ?= "wayland-protocols gbm egl opengl"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:ls1028ardb  = " file://0001-MGS-7599-cso-fix-virgl-driver-assert-issue.patch"

PACKAGECONFIG:remove:ls1028a = "egl gbm"
PACKAGECONFIG:remove:ls1028a = "gles"

# FIXME: mesa should support 'x11-no-tls' option
python () {
    overrides = d.getVar("OVERRIDES").split(":")
    if "imxgpu2d" not in overrides:
        return

    x11flag = d.getVarFlag("PACKAGECONFIG", "x11", False)
    d.setVarFlag("PACKAGECONFIG", "x11", x11flag.replace("--enable-glx-tls", "--enable-glx"))
}

# Enable Etnaviv and Freedreno support
PACKAGECONFIG:append:ls1028ardb = " gallium etnaviv kmsro freedreno"

# For NXP BSP, GPU drivers don't support dri
PACKAGECONFIG:remove:ls1028a = "dri"

# mainline/etnaviv:
RRECOMMENDS:${PN}-megadriver:append:ls1028ardb = " libdrm-etnaviv mesa-etnaviv-env"

BACKEND = \
    "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', \
        bb.utils.contains('DISTRO_FEATURES',     'x11',     'x11', \
                                                             'fb', d), d)}"

# FIXME: Dirty hack to allow use of Vivante GPU libGL binary
do_install:append:ls1028a () {
    rm -f ${D}${libdir}/libGL.* \
          ${D}${includedir}/GL/gl.h \
          ${D}${includedir}/GL/glcorearb.h \
          ${D}${includedir}/GL/glext.h \
          ${D}${includedir}/GL/glx.h \
          ${D}${includedir}/GL/glxext.h
    if [ "${BACKEND}" = "x11" ]; then
        rm -f ${D}${libdir}/pkgconfig/gl.pc
    fi
}

do_install:append:ls1028a () {
    rm -rf ${D}${includedir}/KHR
}
