do_install(){
    mkdir -p ${D}/${libdir}
    mkdir -p ${D}/${includedir} ${D}/${bindir}
    cp ${S}/out/export/lib/libpkcs11.so  ${D}/${libdir}
    cp ${S}/out/export/include/*.h  ${D}/${includedir}
    cp ${S}/out/export/app/*  ${D}/${bindir}
}
