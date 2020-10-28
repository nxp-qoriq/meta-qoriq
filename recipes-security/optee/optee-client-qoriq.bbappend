SRC_URI = "git://bitbucket.sw.nxp.com/dash/optee_client.git;protocol=ssh;nobranch=1"
SRCREV = "be4fa2e36f717f03ca46e574aa66f697a897d090"

do_install() {
    oe_runmake install

    install -D -p -m0755 ${S}/out/export/usr/sbin/tee-supplicant ${D}${bindir}/tee-supplicant
    install -D -p -m0755 ${S}/out/export/usr/lib/libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1
    ln -sf libteec.so.1 ${D}${libdir}/libteec.so

    cp -a ${S}/out/export/usr/include ${D}/usr/
}

