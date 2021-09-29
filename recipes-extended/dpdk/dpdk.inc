DESCRIPTION = "Data Plane Development Kit"
HOMEPAGE = "http://dpdk.org"
LICENSE = "BSD-3-Clause & LGPLv2.1 & GPLv2"
LIC_FILES_CHKSUM = "file://license/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://license/lgpl-2.1.txt;md5=4b54a1fd55a448865a0b32d41598759d \
                    file://license/bsd-3-clause.txt;md5=0f00d99239d922ffd13cabef83b33444"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/dpdk.git;protocol=https;nobranch=1"
SRCREV = "b33a7dbbcdcd0a063c262bc34f6051ce937dcdd4"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(imx|qoriq)"

CVE_PRODUCT = "data_plane_development_kit"
