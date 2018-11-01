do_install_class-native_append_qoriq-ppc () {
        rm -f ${D}/${bindir}/embedspu || :
}


do_install_append_qoriq-ppc () {
        # we dont really care about embedspu for ppc so remove it
        rm -f ${D}/${bindir}/*embedspu || :
}

