require recipes-graphics/imx-gpu-viv/imx-gpu-viv-6.inc

### Section 1: Changes to be upstreamed ###

do_install:prepend() {
    if [ "${IS_MX8}" = "1" ]; then
        if [ ! -z "${PACKAGES_VULKAN}" ]; then
            mv ${S}/gpu-core/etc/vulkan/icd.d/icd_VeriSilicon.json ${S}/gpu-core/etc/vulkan/icd.d/imx_icd.json
        fi
    fi
}

do_install:append() {
    if [ "${IS_MX8}" = "1" ]; then
        if [ ! -z "${PACKAGES_VULKAN}" ]; then
            mv ${D}${sysconfdir}/vulkan/icd.d/imx_icd.json ${D}${sysconfdir}/vulkan/icd.d/icd_VeriSilicon.json
        fi
    fi
}

### Section 2: Changes to be kept in meta-imx ###

PACKAGECONFIG ?= "valgrind"

RDEPENDS:libopenvg-imx += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-gpuconfig', '', d)}"

# FIXME: Don't add .so to main package
FILES:libvulkan-imx += "${libdir}/libvulkan_VSI${SOLIBS}"
INSANE_SKIP:libvulkan-imx += "dev-so"
FILES:libopenvx-imx += "${libdir}/libOpenVX${SOLIBS}"
INSANE_SKIP:libopenvx-imx += "dev-so dev-deps"
FILES:libopencl-imx += "${libdir}/libOpenCL${SOLIBS}"
INSANE_SKIP:libopencl-imx += "dev-so dev-deps"
FILES:libvdk-imx += "${libdir}/libVDK*${SOLIBS}"
INSANE_SKIP:libvdk-imx += "dev-so"
