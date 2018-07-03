rootfs_delete_files() {
    rm -fr ${IMAGE_ROOTFS}/usr/include/crypto
    rm -fr ${IMAGE_ROOTFS}/usr/include/openssl
    rm -fr ${IMAGE_ROOTFS}/usr/include/optee
}
