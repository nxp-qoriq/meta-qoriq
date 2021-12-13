PACKAGES:prepend:ls1028a = "${PN}-vivante "
RRECOMMENDS:${PN}-drivers:append:ls1028a = " ${PN}-vivante"
PACKAGECONFIG:append:ls1028a = " vivante"
