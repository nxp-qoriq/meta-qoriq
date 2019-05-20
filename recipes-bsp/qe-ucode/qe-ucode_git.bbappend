do_install () {
       install -d ${D}/boot
       install -m 644 ${B}/*.bin ${D}/boot
}

do_deploy () {
       install -d ${DEPLOYDIR}/boot
       install -m 644 ${B}/*.bin ${DEPLOYDIR}/boot
}
COMPATIBLE_MACHINE = "(qoriq)"

