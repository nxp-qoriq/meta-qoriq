inherit bash-completion

RDEPENDS_${PN}-bash-completion += "bash"

SRC_URI = "git://bitbucket.sw.nxp.com/dpaa2/restool;protocol=ssh;nobranch=1"
SRCREV = "8fd3a6b3ba28a6d980a612f2064e6f8827744bcd"

EXTRA_OEMAKE += "MANPAGE="
do_compile_prepend() {
	sed -i '/call get_manpage_destination,$(MANPAGE)/d' ${S}/Makefile
}
