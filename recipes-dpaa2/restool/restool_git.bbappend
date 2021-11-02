inherit bash-completion

RDEPENDS_${PN}-bash-completion += "bash"

SRC_URI = "git://bitbucket.sw.nxp.com/dpaa2/restool;protocol=ssh;nobranch=1"
SRCREV = "7be71e7d0d0bce3c317c82308247ac901b8aa436"

EXTRA_OEMAKE += "MANPAGE="
do_compile_prepend() {
	sed -i '/call get_manpage_destination,$(MANPAGE)/d' ${S}/Makefile
}
