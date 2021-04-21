do_install_append_qoriq () {
    cp --dereference -R crypto  ${D}${includedir}
}
