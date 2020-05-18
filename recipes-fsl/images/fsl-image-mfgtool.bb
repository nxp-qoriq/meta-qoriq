require fsl-image-minimal.bb

SUMMARY = "Small image commonly used for manufacturing or other small image needs."
DESCRIPTION = "Small image which only includes essential manufacturing \
packages to deploy other big images to large physical media, such as \
a USB stick or a hard drive."

LICENSE = "MIT"

IMAGE_INSTALL_append = " \
    ethtool \
    lmsensors-sensors \
    packagegroup-core-ssh-dropbear \
    packagegroup-fsl-mfgtools \
    restool \
"

export IMAGE_BASENAME = "fsl-image-mfgtool"
