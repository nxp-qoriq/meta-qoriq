inherit bash-completion

RDEPENDS_${PN}-bash-completion += "bash"

SRC_URI = "git://bitbucket.sw.nxp.com/dpaa2/restool;protocol=ssh;nobranch=1"
SRCREV = "abd2f5b7181db9d03db9e6ccda0194923b73e9a2"

EXTRA_OEMAKE += "MANPAGE="
do_compile_prepend() {
	sed -i '/call get_manpage_destination,$(MANPAGE)/d' ${S}/Makefile
}
