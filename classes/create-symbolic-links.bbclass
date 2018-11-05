rootfs_create_symbolic_link() {
    cd ${IMAGE_ROOTFS}/lib/
    ln -sf ld-2.28.so ld-linux.so.3
    cd -
}
