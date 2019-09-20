SUMMARY = "GPU libraries"
DESCRIPTION = "GPU libraries for Linux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5ab1a30d0cd181e3408077727ea5a2db"

SRC_URI = "git://bitbucket.sw.nxp.com/dash/gpulib.git;protocol=ssh;nobranch=1"
SRCREV = "5a4f7dfef1d3be15d5af0ffa693b60bf265f3f5d"

S = "${WORKDIR}/git"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}/opt ${D}${libdir} ${D}/usr/include
    cd  ls1028a/linux
    cp -a gpu-demos/opt/viv_samples/* ${D}/opt
    cp -a gpu-core/usr/include/* ${D}/usr/include 
    cp -a gpu-core/usr/lib/* ${D}/usr/lib
}

PACKAGES = "${PN}-dbg ${PN}"
FILES_${PN} = "${libdir}/* /opt/* /usr/include/*"
INSANE_SKIP_${PN} += "already-stripped file-rdeps dev-so host-user-contaminated"
COMPATIBLE_MACHINE = "(qoriq)"
