require recipes-fsl/images/fsl-image-core.bb

IMAGE_INSTALL += " \
    kernel-image-image \
    packagegroup-fsl-virtualization \
"

IMAGE_FSTYPES = "ext2.gz"

# copy rootfs image into rootfs
inherit fsl-utils
ROOTFS_POSTPROCESS_COMMAND += "rootfs_copy_core_image;"

do_rootfs[depends] += "fsl-image-core:do_image_complete"

export IMAGE_BASENAME = "fsl-image-virt"
