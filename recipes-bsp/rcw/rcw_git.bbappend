RCW_BRANCH ?= "devel"
RCW_SRC ?= "git://bitbucket.sw.nxp.com/dash/dash-rcw;protocol=ssh"
SRC_URI = "${RCW_SRC};branch=${RCW_BRANCH}"
SRCREV = "e25dd0e103e8c8e1845848607ebd100c15fcd34a"

BOARD_TARGETS_lx2160ardb-rev2 = "lx2160ardb_rev2"
