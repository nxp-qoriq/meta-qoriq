SUMMARY = "OPTEE Client"
HOMEPAGE = "https://github.com/qoriq-open-source/optee_client"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=69663ab153298557a59c67a60a743e5b"

PV="3.8.0+fslgit"

inherit python3native systemd

SRC_URI = "git://bitbucket.sw.nxp.com/dash/optee_client.git;protocol=ssh;branch=optee_client_3.8.0"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "ARCH=arm64"

do_install() {
    oe_runmake install

    install -D -p -m0755 ${S}/out/export/usr/sbin/tee-supplicant ${D}${bindir}/tee-supplicant
    install -D -p -m0755 ${S}/out/export/usr/lib/libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1
    ln -sf libteec.so.1 ${D}${libdir}/libteec.so

    cp -a ${S}/out/export/usr/include ${D}/usr/
}

COMPATIBLE_MACHINE = "(qoriq)"
