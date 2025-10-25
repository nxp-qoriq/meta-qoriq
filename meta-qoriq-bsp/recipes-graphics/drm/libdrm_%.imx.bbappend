SRCREV = "9e42c1b2bfd30a43eafbfc7c45effd3abc0e202d"
PACKAGES:prepend:ls1028a = "${PN}-vivante "
RRECOMMENDS:${PN}-drivers:append:ls1028a = " ${PN}-vivante"
PACKAGECONFIG:append:ls1028a = " vivante"

COMPATIBLE_MACHINE:append = "|(ls1028a)"
