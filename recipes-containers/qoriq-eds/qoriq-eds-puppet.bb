SUMMARY = "Open source Puppet is a configuration management system"
HOMEPAGE = "https://github.com/nxp/qoriq-eds-puppet.git"
LICENSE = "NXP-EULA"
LIC_FILES_CHKSUM = "file://EULA.txt;md5=d969f2c93b3905d4b628787ce5f8df4b"

SRC_URI = "git://github.com/nxp/qoriq-eds-puppet.git;nobranch=1"
SRCREV = "62b21601e045e472604cd6680a1a4eb835da0539"


RDEPENDS_${PN} = "puppet"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${sysconfdir}/puppetlabs/puppet/
    install -d ${D}${bindir}/

    install -m 655 ${S}/puppet.sh ${D}${bindir}
    install -m 655 ${S}/puppet.conf ${D}${sysconfdir}/puppetlabs/puppet/
}
