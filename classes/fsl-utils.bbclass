rootfs_copy_core_image() {
    mkdir -p ${IMAGE_ROOTFS}/boot
    cp ${DEPLOY_DIR_IMAGE}/fsl-image-networking-${MACHINE}.ext2.gz ${IMAGE_ROOTFS}/boot/
}
