CFLAGS_append_qoriq-arm64 = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', '-DEGL_API_FB', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-DWL_EGL_PLATFORM', '', d)} \
"
