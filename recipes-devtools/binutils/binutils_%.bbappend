do_install:class-native:append:qoriq-ppc () {
        rm -f ${D}/${bindir}/embedspu || :
}

do_install:append:qoriq-ppc () {
        # fix file conflict when multilib enabled
        # file /usr/bin/embedspu conflicts between attempted installs of
        # lib32-binutils-2.34-r0.ppce5500 and binutils-2.34-r0.ppc64e5500
        # we dont really care about embedspu for ppc so remove it
        rm -f ${D}/${bindir}/*embedspu || :
}

