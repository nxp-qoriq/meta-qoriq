FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:ls1028a = " file://Replace-glWindowPos2iARB-calls-with-glWindowPos2i.patch \
                    file://fix-clear-build-break.patch \
"

PACKAGECONFIG_REMOVE_IF_2D_ONLY          = ""
PACKAGECONFIG_REMOVE_IF_2D_ONLY:imxgpu2d = "gles1 gles2"
PACKAGECONFIG_REMOVE_IF_2D_ONLY:ls1028a = ""
PACKAGECONFIG_REMOVE_IF_GPU              = ""
PACKAGECONFIG_REMOVE_IF_GPU:ls1028a       = "glx x11"

PACKAGECONFIG:remove = " \
    ${PACKAGECONFIG_REMOVE_IF_2D_ONLY} \
    ${PACKAGECONFIG_REMOVE_IF_GPU} \
"

PACKAGECONFIG_APPEND_IF_GPU        = ""
PACKAGECONFIG_APPEND_IF_GPU:ls1028a = "glu"

PACKAGECONFIG:append = " \
    ${PACKAGECONFIG_APPEND_IF_GPU} \
"
