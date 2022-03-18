# what vivante driver does libsdl2 mean? Anyway it fails with missing functions as
# VIVANTE_Create VIVANTE_GLES_GetProcAddress VIVANTE_GLES_UnloadLibrary ...
EXTRA_OECMAKE:append:ls1028a = " -DSDL_VIVANTE=OFF"

CFLAGS:append:ls1028a = " -DLINUX \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', '-DEGL_API_FB', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-DWL_EGL_PLATFORM', '', d)} \
"
