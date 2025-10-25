SRCREV = "c3d839702294b57e5977a808e0c9cb4cff9581d8"
PACKAGECONFIG:append = " ${PACKAGECONFIG_G2D}"
PACKAGECONFIG_G2D              ??= ""
PACKAGECONFIG_G2D:imxgpu2d     ??= "imxg2d"
PACKAGECONFIG_G2D:mx93-nxp-bsp ??= "imxg2d"

# Remove no longer supported colord
PACKAGECONFIG[colord] = ""

# Weston with i.MX G2D renderer
PACKAGECONFIG[imxg2d] = "-Drenderer-g2d=true,-Drenderer-g2d=false,virtual/libg2d"

# links with imx-gpu libs which are pre-built for glibc
# gcompat will address it during runtime
LDFLAGS:append:ls1028ardb:libc-musl = " -Wl,--allow-shlib-undefined"

PACKAGE_ARCH = "${MACHINE_SOCARCH}"

COMPATIBLE_MACHINE = "(imxfbdev|imxgpu|qoriq)"
