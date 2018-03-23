do_install_append () {
    cp --dereference -R crypto  ${D}${includedir}
}
