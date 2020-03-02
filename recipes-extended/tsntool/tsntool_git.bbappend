SRC_URI = "git://bitbucket.sw.nxp.com/dnind/tsntool.git;protocol=ssh;nobranch=1"
SRCREV= "ca2d8fb348bb54960d706177108c43ae213e0063"
do_install() {
    install -d ${D}${bindir} ${D}${libdir}
    install -m 0755 ${S}/tsntool ${D}${bindir}
    install -m 0755 ${S}/libtsn.so ${D}${libdir}
}

