inherit bash-completion

RDEPENDS_${PN}-bash-completion += "bash"

SRCREV = "d29522aef9f92ff2557978d5d3979b771a9576fe"

EXTRA_OEMAKE += "MANPAGE="
do_compile_prepend() {
	sed -i '/call get_manpage_destination,$(MANPAGE)/d' ${S}/Makefile
}
