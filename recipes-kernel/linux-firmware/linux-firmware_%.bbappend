# Copyright 2017-2022 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

IMX_FIRMWARE_SRC ?= "git://github.com/NXP/imx-firmware.git;protocol=https"
SRCBRANCH = "lf-5.15.32_2.0.0"
SRC_URI += " \
    ${IMX_FIRMWARE_SRC};branch=${SRCBRANCH};destsuffix=imx-firmware;name=imx-firmware \
"

SRCREV_imx-firmware = "03975c5dc83a8ebc9cabb411f3cc95873abbebe4"

SRCREV_FORMAT = "default_imx-firmware"

do_install:append () {

    # Install NXP Connectivity
    install -d ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/wifi_mod_para.conf    ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity SD8801 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8801_SD/ed_mac_ctrl_V1_8801.conf  ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8801_SD/sd8801_uapsta.bin         ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity 8987 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8987/ed_mac_ctrl_V3_8987.conf  ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8987/sdiouart8987_combo_v0.bin ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8987/txpwrlimit_cfg_8987.conf  ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity PCIE8997 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997/ed_mac_ctrl_V3_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997/pcieuart8997_combo_v4.bin ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997/txpwrlimit_cfg_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity SDIO8997 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997_SD/ed_mac_ctrl_V3_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997_SD/sdiouart8997_combo_v4.bin ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997_SD/txpwrlimit_cfg_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity PCIE9098 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_9098_PCIE/ed_mac_ctrl_V3_909x.conf  ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_9098_PCIE/pcieuart9098_combo_v1.bin ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_9098_PCIE/txpwrlimit_cfg_9098.conf  ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity SD9098 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_9098_SD/sdiouart9098_combo_v1.bin ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity IW416 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_IW416_SD/sdiouartiw416_combo_v0.bin ${D}${nonarch_base_libdir}/firmware/nxp
}

# Use the latest version of sdma firmware in firmware-imx
PACKAGES =+ " ${PN}-nxp89xx"

FILES:${PN}-nxp89xx = " \
       ${nonarch_base_libdir}/firmware/nxp/* \
"
