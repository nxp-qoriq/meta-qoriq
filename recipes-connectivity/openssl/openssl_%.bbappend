do_install:append:qoriq () {
    cp --dereference -R crypto  ${D}${includedir}
}
