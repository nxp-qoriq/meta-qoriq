SUMMARY = "OPTEE Client"
HOMEPAGE = "http://www.optee.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

PV="3.10.0+fslgit"

inherit python3native systemd

SRC_URI = "git://bitbucket.sw.nxp.com/lfac/optee-client.git;protocol=ssh;branch=ls_3.10.y \
           file://tee-supplicant.service \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

EXTRA_OEMAKE = "ARCH=arm64 O=${B}"

do_install() {
    oe_runmake -C ${S} install

    install -D -p -m0755 ${B}/export/usr/sbin/tee-supplicant ${D}${bindir}/tee-supplicant
    install -D -p -m0755 ${B}/export/usr/lib/libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1
    ln -sf libteec.so.1 ${D}${libdir}/libteec.so

    cp -a ${B}/export/usr/include ${D}/usr/

    sed -i -e s:@sysconfdir@:${sysconfdir}:g -e s:@bindir@:${bindir}:g ${WORKDIR}/tee-supplicant.service
    install -D -p -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
}

SYSTEMD_SERVICE_${PN} = "tee-supplicant.service"

COMPATIBLE_MACHINE = "(qoriq-arm64)"
