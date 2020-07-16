SRCREV= "27acf2cf0ef88ebdb97245fd1ba2ef661ad0b66d"
LIC_FILES_CHKSUM = "file://LICENSE;md5=44a0d0fad189770cc022af4ac6262cbe"
do_install () {
    if [ ${M} = ls2088ardb ]; then
        oe_runmake BOARDS=${M} DESTDIR=${D}/boot/rcw/ install
        oe_runmake BOARDS=${M}_rev1.1  DESTDIR=${D}/boot/rcw/ install
    elif [ ${M} = ls1088ardb-pb ]; then
        oe_runmake BOARDS=ls1088ardb DESTDIR=${D}/boot/rcw/ install
    elif [ ${M} = lx2160ardb-rev2 ]; then
        oe_runmake BOARDS=lx2160ardb_rev2 DESTDIR=${D}/boot/rcw/ install
    else
        oe_runmake BOARDS=${M} DESTDIR=${D}/boot/rcw/ install
    fi
}
