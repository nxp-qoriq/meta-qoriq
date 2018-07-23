SUMMARY = "Open source Puppet is a configuration management system"
HOMEPAGE = "https://github.com/nxp/qoriq-eds-puppet.git"
LICENSE = "NXP-EULA"
LIC_FILES_CHKSUM = "file://EULA.txt;md5=ac5425aaed72fb427ef1113a88542f89"

SRC_URI = "git://github.com/nxp/qoriq-eds-puppet.git;nobranch=1"
SRCREV = "744cc466cb57c15fba21eb701326a8d3a49ee7e5"


RDEPENDS_${PN} = "puppet"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${sysconfdir}/puppetlabs/puppet/
    install -d ${D}${sysconfdir}/ssl/
    install -d ${D}${bindir}/

    install -m 655 ${S}/puppet.sh ${D}${bindir}
    install -m 655 ${S}/puppet.conf ${D}${sysconfdir}/puppetlabs/puppet/
    install -m 655 ${S}/openssl-sobj.cnf ${D}${sysconfdir}/ssl/
}
