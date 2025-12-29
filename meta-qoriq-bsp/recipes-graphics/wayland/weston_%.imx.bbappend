SRCREV = "${AUTOREV}"
PACKAGECONFIG:append = " ${PACKAGECONFIG_PIPEWIRE}"
PACKAGECONFIG_PIPEWIRE             ??= ""
PACKAGECONFIG_PIPEWIRE:mx8-nxp-bsp ??= "pipewire"
PACKAGECONFIG_PIPEWIRE:mx9-nxp-bsp ??= "pipewire"

# links with imx-gpu libs which are pre-built for glibc
# gcompat will address it during runtime
LDFLAGS:append:ls1028ardb:libc-musl = " -Wl,--allow-shlib-undefined"

COMPATIBLE_MACHINE = "(imx-nxp-bsp|qoriq)"
