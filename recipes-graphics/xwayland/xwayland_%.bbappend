FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Prefer-to-create-GLES2-context-for-glamor-EGL.patch \
"

OPENGL_PKGCONFIGS:remove:ls1028a = "glx"
