do_install_class-native_append_qoriq-ppc () {
        rm -f ${D}/${bindir}/embedspu || :
}

do_install_append_qoriq-ppc () {
        # fix file conflict when multilib enabled
        # file /usr/bin/embedspu conflicts between attempted installs of
        # lib32-binutils-2.34-r0.ppce5500 and binutils-2.34-r0.ppc64e5500
        # we dont really care about embedspu for ppc so remove it
        rm -f ${D}/${bindir}/*embedspu || :
}

