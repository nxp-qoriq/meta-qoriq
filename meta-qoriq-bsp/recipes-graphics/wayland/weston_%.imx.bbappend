SRCREV = "b8807ef38efdf96d33be59adbe364a6d58703b6b"

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
