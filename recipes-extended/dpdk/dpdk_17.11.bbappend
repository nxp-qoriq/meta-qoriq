FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/dpdk;nobranch=1 \
        file://add-RTE_KERNELDIR_OUT-to-split-kernel-bu.patch \
        file://0001-add-Wno-cast-function-type.patch \
"
SRCREV = "c0fe1b99b562a4015423e8ff748bfb0f55a68c05"

FILES_${PN}-staticdev += "/usr/share/dpdk/cmdif/lib/*.a /usr/share/examples/cmdif/lib/arm64-dpaa-linuxapp-gcc/*.a \
    /usr/share/examples/cmdif/lib/arm64-dpaa-linuxapp-gcc/lib/*.a \
"
